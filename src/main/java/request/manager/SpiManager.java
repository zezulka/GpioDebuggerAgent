package request.manager;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public interface SpiManager extends InterfaceManager {

    public String readFromSpi(int slaveIndex, byte[] rBuffer);

    public void writeIntoSpi(int slaveIndex, byte[] tBuffer);
}
