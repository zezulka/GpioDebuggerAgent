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

    @Override
    public void giveFeedbackToClient() {
      ProtocolManager.getInstance().setMessageToSend("SPI interface write successfull,"
              + " response:\n");
    }
}
