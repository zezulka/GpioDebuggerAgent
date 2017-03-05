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
 * Each thread is created when client connects to the server, therefore all 
 * application logic (from client's view) is performed through this entity.
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
        if(ConnectionManager.getOutput() == null) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Connection has not been established properly with client");
            throw new IOException("output stream is closed");
        }
        ConnectionManager.getOutput().println(msg);
    }
    
    /**
     * Initial message sent to client.
     * @param msg message representing the device (name of the device).
     * @throws IOException 
     */
    private void sendMessageName() throws IOException {
        this.sendMessage(Agent.BOARD.getName());
    }

    /**
     * This method takes care of determining which kind of request was sent
     * to server and then calls the appropriate method in the given interface (please
     * consult {@code request.read.ReadRequest} and {@code request.write.WriteRequest} for more information.
     * @param request 
     */
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
