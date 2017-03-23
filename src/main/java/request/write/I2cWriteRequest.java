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

    private static final I2cManager MANAGER = I2cManager.getInstance();
    private final String content;
    
    private I2cWriteRequest(String content) {
        if(content == null) {
            throw new IllegalArgumentException("content cannot be null");
        }
        this.content = content;
    }
    
    public static I2cWriteRequest getInstance(String content) {
        return new I2cWriteRequest(content);
    }

    @Override
    public void write() {
        MANAGER.writeIntoI2c(this.content);
    }

    @Override
    public void giveFeedbackToClient() {
        ProtocolManager.getInstance().setMessageToSend("Write I2c request has been"
                + "submitted; result="+I2cManager.getInstance().readFromI2c());
    }

    
}
