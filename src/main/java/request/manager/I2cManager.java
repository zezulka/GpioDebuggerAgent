/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request.manager;

import core.DeviceManager;
import java.io.IOException;
import net.ProtocolMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author miloslav
 */
public class I2cManager {
    
    private static final I2cManager INSTANCE = new I2cManager();
    private static final DeviceManager DEVICE_MANAGER = DeviceManager.getInstance();
    private static final Logger LOGGER = LoggerFactory.getLogger(I2cManager.class);
    
    private I2cManager() {}
    
    public static I2cManager getInstance() {
        return INSTANCE;
    }
    
    public void writeIntoI2c(String msg) {
        try {
            DEVICE_MANAGER.getI2c().writeString(msg);
        } catch (IOException ex) {
            LOGGER.error(ProtocolMessages.S_IO_EXCEPTION.toString(), ex);
        }
    }
    
    public String readFromI2c() {
        try {
            return DEVICE_MANAGER.getI2c().readString();
        } catch (IOException ex) {
            LOGGER.error(ProtocolMessages.S_IO_EXCEPTION.toString(), ex);
            return null;
        }
    }
}
