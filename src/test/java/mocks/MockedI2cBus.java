package mocks;

import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cConnection;
import io.silverspoon.bulldog.core.pin.Pin;

import java.io.IOException;

public class MockedI2cBus extends MockedBus implements I2cBus {

    MockedI2cBus(String name) {
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
        // TODO Auto-generated method stub

    }

    @Override
    public void writeBytesToRegister(int register, byte[] bytes) {
        // TODO Auto-generated method stub

    }

    @Override
    public byte readByteFromRegister(int register) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int readBytesFromRegister(int register, byte[] buffer) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public I2cConnection createI2cConnection(int address) {
        // TODO Auto-generated method stub
        return null;
    }

}
