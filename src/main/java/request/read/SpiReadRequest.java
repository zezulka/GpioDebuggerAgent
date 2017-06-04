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

    /**
      * Supposes that the request submitted wishes to return some response.
      * @return contents of read buffer converted into String
      *
      */
    @Override
    public String read() {
        return MANAGER.readFromSpi(this.tBuffer);
    }

    @Override
    public void giveFeedbackToClient() {
      StringBuilder build = new StringBuilder();
      String response = read();
      for(char c : response.toCharArray()) {
        build = build.append("0b")
                     .append(Integer.toBinaryString(c))
                     .append("\t\t")
                     .append("0x")
                     .append(Integer.toHexString(c))
                     .append("\t\t")
                     .append("dec")
                     .append(Integer.valueOf(c))
                     .append('\n');
      }
      ProtocolManager.getInstance().setMessageToSend("SPI interface"
              + " response:\n" + build.toString() + '\n');
    }

}
