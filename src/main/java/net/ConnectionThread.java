package net;

import core.Agent;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class ConnectionThread implements Runnable {

    private final Socket sock;
    private InputStream input;
    private PrintWriter output;
    
    ConnectionThread(Socket sock) {
        this.sock = sock;
    }
    
    @Override
    public void run() {
        try {
            this.input = this.sock.getInputStream();
            this.output = new PrintWriter(this.sock.getOutputStream(), true);
            while(sock.isConnected()) {
                receiveRequest();
                sendMessage();
            }
        } catch (IOException ex) {
            System.err.println(ProtocolMessages.S_CANNOT_CONNECT_TO_CLIENT.getMessage() + ex);
        }
    }

    private void receiveRequest() throws IOException {
        System.out.println(ProtocolMessages.S_SERVER_REQUEST_WAIT.getMessage());
        StringBuilder request = new StringBuilder();
        int c = 0;
        while((c = this.input.read()) != '\n' && c != -1) { //VERY PRONE TO ERROR!
            request = request.append((char)c);
            System.out.printf("%d ", c);
        } 
        System.out.println("\nReceived message:" + request.toString());
    }
    
    private void sendMessage() throws IOException {
        this.output.println(Agent.BOARD.toString() + ": MSG OK.");
    }

    
}
