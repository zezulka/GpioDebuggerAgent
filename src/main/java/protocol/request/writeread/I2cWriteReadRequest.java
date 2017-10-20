package protocol.request.writeread;

import protocol.request.read.I2cReadRequest;
import protocol.request.write.I2cWriteRequest;

public class I2cWriteReadRequest extends AbstractWriteReadRequest {

    public I2cWriteReadRequest(I2cWriteRequest write, I2cReadRequest read) {
        super(write, read);
    }
}
