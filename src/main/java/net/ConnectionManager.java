package net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import request.IllegalRequestException;
import request.Interface;
import request.interrupt.EpollInterruptListenerManager;
import request.interrupt.InterruptListenerManager;
import board.manager.BoardManager;
import board.manager.BoardManagerBulldogImpl;
import request.manager.BulldogPinAccessor;
import request.manager.BulldogI2cManager;
import request.manager.InterfaceManager;
import request.manager.BulldogSpiManager;

/**
 * Responsibility: manage all the connections binded to the device.
 *
 * @author Miloslav Zezulka, 2017
 */
public final class ConnectionManager implements Runnable {

    private static ServerSocketChannel serverSocketChannel;
    private static SocketChannel socketChannel;
    private static Selector selector;
    /**
     * Default socket port.
     */
    public static final int DEFAULT_SOCK_PORT = 8088;
    private static int port;
    private static String messageToSend;

    /**
     * Timeout used in connection manager thread. Generally, the time which the
     * agent waits for incoming connections for.
     */
    public static final long TIMEOUT = 10 * 1000;
    private static final int BUFF_SIZE = 1024;
    private static final int END_OF_STREAM = -1;

    private static final ConnectionManager INSTANCE = new ConnectionManager();
    private static final BoardManager BOARD_MANAGER
            = BoardManagerBulldogImpl.getInstance();
    private static final Function<Interface, InterfaceManager> CONVERTER = (t)
            -> {
        switch (t) {
            case GPIO:
                return new BulldogPinAccessor(BOARD_MANAGER);
            case I2C:
                return BulldogI2cManager.getInstance(BOARD_MANAGER);
            case SPI:
                return BulldogSpiManager.getInstance(BOARD_MANAGER);
            default:
                throw new IllegalArgumentException();
        }
    };

    private static final ProtocolManager PROTOCOL_MANAGER
            = new ProtocolManager(CONVERTER);
    private static final InterruptListenerManager LISTENER_MANAGER
            = EpollInterruptListenerManager.getInstance();
    private static final Logger LOGGER
            = LoggerFactory.getLogger(ConnectionManager.class);

    private ConnectionManager() {
        this(DEFAULT_SOCK_PORT);
    }

    public ConnectionManager(int sockPort) {
        ConnectionManager.port = sockPort;
    }

    /**
     * Prepares agent for accepting connection (on the network level). This
     * method must be executed only once per client (i.e. resources must be
     * initialised only once). Should the method be executed more than once,
     * such attempt is ignored.
     */
    private void prepareForAcceptingConnection() {
        LOGGER.info(ProtocolMessages.SERVER_INIT.toString());
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(true);
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.socket().setReuseAddress(true);
            serverSocketChannel.configureBlocking(false);
        } catch (IOException ex) {
            LOGGER.error(ProtocolMessages.IO_EXCEPTION.toString(), ex);
        }
        LOGGER.info(ProtocolMessages.SERVER_INIT_SUCCESS.toString());
    }

    private void registerAcceptOperation() {
        try {
            LOGGER.info("now accepting connection from client");
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException ex) {
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
        if (message == null) {
            throw new IllegalArgumentException("message cannot be null");
        }
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
            while (true) {
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
                setMessageToSend(ProtocolMessages.ILLEGAL_REQUEST.toString());
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
        StringBuilder builder = new StringBuilder("INIT:");
        builder.append(BOARD_MANAGER.getBoardName()).append(':');
        for (Feature f : getFeaturesAvailable()) {
            builder.append(f.name()).append(' ');
        }
        setMessageToSend(builder.toString());
        socketChannel.register(selector, SelectionKey.OP_WRITE);
    }

    private Set<Feature> getFeaturesAvailable() {
        Set<Feature> result = new HashSet<>();
        if (Util.isUserRoot()) {
            addAllFeatures(result);
        } else if (Util.isUserInGpioGroup()) {
            result.add(Feature.GPIO);
            result.add(Feature.INTERRUPTS);
        }
        return result;
    }

    private void addAllFeatures(Set<Feature> set) {
        set.addAll(Arrays.asList(Feature.values()));
    }

    private enum Feature {
        GPIO,
        INTERRUPTS,
        I2C,
        SPI;
    }

    /**
     * Performs the actual write operation. Reads data stored in internal buffer
     * and attempts to send them to the output stream.
     *
     * @param key
     */
    private void write(SelectionKey key) {
        LOGGER.info(ProtocolMessages.CLIENT_FEEDBACK.toString());
        try {
            socketChannel.write(ByteBuffer.wrap(messageToSend.getBytes()));
            LOGGER.debug(String
                    .format("sent to client %s", messageToSend));
            key.interestOps(SelectionKey.OP_READ);
        } catch (IOException ex) {
            LOGGER.info(ex.getMessage());
        }
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

    /*
     * Closes connection.
     */
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
