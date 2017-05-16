package request.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.silverspoon.bulldog.core.io.bus.spi.SpiConnection;
/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class SpiManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpiManager.class);
    private SpiConnection spiConnection;

    private SpiManager(int slaveIndex) {
        this.spiConnection = DeviceManager.getSpi().createConnection(slaveIndex);
    }


    public String readFromSpi(byte[] rBuffer) {
        return new String(spiConnection.transfer(rBuffer).getReceivedBytes(c));
    }

    public void writeIntoSpi(byte[] tBuffer) {
        spiConnecion.transfer(tBuffer);
    }

}
