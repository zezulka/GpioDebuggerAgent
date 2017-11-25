package protocol.request.manager;

import java.util.Random;

public final class TestingI2cManager implements I2cManager {

    private static final Random RANDOM = new Random();

    @Override
    public byte[] readFromI2c(int slave, int len) {
        byte[] result = new byte[len];

        RANDOM.nextBytes(result);
        return result;
    }

    @Override
    public void writeIntoI2c(int slave, byte[] message) {
        // NO-OP
    }

}
