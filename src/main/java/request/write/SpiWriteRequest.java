package request.write;

import net.ProtocolManager;
import request.manager.SpiManager;
/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class SpiWriteRequest implements WriteRequest {

    private static SpiManager MANAGER;
    private final byte[] tBuffer;

    public SpiWriteRequest(int slaveIndex, byte[] tBuffer) {
        this.tBuffer = tBuffer;
        MANAGER = SpiManager.fromIndex(slaveIndex);
    }

    @Override
    public void write() {
        MANAGER.writeIntoSpi(this.tBuffer);
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
