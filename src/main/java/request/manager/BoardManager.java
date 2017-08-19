/*
 * Copyright 2017 Miloslav.
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
package request.manager;

import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.io.bus.spi.SpiBus;
import io.silverspoon.bulldog.core.platform.Board;

public interface BoardManager {

    /**
     * Returns device descriptor. This String is used as an identifier for
     * client.
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
}
