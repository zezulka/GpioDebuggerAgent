/*
 * Copyright 2017 miloslav.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package protocol.request;

import board.test.BoardManager;
import board.test.TestingBoard;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.io.bus.spi.SpiBus;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.platform.Board;
import mocks.MockedGpioManager;
import mocks.MockedI2cManager;
import mocks.MockedSpiManager;
import protocol.request.manager.I2cManager;
import protocol.request.manager.InterfaceManager;
import protocol.request.manager.SpiManager;

import java.util.function.Function;

public class RequestParserUtils {

    public static final MockedGpioManager MOCKED_PIN_ACCESSOR = new MockedGpioManager();
    public static final I2cManager MOCKED_I2C_MANAGER = new MockedI2cManager();
    public static final SpiManager MOCKED_SPI_MANAGER = new MockedSpiManager();
    public static final Pin REQUESTED_PIN = MOCKED_PIN_ACCESSOR.getBoard().getPin("P0");
    public static final String REQUESTED_PIN_NAME = REQUESTED_PIN.getName();
    public static final BoardManager BOARD_MANAGER = new BoardManager() {
        @Override
        public String getBoardName() {
            return "TEST_DEVICE";
        }

        @Override
        public Board getBoard() {
            return new TestingBoard();
        }

        @Override
        public void cleanUpResources() {
            //NO-OP
        }

        @Override
        public I2cBus getI2c() {
            return null;
        }

        @Override
        public SpiBus getSpi() {
            return null;
        }

        @Override
        public Function<RequestType, InterfaceManager> deviceToInterfaceMapper() {
            return (t) -> {
                switch (t) {
                    case GPIO:
                        return MOCKED_PIN_ACCESSOR;
                    case I2C:
                        return MOCKED_I2C_MANAGER;
                    case SPI:
                        return MOCKED_SPI_MANAGER;
                    default:
                        throw new IllegalArgumentException();
                }
            };
        }
    };
}
