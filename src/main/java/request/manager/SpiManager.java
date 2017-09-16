package request.manager;

public interface SpiManager extends InterfaceManager {

    /**
     *
     * @return byte array with read values, null if exception occurred
     */
    byte[] readFromSpi(int slaveIndex, byte[] rBuffer);

    void writeIntoSpi(int slaveIndex, byte[] tBuffer);
}
