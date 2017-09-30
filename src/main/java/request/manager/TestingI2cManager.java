package request.manager;

import java.util.Random;

public final class TestingI2cManager implements I2cManager {

    @Override
    public byte[] readFromI2c(int slave, int len) {
        byte[] result = new byte[len];
        new Random(slave).nextBytes(result);
        return result;
    }

    @Override
    public void writeIntoI2c(int slave, byte[] message) {
        // NO-OP
    }

}
