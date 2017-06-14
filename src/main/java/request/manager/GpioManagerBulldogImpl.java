package request.manager;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.base.DigitalIOFeature;
import io.silverspoon.bulldog.core.pin.Pin;
import request.IllegalRequestException;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class GpioManagerBulldogImpl implements GpioManager {

    private final BoardManager boardManager;

    private GpioManagerBulldogImpl(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    public static GpioManager getInstance(BoardManager boardManager) {
        return new GpioManagerBulldogImpl(boardManager);
    }

    private static void applyVoltage(Signal sig, Pin pin) {
        pin.getFeature(DigitalIOFeature.class).write(sig);
    }

    @Override
    public Signal read(Pin pin) throws IllegalRequestException {
        if (pin == null) {
            throw new IllegalRequestException("pin cannot be null");
        }
        return pin.getFeature(DigitalIOFeature.class).read();
    }

    @Override
    public void write(Pin pin, Signal sig) throws IllegalRequestException {
        if (pin == null) {
            throw new IllegalRequestException("descriptor does not have any mapping to it on this board");
        }
        applyVoltage(sig, pin);
    }

    @Override
    public Pin getPin(String pinName) {
         return this.boardManager.getBoard().getPin(pinName);
    }
}
