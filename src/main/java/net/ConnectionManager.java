package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsibilities: manage all the connections binded to the device. This is
 * the most abstract manager representing the whole application running on the
 * board device.
 *
 * @author Miloslav Zezulka, 2017
 */
public class ConnectionManager implements Runnable {

    private static ServerSocket servSock;
    private static Socket sock;
    private static BufferedReader input;
    private static PrintWriter output;
    public static final int DEFAULT_SOCK_PORT = 1024;

    private ConnectionManager(int port) {
        try {
            servSock = new ServerSocket(port);
            Logger.getAnonymousLogger().log(Level.INFO, ProtocolMessages.S_START.toString());
        } catch (IOException ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, ProtocolMessages.S_SERV_SOCK_ERR.toString());
        }
    }

    public static ConnectionManager getManagerWithDefaultPort() {
        return ConnectionManager.getManager(ConnectionManager.DEFAULT_SOCK_PORT);
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

    public static void writeToOutput(String msg) throws IOException {
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
    }

    /**
     * Initializes network resources: {@code java.lang.Socket} the server is
     * listening to, server input {@code java.io.BufferedReader} and server
     * output {@code java.io.PrintStream}.
     *
     * @throws java.io.IOException I/O error occurs
     */
    public void initResources() throws IOException {
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
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Logger.getAnonymousLogger().log(Level.INFO, ProtocolMessages.S_FINISHED.toString());
    }

    public synchronized void stop() {
        try {
            if (!servSock.isClosed()) {
                servSock.close();
            }
        } catch (IOException ex) {
            System.err.println(ProtocolMessages.S_STOP_ERR.toString() + ex);
        }
    }
}
