package protocol.request.manager;

import board.test.BoardManager;
import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.base.DigitalIOFeature;
import io.silverspoon.bulldog.core.pin.Pin;
import protocol.request.IllegalRequestException;

import java.util.Objects;

public final class BulldogGpioManager implements GpioManager {

    private final BoardManager boardManager;

    public BulldogGpioManager(BoardManager boardManager) {
        Objects.requireNonNull(boardManager, "boardManager");
        this.boardManager = boardManager;
    }

    private void applyVoltage(Signal sig, Pin pin) {
        pin.getFeature(DigitalIOFeature.class).write(sig);
    }

    private Signal readSignal(Pin pin) {
        return pin.getFeature(DigitalIOFeature.class).read();
    }

    /**
     * Attempts to read input denoted in getInstance() method and if successful,
     * returns the pin numeric value as a result.
     *
     * @return String numeric representation of the read signal
     */
    @Override
    public boolean read(String pinName) throws IllegalRequestException {
        return readSignal(getPin(pinName)).getBooleanValue();
    }

    @Override
    public void write(Signal sig, String pinName)
            throws IllegalRequestException {
        applyVoltage(sig, getPin(pinName));
    }

    @Override
    public Pin getPin(String pinName) throws IllegalRequestException {
        Pin result = boardManager.getBoard().getPin(pinName);
        if (result == null) {
            throw new IllegalRequestException("pin has not been found");
        }
        return result;
    }
}
