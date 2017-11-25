package board.test;

import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.io.bus.spi.SpiBus;
import io.silverspoon.bulldog.core.platform.Board;
import java.util.function.Function;
import protocol.request.DeviceInterface;
import protocol.request.manager.InterfaceManager;

public interface BoardManager {

    /**
     * Returns device name. This String is used as an identifier for client.
     *
     * @return String representation of the device.
     */
    String getBoardName();

    Board getBoard();

    void cleanUpResources();

    /**
     * Returns I2c bus, if such interface is available.
     *
     * @return i2c bus which is ready for R/W operations, null if no such
     * interface exists
     */
    I2cBus getI2c();

    SpiBus getSpi();

    Function<DeviceInterface, InterfaceManager> deviceToInterfaceMapper();
}
