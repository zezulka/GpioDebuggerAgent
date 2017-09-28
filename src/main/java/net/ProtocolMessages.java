package net;

public enum ProtocolMessages {
    SERVER_INIT("Initializing server..."),
    SERVER_INIT_SUCCESS("...initialization successful."),
    CONNECTION_ACCEPT("Now accepting connection."),
    ERR_INIT_MORE_THAN_ONCE("Init method called more than once!"),
    NOTHING_TO_READ("Reached end of the stream, closing connection..."),
    IO_EXCEPTION("I/O exception"),
    REQUEST_OK("Request successfully processed"),
    REQUEST_CAPTURED("Captured request is about to get processed"),
    ILLEGAL_REQUEST("Illegal request");

    private final String msg;

    ProtocolMessages(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return this.msg;
    }
}
