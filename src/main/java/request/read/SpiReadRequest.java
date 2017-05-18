package request.read;

import net.ProtocolManager;
import request.manager.SpiManager;
/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class SpiReadRequest implements ReadRequest {

    private static SpiManager MANAGER;
    private final byte[] tBuffer;

    public SpiReadRequest(int slaveIndex, byte[] tBuffer) {
      this.tBuffer = tBuffer;
      MANAGER = SpiManager.fromIndex(slaveIndex);
    }

    @Override
    public String read() {
        return MANAGER.readFromSpi(this.tBuffer);
    }

    @Override
    public void giveFeedbackToClient() {
      ProtocolManager.getInstance().setMessageToSend("SPI interface read"
              + " response:\n" + read() + '\n');
    }

}
