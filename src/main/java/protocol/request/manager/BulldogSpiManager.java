package protocol.request.manager;

import board.test.BoardManager;
import io.silverspoon.bulldog.core.io.bus.spi.SpiConnection;
import io.silverspoon.bulldog.core.io.bus.spi.SpiMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class BulldogSpiManager implements SpiManager {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(SpiManager.class);
    private SpiConnection spiConnection;
    private final BoardManager manager;
    private static final int MAX_SLAVE_INDEX = 2;

    public BulldogSpiManager(BoardManager boardManager) {
        this.manager = boardManager;
    }

    @Override
    public byte[] readFromSpi(int slaveIndex, byte[] rBuffer) {
        LOGGER.debug("Reading from SPI...");
        SpiMessage message;
        try {
            createConnectionIfNecessary(slaveIndex);
            message = spiConnection.transfer(rBuffer);
            LOGGER.debug("reading finished.");
            return message.getReceivedBytes();

        } catch (IOException ex) {
            LOGGER.error("reading failed.", ex);
            return null;
        }
    }

    @Override
    public void writeIntoSpi(int slaveIndex, byte[] tBuffer) {
        try {
            createConnectionIfNecessary(slaveIndex);
            spiConnection.transfer(tBuffer);
        } catch (IOException ex) {
            LOGGER.error(null, ex);
        }
    }

    private void createConnectionIfNecessary(int index) {
        if (index < 0 || index > MAX_SLAVE_INDEX) {
            throw new IllegalArgumentException(String
                    .format("slave index out of bounds (=%d)", index));
        }
        spiConnection = manager.getSpi().createSpiConnection(index);

        LOGGER.info(String
                .format("created SpiConnection with slave %d", index));
    }

}
