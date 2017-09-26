package request.writeread;

import request.read.SpiReadRequest;
import request.write.SpiWriteRequest;

public class SpiWriteReadRequest extends AbstractWriteReadRequest {

    public SpiWriteReadRequest(SpiWriteRequest write, SpiReadRequest read) {
        super(write, read);
    }
}
