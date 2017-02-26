package net;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public enum ProtocolMessages {
    S_START("Server is running..."), 
    S_CANNOT_CONNECT_TO_CLIENT("Cannot connect to client."),
    S_SERVER_REQUEST_WAIT("System is now waiting to receive a message from client."),
    S_CONNECTION_LOST_CLIENT("Connection has been lost with client.");
    
    private final String msg;
    
    ProtocolMessages(String msg) {
        this.msg = msg;
    }
    
    public String getMessage() {
        return this.msg;
    }
}
