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
package mocks;

import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.io.bus.spi.SpiBus;

import io.silverspoon.bulldog.core.pin.Pin;

import io.silverspoon.bulldog.core.platform.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import request.manager.BoardManager;

/**
 *
 * @author Miloslav Zezulka
 */
public class MockedDeviceManager implements BoardManager {
    private static final Board BOARD = new MockedBoard();
    private static List<I2cBus> I2CBUSES = Collections.EMPTY_LIST;
    private static final BoardManager INSTANCE = new MockedDeviceManager();

    private MockedDeviceManager() {
        if(BOARD == null) {
            throw new IllegalArgumentException("board cannot be null");
        }
        I2CBUSES = new ArrayList<>(BOARD.getI2cBuses());
    }

    public static BoardManager getInstance() {
        return INSTANCE;
    }
    /**
     * Returns board name.
     * @return String representation of the name.
     */
    @Override
    public String getBoardName() {
        return BOARD.getName();
    }

    @Override
    public I2cBus getI2c() {
        return I2CBUSES.size() < 1 ? null : I2CBUSES.get(0);
    }


    @Override
    public SpiBus getSpi() {
        List<SpiBus> buses = BOARD.getSpiBuses();
        return buses.size() < 1 ? null : buses.get(0);
    }
    /*
     * NO-OP
     */
    @Override
    public void cleanUpResources() {
    }

    @Override
    public Board getBoard() {
        return BOARD;
    }
}
