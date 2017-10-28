package mocks;

import protocol.request.manager.I2cManager;

public class MockedI2cManager implements I2cManager {

    public MockedI2cManager() {
    }

    @Override
    public byte[] readFromI2c(int slave, int len) {
        len = (byte) len;
        byte[] bytes = new byte[len];
        for(byte i = 0; i < bytes.length; i++) {
            bytes[i] = i;
        }
        return bytes;
    }

    @Override
    public void writeIntoI2c(int slave, byte[] message) {
        //NO-OP
    }

}
