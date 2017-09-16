package request.read;

import request.manager.SpiManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.BulldogRequestUtils;
import request.StringConstants;

public final class SpiReadRequest extends AbstractReadRequest {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(SpiReadRequest.class);
    private final SpiManager spiMngr;
    private final byte[] tBuf;
    private final int slaveIndex;

    public SpiReadRequest(SpiManager spiMngr, int slaveIndex, byte[] tBuf) {
        this.tBuf = tBuf;
        this.slaveIndex = slaveIndex;
        this.spiMngr = spiMngr;
    }

    @Override
    public String getFormattedResponse() {
        String formattedBytes = BulldogRequestUtils
                .getFormattedByteArray(spiMngr.readFromSpi(slaveIndex, tBuf));
        if (formattedBytes == null) {
            return StringConstants.ERROR_RESPONSE;
        } else {
            return String.format(StringConstants.SPI_READ_RESPONSE_FORMAT,
                    formattedBytes);
        }

    }
}
