package request.read;

import net.AgentConnectionManager;

import request.manager.SpiManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.StringConstants;
/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class SpiReadRequest implements ReadRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpiReadRequest.class);
    private final SpiManager spiManager;
    private final byte[] tBuffer;
    private final int slaveIndex;

    public SpiReadRequest(SpiManager spiManager, int slaveIndex, byte[] tBuffer) {
      this.tBuffer = tBuffer;
      this.slaveIndex = slaveIndex;
      this.spiManager = spiManager;
    }

    /**
      * Supposes that the request submitted wishes to return some response.
      * @return contents of read buffer converted into String
      *
      */
    @Override
    public String read() {
        writeSpiInfoIntoLogger(this.tBuffer);
        return spiManager.readFromSpi(this.slaveIndex, this.tBuffer);
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
      AgentConnectionManager.setMessageToSend(String.format(StringConstants.SPI_READ_RESPONSE_FORMAT.toString(), build.toString() + '\n'));
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
