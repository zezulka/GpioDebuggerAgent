package net;

import core.Agent;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import request.IllegalRequestException;
import request.Request;
import request.RequestParser;
import request.read.ReadRequest;
import request.write.WriteRequest;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class ConnectionThread implements Runnable {

    private final Socket sock;
    
    ConnectionThread(Socket sock) {
        this.sock = sock;
    }
    
    @Override
    public void run() {
        try {
            sendMessageName();
            Request request;
            while(!sock.isClosed()) {
                if((request = receiveRequest()) != null) {
                    handleRequest(request);
                    sendMessage("Server response: OK");
                } else {
                    System.err.println(ProtocolMessages.S_CONNECTION_LOST_CLIENT);
                    break;
                }
            }
        } catch (IOException ex) {
            System.err.println(ProtocolMessages.S_CANNOT_CONNECT_TO_CLIENT.toString() + ex);
        } catch (IllegalRequestException ex) {
            System.err.println(ProtocolMessages.S_INVALID_REQUEST);
        }
    }

    private Request receiveRequest() throws IOException, IllegalRequestException {
        System.out.println(ProtocolMessages.S_SERVER_REQUEST_WAIT);
        String line;
        line = ConnectionManager.getInput().readLine();
        if(line == null) {
            sock.close();
            return null;
        }
        return RequestParser.parse(line);
    }
    
    private void sendMessage(String msg) throws IOException {
        ConnectionManager.getOutput().println(msg);
    }
    
    private void sendMessageName() throws IOException {
        this.sendMessage(Agent.BOARD.getName());
    }

    private void handleRequest(Request request) {
        if(request instanceof ReadRequest) {
            ReadRequest req = (ReadRequest) request;
            req.read();
            
        } else if(request instanceof WriteRequest) {
            WriteRequest req = (WriteRequest) request;
            req.write();
        }
        request.giveFeedbackToClient();
    }

    
}
