package net;

import core.Agent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class ConnectionThread implements Runnable {

    private final Socket sock;
    private BufferedReader input;
    private PrintWriter output;
    
    ConnectionThread(Socket sock) {
        this.sock = sock;
    }
    
    @Override
    public void run() {
        try {
            this.input = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
            this.output = new PrintWriter(this.sock.getOutputStream(), true);
            while(true) {
                if(receiveRequest()) {
                    sendMessage();
                } else {
                    System.err.println(ProtocolMessages.S_CONNECTION_LOST_CLIENT.getMessage());
                    break;
                }
            }
        } catch (IOException ex) {
            System.err.println(ProtocolMessages.S_CANNOT_CONNECT_TO_CLIENT.getMessage() + ex);
        }
    }

    private boolean receiveRequest() throws IOException {
        System.out.println(ProtocolMessages.S_SERVER_REQUEST_WAIT.getMessage());
        String line;
        line = input.readLine();
        System.out.println("\nReceived message:" + line);
        return line != null;
    }
    
    private void sendMessage() throws IOException {
        this.output.println(Agent.BOARD.toString() + ": MSG OK.");
    }

    
}
