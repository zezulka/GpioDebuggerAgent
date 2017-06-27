package request.read;

import net.AgentConnectionManager;
import request.StringConstants;

import request.manager.I2cManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class I2cReadRequest implements ReadRequest {

    private final I2cManager i2cManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(I2cReadRequest.class);
    private final int len;
    private final int slaveAddress;

    public I2cReadRequest(I2cManager i2cManager, int slaveAddress, int len) {
        if(len <= 0) {
            throw new IllegalArgumentException("len must be a positive number");
        }
        this.len = len;
        this.slaveAddress = slaveAddress;
        this.i2cManager = i2cManager;
    }

    @Override
    public String read() {
        LOGGER.info(String.format("I2c read request from slave %d and of length %d", this.slaveAddress, this.len));
        return i2cManager.readFromI2c(this.slaveAddress, this.len);
    }

    @Override
    public void giveFeedbackToClient() {
        AgentConnectionManager.setMessageToSend(String.format(StringConstants.I2C_READ_RESPONSE_FORMAT.toString(), read() + '\n'));
    }

}
