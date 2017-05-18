/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request.write;

import net.ProtocolManager;
import request.manager.I2cManager;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class I2cWriteRequest implements WriteRequest {

    private static I2cManager MANAGER;
    private final byte[] content;
    private int registerAddress;

    public I2cWriteRequest(int slaveAddress, int registerAddress, byte[] content) {
        if(content == null) {
            throw new IllegalArgumentException("content cannot be null");
        }
        if(slaveAddress < 0x00) {
            throw new IllegalArgumentException("slave address must be an positive integer");
        }
        if(registerAddress < 0x00) {
            throw new IllegalArgumentException("register address must be an positive integer");
        }
        this.content = content;
        this.registerAddress = registerAddress;
        MANAGER = I2cManager.fromAddress(slaveAddress);
    }

    @Override
    public void write() {
        MANAGER.writeIntoI2c(this.registerAddress, this.content);
    }

    @Override
    public void giveFeedbackToClient() {
        ProtocolManager.getInstance().setMessageToSend("Write I2c request has been"
                + " submitted, result:\n"+ MANAGER.readFromI2c(this.registerAddress, content.length));
    }


}
