package request.read;

import net.ProtocolManager;
import request.manager.I2cManager;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class I2cReadRequest implements ReadRequest {
    private static I2cManager MANAGER;

    private int register;
    private int len;

    private I2cReadRequest(int slaveAddress, int registerAddress, int len) {
        if(registerAddress < 0x00) {
            throw new IllegalArgumentException("register address must be positive");
        }
        if(len <= 0) {
            throw new IllegalArgumentException("len must be a positive number");
        }
        this.len = len;
        this.register = registerAddress;

        MANAGER = I2cManager.fromAddress(slaveAddress);
    }

    public static I2cReadRequest getSingleRegisterInstance(int slaveAddress, int registerAddress) {
        return I2cReadRequest.getInstance(slaveAddress, registerAddress, 1);
    }

    public static I2cReadRequest getInstance(int slaveAddress, int registerAddress, int len) {
        return new I2cReadRequest(slaveAddress, registerAddress, len);
    }

    @Override
    public String read() {
        return MANAGER.readFromI2c(this.register, this.len);
    }

    @Override
    public void giveFeedbackToClient() {
        ProtocolManager.getInstance().setMessageToSend("I2C interface read"
                + " response:\n" + read() + '\n');
    }

}
