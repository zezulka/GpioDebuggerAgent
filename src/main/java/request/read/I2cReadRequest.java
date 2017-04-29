/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private boolean isRegisterSpecific = false;

    private I2cReadRequest(int slaveAddress) {
        if(slaveAddress < 0x00) {
           throw new IllegalArgumentException("slave address must be positive");
        }
        MANAGER = I2cManager.fromAddress(slaveAddress);
    }

    private I2cReadRequest(int slaveAddress, int registerAddress) {
        this(slaveAddress);
        if(registerAddress < 0x00) {
            throw new IllegalArgumentException("register address must be positive");
        }
        this.register = registerAddress;
        this.isRegisterSpecific = true;
    }

    public static I2cReadRequest getRegisterSpecificInstance(int slaveAddress, int registerAddress) {
        return new I2cReadRequest(slaveAddress, registerAddress);
    }

    public static I2cReadRequest getInstance(int slaveAddress) {
        return new I2cReadRequest(slaveAddress);
    }

    @Override
    public String read() {
        if(isRegisterSpecific) {
            return MANAGER.readFromI2cRegister(this.register);
        } else {
            return MANAGER.readFromI2c();
        }
    }

    @Override
    public void giveFeedbackToClient() {
        ProtocolManager.getInstance().setMessageToSend("I2C interface read"
                + " response: \n" + read());
    }

}
