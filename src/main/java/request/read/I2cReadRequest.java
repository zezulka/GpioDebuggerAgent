package request.read;

import net.AgentConnectionManager;
import request.manager.I2cManager;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class I2cReadRequest implements ReadRequest {
    private final I2cManager i2cManager;

    private final int register;
    private final int len;
    private final int slaveAddress;

    public I2cReadRequest(I2cManager i2cManager, int slaveAddress, int registerAddress, int len) {
        if(registerAddress < 0x00) {
            throw new IllegalArgumentException("register address must be positive");
        }
        if(len <= 0) {
            throw new IllegalArgumentException("len must be a positive number");
        }
        this.len = len;
        this.register = registerAddress;
        this.slaveAddress = slaveAddress;
        this.i2cManager = i2cManager;
    }

    @Override
    public String read() {
        return i2cManager.readFromI2c(this.slaveAddress, this.register, this.len);
    }

    @Override
    public void giveFeedbackToClient() {
        AgentConnectionManager.setMessageToSend("I2C interface read"
                + " response:\n" + read() + '\n');
    }

}
