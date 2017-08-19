package request.manager;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public interface SpiManager extends InterfaceManager {

    String readFromSpi(int slaveIndex, byte[] rBuffer);

    void writeIntoSpi(int slaveIndex, byte[] tBuffer);
}
