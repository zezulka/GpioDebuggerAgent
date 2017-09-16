package request.write;

import request.StringConstants;

import request.manager.I2cManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.Request;

public final class I2cWriteRequest implements Request {

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
    public void performRequest() {
        LOGGER.info(String.format("I2c write request slave %x, length %d",
                slaveAddress, content.length));
        i2cManager.writeIntoI2c(this.slaveAddress, this.content);
    }

    @Override
    public String getFormattedResponse() {
        return String.format(StringConstants.I2C_WRITE_RESPONSE_FORMAT);
    }

}
