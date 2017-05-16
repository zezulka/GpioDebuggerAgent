/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request.read;
/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class SpiReadRequest implements ReadRequest {

    private static final SpiManager MANAGER;
    private final int slaveIndex;
    private final byte[] rBuffer;

    public SpiReadRequest(int slaveIndex, byte[] rBuffer) {
      this.slaveIndex = slaveIndex;
      this.tBuffer = tBuffer;
    }

    @Override
    public String read() {
        return MANAGER.readFromSpi(this.rBuffer);
    }

    @Override
    public void giveFeedbackToClient() {
      ProtocolManager.getInstance().setMessageToSend("SPI interface read"
              + " response:\n" + read() + '\n');
    }

}
