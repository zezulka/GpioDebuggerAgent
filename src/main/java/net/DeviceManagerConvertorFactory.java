package net;

import board.manager.BoardManager;
import board.manager.TestingBoardManager;
import java.util.function.Function;
import request.DeviceInterface;
import request.manager.BulldogGpioManager;
import request.manager.BulldogI2cManager;
import request.manager.BulldogSpiManager;
import request.manager.InterfaceManager;
import request.manager.TestingGpioManager;
import request.manager.TestingI2cManager;
import request.manager.TestingSpiManager;

final class DeviceManagerConvertorFactory {

    private DeviceManagerConvertorFactory() {
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
