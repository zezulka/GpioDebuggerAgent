package board.test;

import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.io.bus.spi.SpiBus;
import io.silverspoon.bulldog.core.platform.Board;
import java.util.List;

public abstract class AbstractBoardManager implements BoardManager {

    private final Board board;

    public AbstractBoardManager(Board board) {
        this.board = board;
    }

    @Override
    public final String getBoardName() {
        return board.getName();
    }

    @Override
    public final void cleanUpResources() {
        board.shutdown();
    }

    @Override
    public final Board getBoard() {
        return board;
    }

    /**
     * This implementation returns the first bus in the collection (held by
     * Board object).
     */
    @Override
    public final I2cBus getI2c() {
        List<I2cBus> buses = board.getI2cBuses();
        return buses.size() < 1 ? null : buses.get(0);
    }

    /**
     * This implementation returns the first bus in the collection (held by
     * Board object).
     */
    @Override
    public final SpiBus getSpi() {
        List<SpiBus> buses = board.getSpiBuses();
        return buses.size() < 1 ? null : buses.get(0);
    }
}
