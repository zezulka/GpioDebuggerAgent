package request.write;

import net.ConnectionManager;

import request.manager.SpiManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.StringConstants;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public final class SpiWriteRequest implements WriteRequest {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(SpiWriteRequest.class);
    private final SpiManager spiManager;
    private final int slaveIndex;
    private final byte[] tBuf;

    public SpiWriteRequest(SpiManager spiManager, int slaveIndex, byte[] tBuf) {
        this.tBuf = tBuf;
        this.slaveIndex = slaveIndex;
        this.spiManager = spiManager;
    }

    @Override
    public void write() {
        spiManager.writeIntoSpi(slaveIndex, tBuf);
        writeSpiInfoIntoLogger(tBuf);
    }

    private void writeSpiInfoIntoLogger(byte[] tBuffer) {
        StringBuilder builder = new StringBuilder();
        builder.append("Spi write request:\n");
        builder.append("contents of transferbuffer:\n");
        for (byte element : tBuffer) {
            builder = builder.append(' ').append(element);
        }
        builder = builder.append('\n');
        LOGGER.info(builder.toString());
    }

    /**
     * Important note: no content is sent to client by this method, only message
     * confirming that request has been successfully processed. This is due to
     * variability of SPI devices, which do not have an uniform way of
     * communicating with them. Should client wish to view results of this
     * request, he must submit another SpiReadRequest.
     */
    @Override
    public void giveFeedbackToClient() {
        ConnectionManager.setMessageToSend(String.format(
                StringConstants.SPI_WRITE_RESPONSE_FORMAT.toString()));
    }
}
