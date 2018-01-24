package protocol.request.writeread;

import protocol.request.Request;
import protocol.request.read.ReadRequest;
import protocol.request.write.WriteRequest;

/**
 * Abstract implementation of sequential write and read request. This is useful
 * for interfaces which usually return some kind of response after write
 * request.
 */
public abstract class AbstractWriteReadRequest implements Request {

    private final WriteRequest write;
    private final ReadRequest read;

    AbstractWriteReadRequest(WriteRequest write, ReadRequest read) {
        this.write = write;
        this.read = read;
    }

    @Override
    public final String getFormattedResponse() {
        return read.getFormattedResponse();
    }

    @Override
    public final void action() {
        write.action();
    }

}
