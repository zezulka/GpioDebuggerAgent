package mocks;

import protocol.request.manager.SpiManager;

public class MockedSpiManager implements SpiManager {

    @Override
    public byte[] readFromSpi(int slaveIndex, byte[] rBuffer) {
        return new byte[]{-128, 127};
    }

    @Override
    public void writeIntoSpi(int slaveIndex, byte[] tBuffer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
