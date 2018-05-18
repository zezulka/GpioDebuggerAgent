package protocol.request.read;

import protocol.request.BulldogRequestUtils;
import protocol.request.StringConstants;
import protocol.request.manager.SpiManager;

public final class SpiReadRequest extends AbstractReadRequest {

    private final SpiManager spiMngr;
    private final byte[] tBuf;
    private final int slaveIndex;

    public SpiReadRequest(SpiManager spiMngr, int slaveIndex, byte[] tBuf) {
        this.tBuf = tBuf;
        this.slaveIndex = slaveIndex;
        this.spiMngr = spiMngr;
    }

    @Override
    public String responseString() {
        String formattedBytes = BulldogRequestUtils
                .getFormattedByteArray(spiMngr.readFromSpi(slaveIndex, tBuf));
        return String.format(StringConstants.SPI_READ_RESPONSE_FORMAT,
                formattedBytes);
    }
}
