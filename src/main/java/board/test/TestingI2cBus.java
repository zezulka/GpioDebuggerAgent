package board.test;

import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cConnection;
import io.silverspoon.bulldog.core.pin.Pin;

public class TestingI2cBus extends TestingBus implements I2cBus {

    TestingI2cBus(String name) {
        super(name);
    }

    @Override
    public Pin getSDA() {
        return null;
    }

    @Override
    public Pin getSCL() {
        return null;
    }

    @Override
    public int getFrequency() {
        return 0;
    }

    @Override
    public void writeByteToRegister(int register, int b) {
    }

    @Override
    public void writeBytesToRegister(int register, byte[] bytes) {
    }

    @Override
    public byte readByteFromRegister(int register) {
        return 0;
    }

    @Override
    public int readBytesFromRegister(int register, byte[] buffer) {
        return 0;
    }

    @Override
    public I2cConnection createI2cConnection(int address) {
        return null;
    }

}
