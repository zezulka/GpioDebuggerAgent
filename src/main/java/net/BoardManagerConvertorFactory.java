package net;

import board.test.BoardManager;
import board.test.TestingBoardManager;
import java.util.function.Function;
import protocol.request.DeviceInterface;
import protocol.request.manager.BulldogGpioManager;
import protocol.request.manager.BulldogI2cManager;
import protocol.request.manager.BulldogSpiManager;
import protocol.request.manager.InterfaceManager;
import protocol.request.manager.TestingGpioManager;
import protocol.request.manager.TestingI2cManager;
import protocol.request.manager.TestingSpiManager;

final class BoardManagerConvertorFactory {

    private BoardManagerConvertorFactory() {
    }

    static Function<DeviceInterface, InterfaceManager>
            getInstance(BoardManager manager) {

        if (manager instanceof TestingBoardManager) {
            return (t) -> {
                switch (t) {
                    case GPIO:
                        return new TestingGpioManager(manager);
                    case I2C:
                        return new TestingI2cManager();
                    case SPI:
                        return new TestingSpiManager();
                    default:
                        throw new IllegalArgumentException();
                }
            };
        } else {
            return (t) -> {
                switch (t) {
                    case GPIO:
                        return new BulldogGpioManager(manager);
                    case I2C:
                        return new BulldogI2cManager(manager);
                    case SPI:
                        return new BulldogSpiManager(manager);
                    default:
                        throw new IllegalArgumentException();
                }
            };
        }
    }

}
