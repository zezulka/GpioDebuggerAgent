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
package core;

import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.platform.Board;
import io.silverspoon.bulldog.core.platform.Platform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class which takes care of communicating with the device itself, namely using
 * bulldog library (for more information about the library, please see
 * <a href="https://github.com/SilverThings/bulldog" target="_blank">Bulldog
 * github repository</a>).
 *
 * @author Miloslav Zezulka, 2017
 */
public class DeviceManager {

    private static final Board BOARD = Platform.createBoard();
    private static final DeviceManager INSTANCE = new DeviceManager();

    public DeviceManager() {
        if(BOARD == null) {
            throw new IllegalArgumentException("board cannot be null");
        }
    }

    public static DeviceManager getInstance() {
        return INSTANCE;
    }
    /**
     * Returns device descriptor.
     * @return String representation of the device.
     */
    public static String getDeviceName() {
        return BOARD.getName();
    }

    public static Pin getPin(String pinName) {
        return BOARD.getPin(pinName);
    }

    /**
     * Returns I2c interface, if such interface is available.
     * @return i2c bus which is ready for R/W operations, null if no such
     * interface exists
    */
    public static I2cBus getI2c() {
        List<I2cBus> buses = DeviceManager.BOARD.getI2cBuses();
        return buses.size() < 1 ? null : buses.get(0);
    }

    public static SpiBus getSpi() {
        List<SpiBus> buses = DeviceManager.BOARD.getSpiBuses();
        return buses.size() < 1 ? null : buses.get(0);
    }

    public static void cleanUpResources() throws IOException {
        BOARD.shutdown();
    }
}
