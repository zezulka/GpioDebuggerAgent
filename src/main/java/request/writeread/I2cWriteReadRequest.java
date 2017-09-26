package request.writeread;

import request.read.I2cReadRequest;
import request.write.I2cWriteRequest;

public class I2cWriteReadRequest extends AbstractWriteReadRequest {

    public I2cWriteReadRequest(I2cWriteRequest write, I2cReadRequest read) {
        super(write, read);
    }
}
