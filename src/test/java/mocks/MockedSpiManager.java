package mocks;

import protocol.request.manager.SpiManager;

import java.util.Objects;

public class MockedSpiManager implements SpiManager {

    @Override
    public byte[] readFromSpi(int slaveIndex, byte[] rBuffer) {
        Objects.requireNonNull(rBuffer, "rBuffer");
        for(byte i = 0; i < rBuffer.length; i++) {
            rBuffer[i] = i;
        }
        return rBuffer;
    }

    @Override
    public void writeIntoSpi(int slaveIndex, byte[] tBuffer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
