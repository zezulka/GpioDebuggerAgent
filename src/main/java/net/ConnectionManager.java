package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Responsibilities: manage all the connections binded to the device. This
 * is the most abstract manager representing the whole application running on the 
 * board device.
 * @author 
 */
public class ConnectionManager implements Runnable {
    private ServerSocket servSock;
    private Socket sock;
    private boolean isStopped;
    public static final int DEFAULT_SOCK_PORT = 1024;
    private static final int CLIENTS_UPPER_BOUND = 64;
    protected ExecutorService threadPool =
        Executors.newFixedThreadPool(CLIENTS_UPPER_BOUND);
    
    private ConnectionManager(int port) {
        try {
            this.servSock = new ServerSocket(port);
            System.out.println("Server is running.");
        } catch (IOException ex) {
            System.err.println("There was an error creating a server socket: " + ex);
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
    
    /**
     * Initializes network resources: {@code java.lang.Socket} the server is 
     * listening to, server input {@code java.io.BufferedReader} 
     * and server output {@code java.io.PrintStream}.
     * @throws java.io.IOException I/O error occurs
     */
    public void initResources() throws IOException {
        this.sock = this.servSock.accept();
    }
    
    public synchronized boolean isStopped() {
        return this.isStopped;
    }
    
    @Override
    public void run() {
        while(!isStopped()) {
            try {
                initResources();
                System.out.println("Listening on port: " + this.sock.getLocalPort());
            } catch (IOException ex) {
                if(this.isStopped()) {
                    break;
                }
                System.err.println("Unable to create listener socket." + ex);
            }
            threadPool.execute(new ConnectionThread(this.sock));
        }
        threadPool.shutdown();
        System.out.println("Server shutting donwn...");
    }
    
    public synchronized void stop() {
        try {
            servSock.close();
            this.isStopped = true;
        } catch (IOException ex) {
            System.err.println("Cannot stop server." + ex);
        }
    }
}
