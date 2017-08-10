package net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import request.IllegalRequestException;
import request.Interface;
import request.interrupt.EpollInterruptListenerManager;
import request.interrupt.InterruptListenerManager;
import request.manager.BoardManager;
import request.manager.BoardManagerBulldogImpl;
import request.manager.GpioManagerBulldogImpl;
import request.manager.I2cManagerBulldogImpl;
import request.manager.InterfaceManager;
import request.manager.SpiManagerBulldogImpl;

/**
 * Responsibility: manage all the connections binded to the device.
 *
 * @author Miloslav Zezulka, 2017
 */
public class ConnectionManager implements Runnable {

    private static ServerSocketChannel serverSocketChannel;
    private static SocketChannel socketChannel;
    private static Selector selector;
    /**
     * Default socket port.
     */
    public static final int DEFAULT_SOCK_PORT = 8088;
    private static int port = DEFAULT_SOCK_PORT;
    private static String messageToSend;

    /**
     * Timeout used in connection manager thread. Generally, the time which the
     * agent waits for incoming connections for.
     */
    public static final long TIMEOUT = 10 * 1000;

    private static final ConnectionManager INSTANCE = new ConnectionManager();
    private static final BoardManager BOARD_MANAGER = BoardManagerBulldogImpl.getInstance();
    private static final Function<Interface, InterfaceManager> CONVERTER = (t) -> {
        switch (t) {
            case GPIO:
                return GpioManagerBulldogImpl.getInstance(BOARD_MANAGER);
            case I2C:
                return I2cManagerBulldogImpl.getInstance(BOARD_MANAGER);
            case SPI:
                return SpiManagerBulldogImpl.getInstance(BOARD_MANAGER);
            default:
                throw new IllegalArgumentException();
        }
    };
    
    private static final ProtocolManager PROTOCOL_MANAGER = ProtocolManager.getInstance(CONVERTER);
    private static final InterruptListenerManager LISTENER_MANAGER = EpollInterruptListenerManager.getInstance();
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

    /**
     * Creates connection manager with default socket port.
     */
    private ConnectionManager() {
    }

    /**
     * Creates connection manager with the port given.
     *
     * @param port
     */
    private ConnectionManager(int port) {
        ConnectionManager.port = port;
    }

    /**
     * Prepares agent for accepting connection (on the network level).
     * This method must be executed only once per client
     * (i.e. resources must be initialised only once). Should the method be
     * executed more than once, such attempt is ignored.
     */
    private void prepareForAcceptingConnection() {
        LOGGER.info(ProtocolMessages.S_SERVER_INIT.toString());
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(true);
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.socket().setReuseAddress(true);
            serverSocketChannel.configureBlocking(false);
        } catch (IOException ex) {
            LOGGER.error(ProtocolMessages.S_IO_EXCEPTION.toString(), ex);
        }
        LOGGER.info(ProtocolMessages.S_SERVER_INIT_SUCCESS.toString());
    }

    private void registerAcceptOperation() {
       try {
           LOGGER.info("now accepting connection from client");
           serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
       } catch(IOException ex) {
           LOGGER.error(ex.getMessage());
       }
    }

    /**
     * Returns connection manager which listens to the default port
     * {@code AgentConnectionManager.DEFAULT_SOCKET_PORT}.
     *
     * @return
     */
    public static ConnectionManager getManagerWithDefaultPort() {
        return INSTANCE;
    }

    public static void setMessageToSend(String message) {
        LOGGER.debug(String.format("setMessageToSend called, input %s", message));
        messageToSend = message;
        if (message != null) {
            try {
                socketChannel.register(selector, SelectionKey.OP_WRITE);
                selector.wakeup();
            } catch (ClosedChannelException ex) {
                LOGGER.error("There has been an attempt to "
                        + "register write operation on channel which has been closed.");

            }
        }
    }

    /**
     * Static factory method used for getting instance of an agent.
     *
     * @param port
     * @return
     */
    public static ConnectionManager getManager(int port) {
        return new ConnectionManager(port);
    }

    /**
     * Runs in an infinite loop, therefore the only way to stop this thread from
     * running is to kill the whole process.
     */
    @Override
    public void run() {
        LOGGER.info("Agent launched.");
        prepareForAcceptingConnection();
        while (true) {
            registerAcceptOperation();
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
            while (true) {
                if (!selector.isOpen()) {
                    return;
                }
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                if(!processRegisteredKeys(keys)) {
                    return;
                }
            }
        } catch (IOException ex) {
            LOGGER.error(ProtocolMessages.S_IO_EXCEPTION.toString(), ex);
        } finally {
            closeConnection();
        }
    }

    private boolean processRegisteredKeys(Iterator<SelectionKey> keys) throws IOException {
        while (keys.hasNext()) {
            SelectionKey key = keys.next();
            keys.remove();
            if(!processNextKey(key)) {
                return false;
            }
        }
        return true;
    }

    private boolean processNextKey(SelectionKey key) throws IOException {
        if (!key.isValid()) {
            return false;
        }
        if (key.isAcceptable()) {
            accept();
        }
        if (key.isReadable()) {
            String receivedMessage = read();
            if (receivedMessage == null) {
                return false;
            }
            try {
                PROTOCOL_MANAGER.parseRequest(receivedMessage);
            } catch (IllegalRequestException ex) {
                LOGGER.error(null, ex);
                setMessageToSend(ProtocolMessages.S_ILLEGAL_REQUEST.toString());
            }
        }
        if (key.isWritable()) {
            write(key);
        }
        return true;
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
        setMessageToSend("INIT:"+BOARD_MANAGER.getBoardName());
        socketChannel.register(selector, SelectionKey.OP_WRITE);
    }

    /**
     * Performs the actual write operation. Reads data stored in internal buffer
     * and attempts to send them to the output stream.
     *
     * @param key
     */
    private void write(SelectionKey key) {
        LOGGER.info(ProtocolMessages.S_CLIENT_FEEDBACK.toString());
        try {
          socketChannel.write(ByteBuffer.wrap(messageToSend.getBytes()));
          key.interestOps(SelectionKey.OP_READ);
        } catch(IOException ex) {
            LOGGER.info(ex.getMessage());
        }
        setMessageToSend(null);
    }

    /**
     * Reads data stored in internal buffer.
     *
     * @param key
     * @return String representation of data received. Data sent to the server
     * should be a sequence of characters convertible into String.
     * @throws IOException
     */
    private String read() throws IOException {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        readBuffer.clear();
        int read = socketChannel.read(readBuffer);
        if (read == -1) {
            LOGGER.info(ProtocolMessages.S_NOTHING_TO_READ.toString());
            return null;
        }
        readBuffer.flip();
        byte[] data = new byte[1024];
        readBuffer.get(data, 0, read);
        return new String(data).replaceAll("\0", "");
    }

    /*
     * Closes connection.
     */
    private void closeConnection() {
        try {
            LOGGER.info(String.format("Connection closed with %s", socketChannel.getRemoteAddress().toString()));
            socketChannel.close();
            LISTENER_MANAGER.clearAllListeners();
        } catch(IOException ex) {
            LOGGER.error(ex.getMessage());
        }

    }
}
