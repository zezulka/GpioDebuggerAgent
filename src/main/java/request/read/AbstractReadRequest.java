package request.read;

import request.Request;

public abstract class AbstractReadRequest implements Request {

    @Override
    public final void performRequest() {
        // NO-OP since this type of request only returns formatted response
        // and should do nothing else
    }

}
