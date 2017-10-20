package protocol.request.writeread;

import protocol.request.read.SpiReadRequest;
import protocol.request.write.SpiWriteRequest;

public class SpiWriteReadRequest extends AbstractWriteReadRequest {

    public SpiWriteReadRequest(SpiWriteRequest write, SpiReadRequest read) {
        super(write, read);
    }
}
