package board.test;

import java.util.function.Function;
import protocol.request.DeviceInterface;
import protocol.request.manager.InterfaceManager;
import protocol.request.manager.TestingGpioManager;
import protocol.request.manager.TestingI2cManager;
import protocol.request.manager.TestingSpiManager;

public class TestingBoardManager extends AbstractBoardManager {

    public TestingBoardManager() {
        super(new TestingBoard());
    }

    @Override
    public Function<DeviceInterface, InterfaceManager>
            deviceToInterfaceMapper() {
        return (t) -> {
            switch (t) {
                case GPIO:
                    return new TestingGpioManager(this);
                case I2C:
                    return new TestingI2cManager();
                case SPI:
                    return new TestingSpiManager();
                default:
                    throw new IllegalArgumentException();
            }
        };
    }
}
