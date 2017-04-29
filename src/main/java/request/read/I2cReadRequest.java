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
    private static final I2cReadRequest INSTANCE = new I2cReadRequest();
    private static final I2cManager MANAGER = I2cManager.fromDefaultAddress();

    private int register;
    private boolean isRegisterSpecific = false;

    private I2cReadRequest() {
    }

    private I2cReadRequest(int registerAddress) {
        if(registerAddress < 0x00) {
            throw new IllegalArgumentException("register address must be positive");
        }
        this.register = registerAddress;
        this.isRegisterSpecific = true;
    }

    public static I2cReadRequest getRegisterSpecificInstance(int registerAddress) {
        return new I2cReadRequest(registerAddress);
    }

    public static I2cReadRequest getInstance() {
        return INSTANCE;
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
