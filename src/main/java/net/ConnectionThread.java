package net;

import arm_agent.Agent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            while(sock.isConnected()) {
                this.input = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
                this.output = new PrintStream(this.sock.getOutputStream());
                receiveRequest();
                sendMessage();
            }
        } catch (IOException ex) {
            System.err.println("Cannot connect to client");
        }
    }

    private void receiveRequest() throws IOException {
        Stream<String> in = input.lines();
        if(in.count() > 1) {
            return;
        }
        System.out.println("Received message: \n" + in.collect(Collectors.toList()));
    }
    
    private void sendMessage() throws IOException {
        this.output.println(Agent.BOARD.toString() + ": MSG OK.");
        this.output.flush();
    }

    
}
