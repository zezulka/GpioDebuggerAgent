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
    
    ConnectionThread() {
    }
    
    @Override
    public void run() {
        try {
            sendMessageName();
            Request request;
            while(!ConnectionManager.isConnectionClosed()) {
                if((request = receiveRequest()) != null) {
                    handleRequest(request);
                    request.giveFeedbackToClient();
                } else {
                    Logger.getAnonymousLogger().log(Level.SEVERE, ProtocolMessages.S_CONNECTION_LOST_CLIENT.toString());
                    break;
                }
            }
        } catch (IOException ex) {
           Logger.getAnonymousLogger().log(Level.SEVERE, ProtocolMessages.S_CANNOT_CONNECT_TO_CLIENT.toString(), ex);
        } catch (IllegalRequestException ex) {
           Logger.getAnonymousLogger().log(Level.SEVERE, ProtocolMessages.S_INVALID_REQUEST.toString(), ex);
        }
    }

    private Request receiveRequest() throws IOException, IllegalRequestException {
        String line = ConnectionManager.readFromInput();
        if(line == null && ConnectionManager.isConnectionClosed()) {
            ConnectionManager.closeConnection();
        }
        return RequestParser.parse(line);
    }
    
    /**
     * Initial message sent to client.
     * @param msg message representing the device (name of the device).
     * @throws IOException 
     */
    private void sendMessageName() throws IOException {
        ConnectionManager.writeToOutput(Agent.BOARD.getName());
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
    }

    
}
