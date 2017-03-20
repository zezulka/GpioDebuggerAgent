package net;

import core.Agent;
import java.io.IOException;
import java.net.InetSocketAddress;
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

    public static final int DEFAULT_SOCK_PORT = 8088;
    public static final long TIMEOUT = 10 * 1000;

    private static final AgentConnectionManager INSTANCE = new AgentConnectionManager();
    private final Logger connectionManagerLogger = LoggerFactory.getLogger(AgentConnectionManager.class);

    private AgentConnectionManager() {
        initDefaultPort();
    }

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
                        int readVal = ProtocolManager.read(key);
                        if(readVal == -1) {
                            closeConnection();
                        }
                        if (ProtocolManager.getReceivedMessage() == null) {
                            continue;
                        }
                        ProtocolManager.processRequest();
                    }
                    if (key.isWritable() && ProtocolManager.getMessageToSend() != null) {
                        ProtocolManager.write(key);
                    }
                    keys.remove();
                }
            }
        } catch (IOException ex) {
            connectionManagerLogger.error(ProtocolMessages.S_IO_EXCEPTION.toString(), ex);
        } catch (IllegalRequestException ex) {
            connectionManagerLogger.error(ProtocolMessages.S_INVALID_REQUEST.toString(), ex);
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
        ProtocolManager.setMessageToSend(Agent.BOARD.getName());
        socketChannel.register(selector, SelectionKey.OP_WRITE);
    }

    private void closeConnection() {
        connectionManagerLogger.info(ProtocolMessages.S_FINISHED.toString());
        if (selector != null) {
            try {
                selector.close();
                serverSocketChannel.socket().close();
                serverSocketChannel.close();
            } catch (IOException ex) {
                connectionManagerLogger.error(ProtocolMessages.S_IO_EXCEPTION.toString(), ex);
            }
        }
    }
}
