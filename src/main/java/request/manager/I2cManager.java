/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request.manager;

import core.DeviceManager;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cConnection;
import java.io.IOException;
import net.ProtocolMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.StringConstants;

/**
 *
 * @author miloslav
 */
public class I2cManager {
    
    private static DeviceManager deviceManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(I2cManager.class);
    private static I2cConnection i2cConnection;
    private static final int I2CADDR = 0x68;
    private static final I2cManager DEF_MANAGER = new I2cManager();
    private static final int READ_BUFFER_SIZE = 64;
    
    private I2cManager() {
        this(I2CADDR);
    }
    
    private I2cManager(int address) {
        deviceManager = new DeviceManager();
        i2cConnection = deviceManager.getI2c().createI2cConnection(address);
    }
    
    public static I2cManager fromAddress(int address) {
        return new I2cManager(address);
    }
    
    public static I2cManager fromDefaultAddress() {
        return DEF_MANAGER;
    }
    
    public void writeIntoI2c(String msg) {
        if(i2cConnection == null) {
            return;
        }
        try {
            i2cConnection.writeByteToRegister(0x00, 32);
        } catch (IOException ex) {
            LOGGER.error(ProtocolMessages.S_IO_EXCEPTION.toString(), ex);
        }
    }
    
    public String readFromI2cRegister(int i) {
        try {
            return Integer.toHexString(i2cConnection.readByteFromRegister(i));
        } catch (IOException ex) {
            LOGGER.error(null, ex);
            return StringConstants.ERROR_RESPONSE.toString();
        }
    }
    
    /**
     * Reads bytes from i2c interface. Should any error occur during the read operation,
     * special value {@code StringConstants.ERROR_RESPONSE} is returned.
     * @return I2c contents.
     */
    public String readFromI2c() {
        byte[] buff = new byte[READ_BUFFER_SIZE];
        int read;
        try {
            read = i2cConnection.readBytes(buff);
        } catch (IOException ex) {
            LOGGER.error(null, ex);
            return StringConstants.ERROR_RESPONSE.toString();
        }
        int upperBound = read > READ_BUFFER_SIZE ? READ_BUFFER_SIZE : read;
        StringBuilder response = new StringBuilder();
        for(int i = 0; i < upperBound ; i++) {
            response.append(Integer.toHexString(buff[i]));
            if(i > 0 && i % 4 == 0) {
                response.append("\n");
            } 
        }
        return response.toString();
    }
}
