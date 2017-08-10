package request.write;

import net.ConnectionManager;
import request.StringConstants;

import request.manager.I2cManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class I2cWriteRequest implements WriteRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(I2cWriteRequest.class);

    private final I2cManager i2cManager;
    private final byte[] content;
    private final int slaveAddress;

    public I2cWriteRequest(I2cManager i2cManager, int slaveAddress, byte[] content) {
        if(content == null || content.length < 1) {
            throw new IllegalArgumentException("content must be a nonempty byte array");
        }
        if(slaveAddress < 0x00) {
            throw new IllegalArgumentException("slave address must be a positive integer");
        }
        this.content = content;
        this.slaveAddress = slaveAddress;
        this.i2cManager = i2cManager;
    }

    @Override
    public void write() {
        LOGGER.info(String.format("I2c write request from slave %x and of length %d", slaveAddress, content.length));
        i2cManager.writeIntoI2c(this.slaveAddress, this.content);
    }

    @Override
    public void giveFeedbackToClient() {
        ConnectionManager.setMessageToSend(String.format(StringConstants.I2C_WRITE_RESPONSE_FORMAT.toString()));
    }


}
