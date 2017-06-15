package net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import request.IllegalRequestException;
import request.manager.BoardManager;
import request.manager.BoardManagerBulldogImpl;
import request.manager.GpioManagerBulldogImpl;
import request.manager.I2cManagerBulldogImpl;
import request.manager.SpiManagerBulldogImpl;

/**
 * Responsibility: manage all the connections binded to the device.
 *
 * @author Miloslav Zezulka, 2017
 */
public class AgentConnectionManager implements Runnable {

    private ServerSocketChannel serverSocketChannel;
    private SocketChannel socketChannel;
    private Selector selector;
    /**
     * Default socket port.
     */
    public static final int DEFAULT_SOCK_PORT = 8088;
    private static int port = DEFAULT_SOCK_PORT;

    /**
     * Timeout used in connection manager thread. Generally, the time which the
     * agent waits for incoming connections for.
     */
    public static final long TIMEOUT = 10 * 1000;

    private static final AgentConnectionManager INSTANCE = new AgentConnectionManager();
    private static final ProtocolManager PROTOCOL_MANAGER = ProtocolManager.getInstance();
    private static final BoardManager BOARD_MANAGER = BoardManagerBulldogImpl.getInstance();
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentConnectionManager.class);

    /**
     * Creates connection manager with default socket port.
     */
    private AgentConnectionManager() {
    }

    /**
     * Creates connection manager with the port given.
     *
     * @param port
     */
    private AgentConnectionManager(int port) {
        AgentConnectionManager.port = port;
    }

    /**
     * Initializes server. This method must be executed only once per client (i.e.
     * resources must be initialized only once). Should the method be executed
     * more than once, such attempt is ignored.
     */
    private void init() {
        if (selector != null || serverSocketChannel != null) {
            LOGGER.error(ProtocolMessages.S_ERR_INIT_MORE_THAN_ONCE.toString());
            return;
        }
        LOGGER.info(ProtocolMessages.S_SERVER_INIT.toString());
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(true);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException ex) {
            LOGGER.error(ProtocolMessages.S_IO_EXCEPTION.toString(), ex);
        }
        LOGGER.info(ProtocolMessages.S_SERVER_INIT_SUCCESS.toString());
    }

    /**
     * Returns connection manager which listens to the default port
     * {@code AgentConnectionManager.DEFAULT_SOCKET_PORT}.
     *
     * @return
     */
    public static AgentConnectionManager getManagerWithDefaultPort() {
        return INSTANCE;
    }

    /**
     * Static factory method used for getting instance of an agent.
     *
     * @param port
     * @return
     */
    public static AgentConnectionManager getManager(int port) {
        return new AgentConnectionManager(port);
    }

    private SocketChannel getSocketChannel() {
        return this.socketChannel;
    }

    private Selector getSelector() {
        return selector;
    }

    /**
      * Runs in an infinite loop, therefore the only way to stop this thread
      * from running is to kill the whole process.
      */
    @Override
    public void run() {
        LOGGER.info("Agent successfully launched.");
        while(true) {
          init();
          runImpl();
        }
    }

    /*
     * Before running this method, connection must have already been established with a client!
     *
     * This implementation is based on non-blocking IO standard library, Java NIO.
     * Firstly, agent waits for incoming requests (thread is put to sleep
     * for the time specified in the TIMEOUT constant).
     *
     * Firstly, the agent tries to send inital message (which is the name of the device),
     * which can be considered to be the handshake part of the protocol.
     *
     * The only request which arrives from client is the one handled by the {@code read()} method.
     * Accepted message is processed and then parsed. Should any error occur during
     * this parse, IllegalRequestException is thrown and error message is send back
     * to client.
     *
     * Agent communicates with client using the {@code write()} method.
     *
     */
    private void runImpl() {
      try {
          while (true /*!Thread.currentThread().isInterrupted()*/) {
              if(!selector.isOpen()) {
                  return;
              }
              selector.select(TIMEOUT);
              Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

              while (keys.hasNext()) {
                  SelectionKey key = keys.next();
                  if (!key.isValid()) {
                      keys.remove();
                      continue;
                  }
                  if (key.isAcceptable()) {
                      accept();
                      LOGGER.info(ProtocolMessages.S_CONNECTION_ACCEPT.toString());
                  }
                  if (key.isReadable()) {
                      int readVal = read();
                      if (readVal == -1) {
                          return;
                      }
                      if (PROTOCOL_MANAGER.getReceivedMessage() == null) {
                          continue;
                      }
                      try {
                          PROTOCOL_MANAGER.parseRequest((t) -> {
                              switch(t) {
                                  case GPIO: return GpioManagerBulldogImpl.getInstance(BOARD_MANAGER);
                                  case I2C : return I2cManagerBulldogImpl.getInstance(BOARD_MANAGER);
                                  case SPI : return SpiManagerBulldogImpl.getInstance(BOARD_MANAGER);
                                  default : throw new IllegalArgumentException();
                              }
                          });
                      } catch (IllegalRequestException ex) {
                          LOGGER.error(null, ex);
                          ProtocolManager.getInstance().setMessageToSend("Illegal Request.");
                      }
                  }
                  if (key.isWritable() && PROTOCOL_MANAGER.getMessageToSend() != null) {
                      write(key);
                  }
                  keys.remove();
              }
          }
      } catch (IOException ex) {
          LOGGER.error(ProtocolMessages.S_IO_EXCEPTION.toString(), ex);
      } finally {
           closeConnection();
      }
    }

    /**
     * Enables server to accept incoming connections. Accept method also stores
     * initial message into map which contains name of the device, thus forcing
     * server to send this message as the first one.
     *
     * @param key
     * @throws IOException
     */
    private void accept() throws IOException {
        socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        PROTOCOL_MANAGER.setMessageToSend(BOARD_MANAGER.getBoardName());
        socketChannel.register(selector, SelectionKey.OP_WRITE);
    }

    /**
     * Performs the actual write operation. Reads data stored in internal buffer
     * and attempts to send them to the output stream.
     *
     * @param key
     */
    private void write(SelectionKey key) throws IOException {
        LOGGER.info(ProtocolMessages.S_CLIENT_FEEDBACK.toString());
        socketChannel.write(ByteBuffer.wrap(PROTOCOL_MANAGER.getMessageToSend().getBytes()));
        key.interestOps(SelectionKey.OP_READ);
        PROTOCOL_MANAGER.resetMessageToSend();
    }

    /**
     * Reads data stored in internal buffer.
     *
     * @param key
     * @return String representation of data received. Data sent to the server
     * should be a sequence of characters convertible into String.
     * @throws IOException
     */
    private int read() throws IOException {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        readBuffer.clear();
        int read = socketChannel.read(readBuffer);
        if (read == -1) {
            LOGGER.info(ProtocolMessages.S_NOTHING_TO_READ.toString());
            return read;
        }
        readBuffer.flip();
        byte[] data = new byte[1024];
        readBuffer.get(data, 0, read);
        String msg = new String(data).replaceAll("\0", "");
        PROTOCOL_MANAGER.setReceivedMessage(msg);
        socketChannel.register(selector, SelectionKey.OP_WRITE);
        return 0;
    }

    /*
     * Closes connection and cleans up all network resources.
     */
    private void closeConnection() {
      try {
        if(serverSocketChannel != null) {
            LOGGER.info(String.format("Connection closed with %s", socketChannel.getRemoteAddress().toString()));
        }
        if (selector != null) {
            selector.close();
            selector = null;
        }
        BOARD_MANAGER.cleanUpResources();
        serverSocketChannel.socket().close();
        serverSocketChannel.close();
        serverSocketChannel = null;
      } catch(IOException ex) {
        LOGGER.error(null, ex);
      }
    }
}
