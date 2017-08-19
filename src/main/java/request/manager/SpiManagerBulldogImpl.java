package request.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.silverspoon.bulldog.core.io.bus.spi.SpiConnection;
import io.silverspoon.bulldog.core.io.bus.spi.SpiMessage;

import java.io.IOException;
import request.StringConstants;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public final class SpiManagerBulldogImpl implements SpiManager {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(SpiManager.class);
    private SpiConnection spiConnection;
    private final BoardManager boardManager;
    private static final int MAX_SLAVE_INDEX = 2;

    private SpiManagerBulldogImpl(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    public static SpiManagerBulldogImpl getInstance(BoardManager boardManager) {
        return new SpiManagerBulldogImpl(boardManager);
    }

    @Override
    public String readFromSpi(int slaveIndex, byte[] rBuffer) {
        LOGGER.debug("Reading from SPI...");
        SpiMessage message;
        try {
            createConnectionIfNecessary(slaveIndex);
            message = spiConnection.transfer(rBuffer);
        } catch (IOException ex) {
            LOGGER.error(null, ex);
            return StringConstants.ERROR_RESPONSE.toString();
        }
        LOGGER.debug("reading finished.");
        return new String(message.getReceivedBytes());
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
