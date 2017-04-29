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
    private static final int READ_BUFFER_SIZE = 64;

    private I2cManager(int address) {
        i2cConnection = DeviceManager.getI2c().createI2cConnection(address);
    }

    public static I2cManager fromAddress(int address) {
        LOGGER.info(String.format("I2c read request, the selected slave address is %x.", address));
        return new I2cManager(address);
    }

    public void writeIntoI2c(String msg) {
        if (i2cConnection == null) {
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
     * Reads bytes from i2c interface. Should any error occur during the read
     * operation, special value {@code StringConstants.ERROR_RESPONSE} is
     * returned.
     *
     * @return I2c contents.
     */
    public String readFromI2c() {
        byte[] buff = new byte[READ_BUFFER_SIZE];
        try {
            i2cConnection.getBus().readBytes(buff);
        } catch(IOException ex) {
            LOGGER.error(null, ex);
        }

        StringBuilder response = new StringBuilder();
        for (int i = 0; i < READ_BUFFER_SIZE; i++) {
            if (i % 4 == 0) {
                response.append("\n");
            }
            response.append(Integer.toHexString(buff[i]));
            response.append('\t');
        }
        return response.toString();
    }

    @Override
    public String read(String len) throws IllegalRequestException {
        return readFromI2c(); //str argument needs to be transformed into integer.
    }

    @Override
    public void write(String deviceName, String message) throws IllegalRequestException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
