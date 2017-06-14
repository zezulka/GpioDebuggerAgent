package request.write;

import net.ProtocolManager;

import request.manager.SpiManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class SpiWriteRequest implements WriteRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpiWriteRequest.class);
    private final SpiManager spiManager;
    private final int slaveIndex;
    private final byte[] tBuffer;

    public SpiWriteRequest(SpiManager spiManager, int slaveIndex, byte[] tBuffer) {
        this.tBuffer = tBuffer;
        this.slaveIndex = slaveIndex;
        this.spiManager = spiManager;
    }

    @Override
    public void write() {
        spiManager.writeIntoSpi(this.slaveIndex, this.tBuffer);
        writeSpiInfoIntoLogger(this.tBuffer);
    }

    private void writeSpiInfoIntoLogger(byte[] tBuffer) {
        StringBuilder builder = new StringBuilder();
        builder.append("Spi write request:\n");
        builder.append("contents of transferbuffer:\n");
        for(byte element : tBuffer) {
            builder = builder.append(' ').append(element);
        }
        builder = builder.append('\n');
        LOGGER.info(builder.toString());
    }

    /**
      * Very important note: no dynamic content is sent to client by this method,
      * only message confirming that request has been successfully processed.
      * This is due to variability of SPI devices, which do not have an uniform
      * way of communicating with them. Should client wish to view results
      * of this request, he must submit another SpiReadRequest.
      */
    @Override
    public void giveFeedbackToClient() {
      ProtocolManager.getInstance().setMessageToSend("SPI interface write successfull.");
    }
}
