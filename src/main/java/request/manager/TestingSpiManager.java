package request.manager;

import java.util.Random;

public final class TestingSpiManager implements SpiManager {

    @Override
    public byte[] readFromSpi(int slaveIndex, byte[] rBuffer) {
        new Random(slaveIndex).nextBytes(rBuffer);
        return rBuffer;
    }

    @Override
    public void writeIntoSpi(int slaveIndex, byte[] tBuffer) {
        // NO-OP as readFromSpi returns random data anyway
    }

}
