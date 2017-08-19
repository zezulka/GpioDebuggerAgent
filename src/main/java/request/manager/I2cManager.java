package request.manager;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public interface I2cManager extends InterfaceManager {

    /**
     * Reads bytes from i2c interface.
     *
     * @return I2c contents.
     */
    String readFromI2c(int slave, int len);

    /*
     * Reads bytes from the given address. I2c connection must have
     * been established before calling this method.
     */
    void writeIntoI2c(int slave, byte[] message);
}
