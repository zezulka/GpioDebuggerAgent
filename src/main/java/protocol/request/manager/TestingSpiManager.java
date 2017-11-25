package protocol.request.manager;

import java.util.Random;

public final class TestingSpiManager implements SpiManager {

    private static final Random RANDOM = new Random();

    @Override
    public byte[] readFromSpi(int slaveIndex, byte[] rBuffer) {
        RANDOM.nextBytes(rBuffer);
        return rBuffer;
    }

    @Override
    public void writeIntoSpi(int slaveIndex, byte[] tBuffer) {
        // NO-OP as readFromSpi returns random data anyway
    }

}
