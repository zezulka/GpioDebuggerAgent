package request.read;

import net.ConnectionManager;

import request.manager.SpiManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.StringConstants;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public final class SpiReadRequest implements ReadRequest {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(SpiReadRequest.class);
    private final SpiManager spiManager;
    private final byte[] tBuf;
    private final int slaveIndex;

    public SpiReadRequest(SpiManager spiManager, int slaveIndex, byte[] tBuf) {
        this.tBuf = tBuf;
        this.slaveIndex = slaveIndex;
        this.spiManager = spiManager;
    }

    /**
     * Supposes that the request submitted wishes to return some response.
     *
     * @return contents of read buffer converted into String
     *
     */
    @Override
    public String read() {
        writeSpiInfoIntoLogger(tBuf);
        return spiManager.readFromSpi(slaveIndex, tBuf);
    }

    private void writeSpiInfoIntoLogger(byte[] tBuffer) {
        StringBuilder builder = new StringBuilder();
        for (byte element : tBuffer) {
            builder = builder.append(' ').append(element);
        }
        LOGGER.info(builder.toString());
    }

    @Override
    public void giveFeedbackToClient() {
        StringBuilder build = new StringBuilder();
        String response = read();
        for (char c : response.toCharArray()) {
            build = build.append(getOneLineOfResponse(c));
        }
        ConnectionManager.setMessageToSend(String
                .format(StringConstants.SPI_READ_RESPONSE_FORMAT,
                        build.toString())
        );
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
