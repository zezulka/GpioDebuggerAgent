package board.test;

import protocol.request.RequestType;
import protocol.request.manager.InterfaceManager;
import protocol.request.manager.TestingGpioManager;
import protocol.request.manager.TestingI2cManager;
import protocol.request.manager.TestingSpiManager;

import java.util.function.Function;

public class TestingBoardManager extends AbstractBoardManager {

    public TestingBoardManager() {
        super(new TestingBoard());
    }

    @Override
    public Function<RequestType, InterfaceManager>
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
