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

import io.silverspoon.bulldog.core.io.bus.spi.SpiBus;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;

import io.silverspoon.bulldog.core.platform.Board;
import io.silverspoon.bulldog.core.platform.Platform;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which takes care of communicating with the device itself, namely using
 * bulldog library (for more information about the library, please see
 * <a href="https://github.com/SilverThings/bulldog" target="_blank">Bulldog
 * github repository</a>).
 *
 * @author Miloslav Zezulka, 2017
 */
public final class BoardManagerBulldogImpl implements BoardManager {

    private static final Board BOARD = Platform.createBoard();
    private static final BoardManager INSTANCE = new BoardManagerBulldogImpl();
    private static final Logger LOGGER = LoggerFactory.getLogger(BoardManagerBulldogImpl.class);

    public BoardManagerBulldogImpl() {
        if(BOARD == null) {
            throw new IllegalArgumentException("board cannot be null");
        }
    }

    public static BoardManager getInstance() {
        return INSTANCE;
    }

    @Override
    public String getBoardName() {
        return BOARD.getName();
    }

    @Override
    public void cleanUpResources() {
        BOARD.shutdown();
    }
    
    @Override
    public Board getBoard() {
        return BOARD;
    }

    @Override
    public I2cBus getI2c() {
        List<I2cBus> buses = BOARD.getI2cBuses();
        return buses.size() < 1 ? null : buses.get(0);
    }

    /**
      * Returns SPI bus. Note that this implementation returns the first bus
      * in the collection (held by Board object).
      */
    @Override
    public SpiBus getSpi() {
        List<SpiBus> buses = BOARD.getSpiBuses();
        return buses.size() < 1 ? null : buses.get(0);
    }
}
