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

    private static final I2cManager MANAGER = I2cManager.fromDefaultAddress();
    private final String content;
    
    public I2cWriteRequest(String content) {
        if(content == null) {
            throw new IllegalArgumentException("content cannot be null");
        }
        this.content = content;
    }

    @Override
    public void write() {
        MANAGER.writeIntoI2c(this.content);
    }

    @Override
    public void giveFeedbackToClient() {
        ProtocolManager.getInstance().setMessageToSend("Write I2c request has been"
                + "submitted; result="+ MANAGER.readFromI2c());
    }

    
}
