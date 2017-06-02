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
      StringBuilder build = new StringBuilder();
      String response = read();
      for(char c : response.toCharArray()) {
        build = build.append(Integer.toBinaryString(c)).append(';');
      }
      ProtocolManager.getInstance().setMessageToSend("SPI interface read"
              + " response:\n" + build.toString() + '\n');
    }

}
