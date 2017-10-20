package mocks;

import protocol.request.manager.I2cManager;

public class MockedI2cManager implements I2cManager {

    public MockedI2cManager() {
    }

    @Override
    public byte[] readFromI2c(int slave, int len) {
        return new byte[]{-128, 127};
    }

    @Override
    public void writeIntoI2c(int slave, byte[] message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
