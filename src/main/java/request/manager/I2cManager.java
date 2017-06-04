package request.manager;

import core.DeviceManager;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cConnection;
import io.silverspoon.bulldog.linux.jni.NativeI2c;
import java.io.IOException;
import net.ProtocolMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.IllegalRequestException;
import request.StringConstants;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class I2cManager implements InterfaceManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(I2cManager.class);
    private static I2cConnection i2cConnection;

    private I2cManager(int address) {
        i2cConnection = DeviceManager.getI2c().createI2cConnection(address);
    }

    public static I2cManager fromAddress(int address) {
        return new I2cManager(address);
    }

    public String readFromI2cRegister(int i) {
        return readFromI2c(i, 1);
    }

    /**
     * Reads bytes from i2c interface.
     *
     * @return I2c contents.
     */
    public String readFromI2c(int pos, int len) {
        LOGGER.info(String.format("I2c read request from position %x and of length %d", pos, len));
        try {
            if(len == 1) {
              byte b = i2cConnection.readByteFromRegister(pos);
              return b + " 0x" + Integer.toString((byte)b, 16);
            } else if(len > 1){
              byte[] buff = new byte[len];
              i2cConnection.readBytesFromRegister(pos, buff);
              StringBuilder builder = new StringBuilder();
              for(byte b : buff) {
                  builder = builder.append(b).append("\t0x").append(Integer.toString((byte)b, 16)).append('\n');
              }
              return builder.toString();
            }
        } catch(IOException ex) {
            LOGGER.error(null, ex);
        }
        return "";
    }

    /*
     * Reads bytes from the given address. I2c connection must have
     * been established before calling this method.
     */
    public void writeIntoI2c(int address, byte[] message) {
        if (i2cConnection == null) {
            LOGGER.error("Client attempted to access i2c bus while there was no i2c connection established!");
            throw new IllegalStateException("Client attempted to access i2c bus while there was no i2c connection established!");
        }
        if(message == null || message.length == 0) {
            LOGGER.error("Client attempted to write no data, ignoring...");
            return;
        }
        try {
            LOGGER.info(String.format("I2c write request from position %x and of length %d", address, message.length));
            if(message.length == 1) {
                i2cConnection.writeByteToRegister(address, message[0]);
            } else {
                i2cConnection.writeBytesToRegister(address, message);
            }
        } catch (IOException ex) {
            LOGGER.error(ProtocolMessages.S_IO_EXCEPTION.toString(), ex);
        }
    }
}
