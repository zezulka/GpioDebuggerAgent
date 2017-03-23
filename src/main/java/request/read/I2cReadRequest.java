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
    private static final I2cManager MANAGER = I2cManager.getInstance();
    
    private I2cReadRequest() {
    }
    
    public static I2cReadRequest getInstance() {
        return INSTANCE;
    }

    @Override
    public String read() {
        return MANAGER.readFromI2c();
    }

    @Override
    public void giveFeedbackToClient() {
        ProtocolManager.getInstance().setMessageToSend("I2C interface sent "
                + "the following value:" + read());
    }
    
}
