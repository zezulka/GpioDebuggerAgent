package request.read;

import net.ProtocolManager;

import request.manager.SpiManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class SpiReadRequest implements ReadRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpiReadRequest.class);
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
        writeSpiInfoIntoLogger(this.tBuffer);
        return MANAGER.readFromSpi(this.tBuffer);
    }

    private void writeSpiInfoIntoLogger(byte[] tBuffer) {
        StringBuilder builder = new StringBuilder();
        builder.append("Spi read request:\n");
        builder.append("contents of transferbuffer:\n");
        for(byte element : tBuffer) {
            builder = builder.append(' ').append(element);
        }
        builder = builder.append('\n');
        LOGGER.info(builder.toString());
    }

    @Override
    public void giveFeedbackToClient() {
      StringBuilder build = new StringBuilder();
      String response = read();
      for(char c : response.toCharArray()) {
          build = build.append(getOneLineOfResponse(c));
      }
      ProtocolManager.getInstance().setMessageToSend("SPI interface"
              + " response:\n" + build.toString() + '\n');
    }

    private String getOneLineOfResponse(char c) {
        StringBuilder builder = new StringBuilder();
        return builder.append("0b")
                     .append(Integer.toBinaryString(c))
                     .append("\t\t")
                     .append("0x")
                     .append(Integer.toHexString(c))
                     .append("\t\t")
                     .append("dec")
                     .append(Integer.valueOf(c))
                     .append('\n').toString();
    }

}
