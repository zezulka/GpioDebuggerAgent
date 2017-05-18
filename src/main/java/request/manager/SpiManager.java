package request.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.silverspoon.bulldog.core.io.bus.spi.SpiConnection;
import io.silverspoon.bulldog.core.io.bus.spi.SpiMessage; 
import core.DeviceManager;
import java.io.IOException;
import request.StringConstants;
/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class SpiManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpiManager.class);
    private SpiConnection spiConnection;
    private static final int MAX_SLAVE_INDEX = 2;

    private SpiManager(int slaveIndex) {
        if(slaveIndex < 0 || slaveIndex > MAX_SLAVE_INDEX) {
            throw new IllegalArgumentException(String.format("slave index out of bounds (=%d)", slaveIndex));
        }
        this.spiConnection = DeviceManager.getSpi().createSpiConnection(slaveIndex);
    }

    public static SpiManager fromIndex(int slaveIndex) {
        return new SpiManager(slaveIndex);
    }

    public String readFromSpi(byte[] rBuffer) {
        SpiMessage message;
        try {
            message = spiConnection.transfer(rBuffer);
        } catch(IOException ex) {
            LOGGER.error(null, ex);
            return StringConstants.ERROR_RESPONSE.toString();
        }
        return new String(message.getReceivedBytes());
    }

    public void writeIntoSpi(byte[] tBuffer) {
        try{
            spiConnection.transfer(tBuffer);
        } catch(IOException ex) {
            LOGGER.error(null, ex);
        }

    }

}
