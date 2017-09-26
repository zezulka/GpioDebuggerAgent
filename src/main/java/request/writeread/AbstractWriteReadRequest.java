package request.writeread;

import request.Request;
import request.read.ReadRequest;
import request.write.WriteRequest;

/**
 * Abstract implementation of sequential write and read request. This is useful
 * for interfaces which usually return some kind of response after write
 * request.
 */
public abstract class AbstractWriteReadRequest implements Request {

    private final WriteRequest write;
    private final ReadRequest read;

    public AbstractWriteReadRequest(WriteRequest write, ReadRequest read) {
        this.write = write;
        this.read = read;
    }

    @Override
    public String getFormattedResponse() {
        return read.getFormattedResponse();
    }

    @Override
    public void performRequest() {
        write.performRequest();
    }

}
