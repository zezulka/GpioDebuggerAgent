package board;

import board.test.AbstractBoardManager;
import io.silverspoon.bulldog.core.platform.Platform;
import protocol.request.RequestType;
import protocol.request.manager.BulldogGpioManager;
import protocol.request.manager.BulldogI2cManager;
import protocol.request.manager.BulldogSpiManager;
import protocol.request.manager.InterfaceManager;

import java.util.function.Function;

/**
 * Class which takes care of communicating with the device itself, namely using
 * bulldog library (for more information about the library, please see
 * <a href="https://github.com/SilverThings/bulldog" target="_blank">Bulldog
 * github repository</a>).
 *
 */
public final class BulldogBoardManager extends AbstractBoardManager {

    public BulldogBoardManager() {
        super(Platform.createBoard());
    }

    @Override
    public Function<RequestType, InterfaceManager>
            deviceToInterfaceMapper() {
        return (t) -> {
            switch (t) {
                case GPIO:
                    return new BulldogGpioManager(this);
                case I2C:
                    return new BulldogI2cManager(this);
                case SPI:
                    return new BulldogSpiManager(this);
                default:
                    throw new IllegalArgumentException();
            }
        };
    }
}
