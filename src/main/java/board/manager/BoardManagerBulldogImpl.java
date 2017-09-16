package board.manager;

import io.silverspoon.bulldog.core.io.bus.spi.SpiBus;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;

import io.silverspoon.bulldog.core.platform.Board;
import io.silverspoon.bulldog.core.platform.Platform;

import java.util.List;

/**
 * Class which takes care of communicating with the device itself, namely using
 * bulldog library (for more information about the library, please see
 * <a href="https://github.com/SilverThings/bulldog" target="_blank">Bulldog
 * github repository</a>).
 *
 */
public final class BoardManagerBulldogImpl implements BoardManager {

    private static final Board BOARD = Platform.createBoard();
    private static final BoardManager INSTANCE = new BoardManagerBulldogImpl();

    public BoardManagerBulldogImpl() {
        if (BOARD == null) {
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
     * Returns SPI bus. Note that this implementation returns the first bus in
     * the collection (held by Board object).
     */
    @Override
    public SpiBus getSpi() {
        List<SpiBus> buses = BOARD.getSpiBuses();
        return buses.size() < 1 ? null : buses.get(0);
    }
}
