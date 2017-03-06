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
 * Responsibilities: manage all the connections binded to the device. This
 * is the most abstract manager representing the whole application running on the 
 * board device.
 * @author Miloslav Zezulka, 2017
 */
public class ConnectionManager implements Runnable {
    private ServerSocket servSock;
    private Socket sock;
    private static BufferedReader input;
    private static PrintWriter output;
    public static final int DEFAULT_SOCK_PORT = 1024;
    private static final int CLIENTS_UPPER_BOUND = 8;
    protected ExecutorService threadPool =
        Executors.newFixedThreadPool(CLIENTS_UPPER_BOUND);
    
    private ConnectionManager(int port) {
        try {
            this.servSock = new ServerSocket(port);
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
     * @param port
     * @return 
     */
    public static ConnectionManager getManager(int port) {
        return new ConnectionManager(port);
    }
    
    public static BufferedReader getInput() {
        return ConnectionManager.input;
    }
    
    public static PrintWriter getOutput() {
        return ConnectionManager.output;
    }
    
    /**
     * Initializes network resources: {@code java.lang.Socket} the server is 
     * listening to, server input {@code java.io.BufferedReader} 
     * and server output {@code java.io.PrintStream}.
     * @throws java.io.IOException I/O error occurs
     */
    public void initResources() throws IOException {
        this.sock = this.servSock.accept();
        ConnectionManager.input = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        ConnectionManager.output = new PrintWriter(this.sock.getOutputStream(), true);
    }
    
    @Override
    public void run() {
        while(!sock.isClosed()) {
            try {
                initResources();
                Logger.getAnonymousLogger().log(Level.INFO, 
                        ProtocolMessages.S_PORT_INFO.toString(), this.sock.getLocalPort());
            } catch (IOException ex) {
                if(sock.isClosed()) {
                    break;
                }
                Logger.getAnonymousLogger().log(Level.SEVERE, ProtocolMessages.S_SOCK_ERR.toString(), ex);
            }
            threadPool.execute(new ConnectionThread(this.sock));
        }
        threadPool.shutdown();
        Logger.getAnonymousLogger().log(Level.INFO, ProtocolMessages.S_FINISHED.toString());
    }
    
    public synchronized void stop() {
        try {
            if(!servSock.isClosed()) {
                servSock.close();
            }
        } catch (IOException ex) {
            System.err.println(ProtocolMessages.S_STOP_ERR.toString() + ex);
        }
    }
}
