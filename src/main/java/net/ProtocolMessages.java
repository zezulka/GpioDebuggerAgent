package net;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public enum ProtocolMessages {
      S_SERVER_INIT("Initializing server..."),
      S_SERVER_INIT_SUCCESS("...initialization successful."),
      S_CONNECTION_ACCEPT("Now accepting connection."), 
      S_FINISHED("Server has been shut down."),
      S_CLIENT_FEEDBACK("Sending information back to client..."),
      S_ERR_INIT_MORE_THAN_ONCE("Init method called more than once!"),
      S_NOTHING_TO_READ("Read request WARNING: there is nothing to read, closing connection..."),
      S_IO_EXCEPTION("I/O exception"),
      S_REQUEST_OK("Request successfully processed"),
      S_REQUEST_CAPTURED("Captured request is about to get processed"),
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
