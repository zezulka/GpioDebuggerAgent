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

import io.silverspoon.bulldog.core.io.bus.Bus;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.platform.Board;
import io.silverspoon.bulldog.core.platform.Platform;

/**
 * Class which takes care of communicating with the device itself, namely using
 * bulldog library (for more information about the library, please see
 * <a href="https://github.com/SilverThings/bulldog" target="_blank">Bulldog
 * github repository</a>).
 *
 * @author Miloslav Zezulka, 2017
 */
public class DeviceManager {

    /**
     * Board interface instance which practically connects this agent
     * implementation to the services provided by the bulldog library.
     */
    private static final Board board = Platform.createBoard();
    private static final DeviceManager INSTANCE = new DeviceManager();
    
    private DeviceManager() { }
    
    public static DeviceManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * Returns device descriptor.
     * @return String representation of the device. 
     */
    public String getDeviceName() {
        return board.getName();
    }
    
    public Pin getPinFromName(String name) {
        return board.getPin(name);
    }
    
    public Pin getPinFromId(int id) {
        return board.getPin(id);
    }
    
    /**
     * Returns (the only available) I2c interface.
     * @return 
    */
    public Bus getI2c() {
        return board.getI2cBuses().get(0);
    }
}
