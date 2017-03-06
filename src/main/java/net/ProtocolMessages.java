package net;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public enum ProtocolMessages {
    S_START("Server is running..."), 
    S_FINISHED("Server has been shut down."),
    S_SERV_SOCK_ERR("There was an error creating a server socket: "),
    S_SOCK_ERR("Unable to create listener socket."),
    S_STOP_ERR("Cannot stop server."),
    S_CANNOT_CONNECT_TO_CLIENT("Cannot connect to client."),
    S_PORT_INFO("Listening on port: "),
    S_SERVER_REQUEST_WAIT("System is now waiting to receive a message from client."),
    S_CONNECTION_LOST_CLIENT("Connection has been lost with client."),
    S_INVALID_CONNECTION_CLIENT("Connection has not been established properly with client"),
    S_INVALID_REQUEST("Invalid request");
    
    private final String msg;
    
    ProtocolMessages(String msg) {
        this.msg = msg;
    }
    
    @Override
    public String toString() {
        return this.msg;
    }
}
