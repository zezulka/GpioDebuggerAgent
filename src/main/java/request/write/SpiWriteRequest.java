/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request.write;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class SpiWriteRequest implements WriteRequest {

    private static final SpiManager MANAGER;
    private final int slaveIndex;
    private final byte[] tBuffer;

    public SpiWriteRequest(int slaveIndex, byte[] tBuffer) {
        this.slaveIndex = slaveIndex;
        this.tBuffer = tBuffer;
    }

    @Override
    public void write() {
        return MANAGER.writeInoSpi(this.tBuffer);
    }

    @Override
    public void giveFeedbackToClient() {
      ProtocolManager.getInstance().setMessageToSend("SPI interface write successfull,"
              + " response:\n");
    }


}
