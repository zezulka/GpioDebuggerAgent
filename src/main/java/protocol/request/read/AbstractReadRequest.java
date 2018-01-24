package protocol.request.read;

public abstract class AbstractReadRequest implements ReadRequest {

    @Override
    public final void action() {
        // NO-OP since this type of request only returns formatted response
        // and should do nothing else
    }
}
