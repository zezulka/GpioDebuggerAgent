package protocol.request.write;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.request.StringConstants;
import protocol.request.manager.SpiManager;

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
    public void action() {
        StringBuilder builder = new StringBuilder();
        builder.append("Spi write request:\n");
        builder.append("contents of transferbuffer:\n");
        for (byte element : tBuf) {
            builder = builder.append(' ').append(element);
        }
        spiManager.writeIntoSpi(slaveIndex, tBuf);
        LOGGER.debug(builder.toString());

    }

    /**
     * Important note: no data is sent to client by this method, only message
     * confirming that request has been successfully processed. This is due to
     * variability of SPI devices, which do not have an uniform way of
     * communicating with them. Should client wish to view results of this
     * request, he must submit another SPI read request instead.
     */
    @Override
    public String responseString() {
        return StringConstants.SPI_WRITE_RESPONSE;
    }
}
