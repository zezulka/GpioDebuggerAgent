package net;

import core.Agent;
import core.DeviceManager;
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

/**
 * Responsibilities: manage all the connections binded to the device.
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

    /**
     * Timeout used in connection manager thread. Generally, the time which the
     * agent waits for incoming connections for.
     */
    public static final long TIMEOUT = 10 * 1000;

    private static final AgentConnectionManager INSTANCE = new AgentConnectionManager();
    private final ProtocolManager protocolManager = ProtocolManager.getInstance();
    private final Logger connectionManagerLogger = LoggerFactory.getLogger(AgentConnectionManager.class);

    /**
     * Creates connection manager with default socket port.
     */
    private AgentConnectionManager() {
        initDefaultPort();
    }

    /**
     * Creates connection manager with the port given.
     *
     * @param port
     */
    private AgentConnectionManager(int port) {
        init(port);
    }

    /**
     * Initializes server. This method must be executed only once (i.e.
     * resources must be initialized only once). Should the method be executed
     * more than once, such attempt is ignored.
     */
    private void init(int port) {
        if (selector != null || serverSocketChannel != null) {
            connectionManagerLogger.error(ProtocolMessages.S_ERR_INIT_MORE_THAN_ONCE.toString());
            return;
        }
        connectionManagerLogger.info(ProtocolMessages.S_SERVER_INIT.toString());
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(true);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException ex) {
            connectionManagerLogger.error(ProtocolMessages.S_IO_EXCEPTION.toString(), ex);
        }
        connectionManagerLogger.info(ProtocolMessages.S_SERVER_INIT_SUCCESS.toString());
    }

    private void initDefaultPort() {
        init(DEFAULT_SOCK_PORT);
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

    public SocketChannel getSocketChannel() {
        return this.socketChannel;
    }

    public Selector getSelector() {
        return selector;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
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
                        connectionManagerLogger.info(ProtocolMessages.S_CONNECTION_ACCEPT.toString());
                    }
                    if (key.isReadable()) {
                        int readVal = read();
                        if (readVal == -1) {
                            closeConnection();
                        }
                        if (protocolManager.getReceivedMessage() == null) {
                            continue;
                        }
                        try {
                            protocolManager.processRequest();
                        } catch (IllegalRequestException ex) {
                            connectionManagerLogger.error(null, ex);
                            ProtocolManager.getInstance().setMessageToSend("Illegal Request.");
                        }
                    }
                    if (key.isWritable() && protocolManager.getMessageToSend() != null) {
                        write(key);
                    }
                    keys.remove();
                }
            }
        } catch (IOException ex) {
            connectionManagerLogger.error(ProtocolMessages.S_IO_EXCEPTION.toString(), ex);
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
        protocolManager.setMessageToSend(DeviceManager.getDeviceName());
        socketChannel.register(selector, SelectionKey.OP_WRITE);
    }

    /**
     * Performs the actual write operation. Reads data stored in internal buffer
     * and attempts to send them to the output stream.
     *
     * @param key
     */
    private void write(SelectionKey key) throws IOException {
        connectionManagerLogger.info(ProtocolMessages.S_CLIENT_FEEDBACK.toString());
        socketChannel.write(ByteBuffer.wrap(protocolManager.getMessageToSend().getBytes()));
        key.interestOps(SelectionKey.OP_READ);
        protocolManager.resetMessageToSend();
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
            connectionManagerLogger.info(ProtocolMessages.S_NOTHING_TO_READ.toString());
            return read;
        }
        readBuffer.flip();
        byte[] data = new byte[1024];
        readBuffer.get(data, 0, read);
        String msg = new String(data).replaceAll("\0", "");
        protocolManager.setReceivedMessage(msg);
        socketChannel.register(selector, SelectionKey.OP_WRITE);
        return 0;
    }

    /**
     * Closes connection, thus closing all resources which were binded to this
     * connection manager.
     */
    private void closeConnection() {
        connectionManagerLogger.info(ProtocolMessages.S_FINISHED.toString());
            try {
                if (selector != null) {
                    selector.close();
                    serverSocketChannel.socket().close();
                    serverSocketChannel.close();
                }
                DeviceManager.cleanUpResources();
            } catch (IOException ex) {
                connectionManagerLogger.error(ProtocolMessages.S_IO_EXCEPTION.toString(), ex);
            }
        }
}
