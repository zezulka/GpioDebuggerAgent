package protocol.request.manager;

import board.test.BoardManager;
import board.test.TestingDigitalIoFeature;
import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.pin.Pin;
import java.util.Objects;
import protocol.request.IllegalRequestException;

public class TestingGpioManager implements GpioManager {

    private final BoardManager boardManager;

    public TestingGpioManager(BoardManager boardManager) {
        Objects.requireNonNull(boardManager, "boardManager");
        this.boardManager = boardManager;
    }

    @Override
    public boolean read(String pinName) throws IllegalRequestException {
        return getPin(pinName).getFeature(TestingDigitalIoFeature.class)
                .read().getBooleanValue();
    }

    @Override
    public void write(Signal sig, String pinName)
            throws IllegalRequestException {
        getPin(pinName).getFeature(TestingDigitalIoFeature.class)
                .write(sig);
    }

    @Override
    public Pin getPin(String pinName) throws IllegalRequestException {
        return boardManager.getBoard().getPin(pinName);
    }

}