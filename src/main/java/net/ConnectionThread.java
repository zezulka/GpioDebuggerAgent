package net;

import arm_agent.Agent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class ConnectionThread implements Runnable {

    private final Socket sock;
    private BufferedReader input;
    private PrintStream output;
    
    ConnectionThread(Socket sock) {
        this.sock = sock;
    }
    
    @Override
    public void run() {
        try {
            this.input = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
            this.output = new PrintStream(this.sock.getOutputStream());
            while(sock.isConnected()) {
                receiveRequest();
                sendMessage();
            }
        } catch (IOException ex) {
            System.err.println("Cannot connect to client");
        }
    }

    private void receiveRequest() throws IOException {
        String in;
        while((in = input.readLine()) != null) {
            //RequestParser.parse(/*Request object*/);
            System.out.println("This is what I received: \n");
            System.out.println(in);
        }
    }

    private void sendMessage() throws IOException {
        this.output.println(Agent.BOARD.toString() + ": MSG OK.");
        //this.output.flush();
    }

    
}
