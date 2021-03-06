package net;

import board.BoardManagerFactory;
import board.test.BoardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.Feature;
import protocol.ProtocolManager;
import protocol.ProtocolMessages;
import protocol.request.IllegalRequestException;
import protocol.request.InitMessage;
import protocol.request.interrupt.AgentInterruptListenerManager;
import protocol.request.interrupt.InterruptListenerManager;
import util.ApplicationProperties;
import util.Unix;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Objects;

/**
 * Responsibility: manage all the connections binded to the device.
 */
public final class ConnectionManager implements Runnable {

    private static final int BUFF_SIZE = 1024;
    private static final int END_OF_STREAM = -1;
    private static final ConnectionManager INSTANCE = new ConnectionManager();
    private static final BoardManager BOARD_MANAGER
            = BoardManagerFactory.getInstance();
    private static final ProtocolManager PROTOCOL_MANAGER
            = new ProtocolManager(BOARD_MANAGER.deviceToInterfaceMapper());
    private static final InterruptListenerManager LISTENER_MANAGER
            = AgentInterruptListenerManager.getInstance();
    private static final Logger LOGGER
            = LoggerFactory.getLogger(ConnectionManager.class);
    private static ServerSocketChannel serverSocketChannel;
    private static SocketChannel socketChannel;
    private static Selector selector;
    private static String messageToSend;
    private int port;

    private ConnectionManager() {
        this.port = ApplicationProperties.socketPort();
    }

    /**
     * Returns connection manager which listens to the default port
     * {@code AgentConnectionManager.DEFAULT_SOCKET_PORT}.
     */
    public static ConnectionManager getDefaultManager() {
        return INSTANCE;
    }

    public static void setMessage(String message) {
        Objects.requireNonNull(message, "message");
        messageToSend = message;
        try {
            socketChannel.register(selector, SelectionKey.OP_WRITE);
            selector.wakeup();
        } catch (ClosedChannelException ex) {
            LOGGER.error("There has been an attempt to "
                    + "register write operation on closed channel.", ex);
        }
    }

    /**
     * Prepares agent for accepting connection (on the network level). This
     * method must be executed only once per client (i.e. resources must be
     * initialised only once). Should the method be executed more than once,
     * such attempt is ignored.
     */
    private void preAccept() {
        LOGGER.debug(ProtocolMessages.SERVER_INIT.toString());
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(true);
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.socket().setReuseAddress(true);
            serverSocketChannel.configureBlocking(false);
            LOGGER.info(String.format("Listening on port %d.", port));
        } catch (IOException ex) {
            LOGGER.error(ProtocolMessages.IO_EXCEPTION.toString(), ex);
        }
        LOGGER.debug(ProtocolMessages.SERVER_INIT_SUCCESS.toString());
    }

    private void registerAccept() {
        try {
            LOGGER.info("Ready to accept connection.");
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    /**
     * Runs in an infinite loop, therefore the only way to stop this thread from
     * running is to kill the whole process.
     */
    @Override
    public void run() {
        LOGGER.info("Launched.");
        LOGGER.info("Available features:");
        for (Feature f : Unix.getAppFeatures()) {
            LOGGER.info("\t" + f.toString());
        }
        preAccept();
        while (true) {
            registerAccept();
            runImpl();
        }
    }

    /*
     * Before running this method, connection must have already been established
     * with a client!
     *
     * This implementation is based on Java NIO.
     * Firstly, agent waits for incoming requests (thread is put to sleep
     * for the time specified in the TIMEOUT constant).
     *
     * Firstly, the agent tries to send inital message
     * (which is the name of the device), which can be considered to be the
     * handshake part of the protocol.
     *
     * The only request which arrives from client is the one handled by the
     * {@code read()} method.
     * Accepted message is processed and then parsed. Should any error occur
     * during this parse, IllegalRequestException is thrown and error message
     * is send back to client.
     *
     * Agent communicates with client using the {@code write()} method.
     *
     */
    private void runImpl() {
        try {
            while (!Thread.interrupted()) {
                if (!selector.isOpen()) {
                    return;
                }
                selector.select();
                Iterator<SelectionKey> keys
                        = selector.selectedKeys().iterator();
                if (!processRegisteredKeys(keys)) {
                    return;
                }
            }
        } catch (IOException ex) {
            LOGGER.error(ProtocolMessages.IO_EXCEPTION.toString(), ex);
        } finally {
            closeConnection();
        }
    }

    private boolean processRegisteredKeys(Iterator<SelectionKey> keys)
            throws IOException {
        while (keys.hasNext()) {
            SelectionKey key = keys.next();
            keys.remove();
            if (!processNextKey(key)) {
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
                LOGGER.info(ProtocolMessages.REQUEST_OK.toString());
            } catch (IllegalRequestException ex) {
                LOGGER.error(null, ex);
                setMessage(ProtocolMessages.ILLEGAL_REQUEST.toString());
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
     */
    private void accept() throws IOException {
        socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        setMessage(new InitMessage(BOARD_MANAGER).getFormattedResponse());
        socketChannel.register(selector, SelectionKey.OP_WRITE);
    }

    /**
     * Performs the actual write operation. Reads data stored in internal buffer
     * and attempts to send them to the output stream.
     */
    private void write(SelectionKey key) {
        try {
            socketChannel.write(ByteBuffer.wrap(messageToSend.getBytes()));
            LOGGER.debug(String
                    .format("sent to client:\n %s", messageToSend));
            key.interestOps(SelectionKey.OP_READ);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    /**
     * Reads data stored in internal buffer.
     *
     * @return String representation of data received. Data sent to the server
     * should be a sequence of characters convertible into String.
     */
    private String read() throws IOException {
        ByteBuffer readBuffer = ByteBuffer.allocate(BUFF_SIZE);
        readBuffer.clear();
        int read = socketChannel.read(readBuffer);
        if (read == END_OF_STREAM) {
            LOGGER.info(ProtocolMessages.NOTHING_TO_READ.toString());
            return null;
        }
        readBuffer.flip();
        byte[] data = new byte[BUFF_SIZE];
        readBuffer.get(data, 0, read);
        return new String(data).replaceAll("\0", "");
    }

    private void closeConnection() {
        try {
            LOGGER.info(String.format("Connection closed with %s",
                    socketChannel.getRemoteAddress().toString()));
            socketChannel.close();
            LISTENER_MANAGER.clearAllListeners();
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
    }
}
