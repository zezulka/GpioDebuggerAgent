package protocol.request.write;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.request.StringConstants;
import protocol.request.manager.I2cManager;

public final class I2cWriteRequest implements WriteRequest {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(I2cWriteRequest.class);

    private final I2cManager i2cManager;
    private final byte[] content;
    private final int slaveAddress;

    public I2cWriteRequest(I2cManager i2cManager, int slaveAddress,
            byte[] content) {
        if (content == null || content.length < 1) {
            throw new IllegalArgumentException("empty content");
        }
        if (slaveAddress < 0x00) {
            throw new IllegalArgumentException("not a positive integer");
        }
        this.content = content;
        this.slaveAddress = slaveAddress;
        this.i2cManager = i2cManager;
    }

    @Override
    public void action() {
        LOGGER.info(String.format("I2c write request slave %x, length %d",
                slaveAddress, content.length));
        i2cManager.writeIntoI2c(this.slaveAddress, this.content);
    }

    @Override
    public String responseString() {
        return StringConstants.I2C_WRITE_RESPONSE;
    }

}
