package net;

import core.Agent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsibilities: manage all the connections binded to the device. This is
 * the most abstract manager representing the whole application running on the
 * board device.
 *
 * @author Miloslav Zezulka, 2017
 */
public class ConnectionManager implements Runnable {

    //private static ServerSocket servSock;
    //private static Socket sock;
    //private static BufferedReader input;
    //private static PrintWriter output;
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private Map<SocketChannel, byte[]> dataTracking = new HashMap<>();

    public static final int DEFAULT_SOCK_PORT = 1024;
    public static final long TIMEOUT = 10 * 1000;
    public String msg;

    private static final ConnectionManager INSTANCE = new ConnectionManager();

    private final Logger managerLogger = LoggerFactory.getLogger(ConnectionManager.class);

    private ConnectionManager(/*int port*/) {
        initDefaultPort();
//        try {
//            servSock = new ServerSocket(port);
//            managerLogger.info(ProtocolMessages.S_START.toString());
//        } catch (IOException ex) {
//            managerLogger.error(ProtocolMessages.S_SERV_SOCK_ERR.toString(), ex);
//        }
    }

    private ConnectionManager(int port) {
        init(port);
    }

    /**
     * Initializes server. This method must be executed only once (i.e.
     * resources must be initialized only once). Should the method be executed
     * more than once, such attempt is ignored.
     */
    private void init(int port) {
        if (selector != null || serverChannel != null) {
            managerLogger.debug("Init method called more than once!");
            return;
        }
        managerLogger.info("Initializing server...");
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port));

            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException ex) {
            managerLogger.error("I/O exception", ex);
        }
    }

    private void initDefaultPort() {
        init(DEFAULT_SOCK_PORT);
    }

    public static ConnectionManager getManagerWithDefaultPort() {
        return INSTANCE;
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

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                //wait for events to happen...
                selector.select(TIMEOUT);

                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isAcceptable()) {
                        accept(key);
                        managerLogger.info("Now accepting connection.");
                    }
                    
                    if(this.msg != null && key.isConnectable()) {
                        fetchMessageToBuffer(key);
                    }

                    if (key.isWritable()) {
                        managerLogger.info("Key writable, performing write operation...");
                        write(key);
                    }
                    if (key.isReadable()) {
                        managerLogger.info("Reading incoming connection...");
                        read(key);
                    }
                }
            }
        } catch (IOException ex) {
            managerLogger.error("I/O exception: ", ex);
        } finally {
            closeConnection();
        }
    }

    /**
     * Enables server to accept incoming connections. Accept method also stores
     * initial message into map which contains name of the device.
     *
     * @param key
     * @throws IOException
     */
    private void accept(SelectionKey key) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);

            socketChannel.register(selector, SelectionKey.OP_WRITE);
            byte[] hello = Agent.BOARD.getName().getBytes();
            dataTracking.put(socketChannel, hello);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param msg message to write to buffer
     * @throws IllegalArgumentException if msg is null
     */
    public void setMessage(String msg) {
        if(msg == null) {
            throw new IllegalArgumentException("msg cannot be null");
        }
        this.msg = msg;
    }
    
    private void setMessageNull() {
        this.msg = null;
    }
    
    private void fetchMessageToBuffer(SelectionKey key) throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_WRITE);
        
        dataTracking.put(socketChannel, this.msg.getBytes());
        setMessageNull();
    }

    private void write(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        byte[] data = dataTracking.get(channel);
        dataTracking.remove(channel);

        try {
            channel.write(ByteBuffer.wrap(data));
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        key.interestOps(SelectionKey.OP_READ);
    }

    private String read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        readBuffer.clear();
        int read;
        try {
            read = channel.read(readBuffer);
            if (read == -1) {
                managerLogger.info("Read request WARNING: there is nothing to read, closing connection...");
                channel.close();
                key.cancel();
                return null;
            }
        } catch (IOException ex) {
            managerLogger.error("There has been a problem serving a read request, closing connection...", ex);
            key.cancel();
            channel.close();
            return null;
        }
        readBuffer.flip();
        byte[] data = new byte[1024]; //before : new byte[1000]
        readBuffer.get(data, 0, read);
        return new String(data);
    }

    private void closeConnection() {
        managerLogger.info(ProtocolMessages.S_FINISHED.toString());
        if (selector != null) {
            try {
                selector.close();
                serverChannel.socket().close();
                serverChannel.close();
            } catch (IOException ex) {
                managerLogger.error("I/O error", ex);
            }
        }
    }

    /*public static void writeToOutput(String msg) throws IOException {
        if (isConnectionClosed() || output == null) {
            throw new IOException(ProtocolMessages.S_CANNOT_CONNECT_TO_CLIENT.toString());
        }
        output.println(msg);
        output.flush();
    }

    public static String readFromInput() throws IOException {
        if (isConnectionClosed() || input == null) {
            throw new IOException(ProtocolMessages.S_CANNOT_CONNECT_TO_CLIENT.toString());
        }
        return input.readLine();
    }

    public static boolean isConnectionClosed() {
        return sock.isClosed();
    }

    public static boolean isConnectionReady() throws IOException {
        return sock.getInputStream().available() > 0;
    }

    public static void closeConnection() throws IOException {
        if (!servSock.isClosed()) {
            servSock.close();
        }
    }*/
    /**
     * Initializes network resources: {@code java.lang.Socket} the server is
     * listening to, server input {@code java.io.BufferedReader} and server
     * output {@code java.io.PrintStream}.
     *
     * @throws java.io.IOException I/O error occurs
     */
    /*public void initResources() throws IOException {
        sock = servSock.accept();
        ConnectionManager.input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        ConnectionManager.output = new PrintWriter(sock.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            initResources();
            while (!isConnectionReady()) {
                Thread.sleep(2500);
            }
            (new ConnectionThread()).run();
            closeConnection();
        } catch (IOException ex) {
            managerLogger.error("I/O error", ex);
        } catch (InterruptedException ex) {
            managerLogger.error("thread has been interrupted: ", ex);
        }
        managerLogger.info(ProtocolMessages.S_FINISHED.toString());
    }

    public synchronized void stop() {
        try {
            if (!servSock.isClosed()) {
                servSock.close();
            }
        } catch (IOException ex) {
            managerLogger.info(ProtocolMessages.S_STOP_ERR.toString());
        }
    }*/
}
