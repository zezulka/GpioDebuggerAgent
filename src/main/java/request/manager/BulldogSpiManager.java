package request.manager;

import board.manager.BoardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.silverspoon.bulldog.core.io.bus.spi.SpiConnection;
import io.silverspoon.bulldog.core.io.bus.spi.SpiMessage;

import java.io.IOException;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public final class BulldogSpiManager implements SpiManager {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(SpiManager.class);
    private SpiConnection spiConnection;
    private final BoardManager boardManager;
    private static final int MAX_SLAVE_INDEX = 2;

    private BulldogSpiManager(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    public static BulldogSpiManager getInstance(BoardManager boardManager) {
        return new BulldogSpiManager(boardManager);
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
        if (spiConnection == null) {
            spiConnection = boardManager.getSpi().createSpiConnection(index);
        } else if (spiConnection.getAddress() != index) {
            try {
                spiConnection.getBus().close();
                spiConnection
                        = boardManager.getSpi().createSpiConnection(index);
            } catch (IOException ex) {
                LOGGER.error(null, ex);
            }
        }
        LOGGER.info(String
                .format("created SpiConnection with slave %d", index));
    }

}
