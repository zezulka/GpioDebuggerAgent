package request.write;

import net.ProtocolManager;

import request.manager.I2cManager;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class I2cWriteRequest implements WriteRequest {

    private final I2cManager i2cManager;
    private final byte[] content;
    private final int registerAddress;
    private final int slaveAddress;

    public I2cWriteRequest(I2cManager i2cManager, int slaveAddress, int registerAddress, byte[] content) {
        if(content == null || content.length < 1) {
            throw new IllegalArgumentException("content must be a nonempty byte array");
        }
        if(slaveAddress < 0x00) {
            throw new IllegalArgumentException("slave address must be an positive integer");
        }
        if(registerAddress < 0x00) {
            throw new IllegalArgumentException("register address must be an positive integer");
        }
        this.content = content;
        this.slaveAddress = slaveAddress;
        this.registerAddress = registerAddress;
        this.i2cManager = i2cManager;
    }

    @Override
    public void write() {
        i2cManager.writeIntoI2c(this.slaveAddress, this.registerAddress, this.content);
    }

    @Override
    public void giveFeedbackToClient() {
        ProtocolManager.getInstance().setMessageToSend("Write I2c request has been"
                + " submitted, result:\n"+ i2cManager.readFromI2c(this.slaveAddress, this.registerAddress, content.length));
    }


}
