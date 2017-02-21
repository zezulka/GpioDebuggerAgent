package net;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public enum ProtocolMessages {
    START("Server is running..."), 
    C_CONNECTION_OK("Connection to server OK"),
    C_SERVER_READY("The system is ready to accept requests."),
    C_RESPONSE_WAIT("Waiting for server to response..."),
    
    S_CANNOT_CONNECT_TO_CLIENT("Cannot connect to client."),
    S_SERVER_REQUEST_WAIT("System is now waiting to receive a message from client."),
    
    OTHER_APPROPRIATE_MESSAGES("some text here");
    
    private final String msg;
    
    ProtocolMessages(String msg) {
        this.msg = msg;
    }
    
    public String getMessage() {
        return this.msg;
    }
}
