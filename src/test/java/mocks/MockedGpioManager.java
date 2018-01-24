package mocks;

import board.test.BoardManager;
import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.platform.Board;
import protocol.request.IllegalRequestException;
import protocol.request.manager.GpioManager;

public class MockedGpioManager implements GpioManager {

    private static final BoardManager MOCKED_MANAGER = MockedDeviceManager.getInstance();

    public MockedGpioManager() {
    }

    private void applyVoltage(Signal sig, Pin pin) {
        pin.getFeature(MockedDigitalIoFeature.class).write(sig);
    }

    @Override
    public boolean read(String pinName) throws IllegalRequestException {
        Pin pin = getPin(pinName);
        if (pin == null) {
            throw new IllegalRequestException();
        }
        MockedDigitalIoFeature feat = pin.getFeature(MockedDigitalIoFeature.class);
        feat.unblockPin();
        return feat.read().getBooleanValue();
    }

    @Override
    public void write(Signal sig, String pinName) throws IllegalRequestException {
        Pin pin = getPin(pinName);
        if (pin == null) {
            throw new IllegalRequestException();
        }
        applyVoltage(sig, pin);
    }

    @Override
    public Pin getPin(String pinName) {
        return MOCKED_MANAGER.getBoard().getPin(pinName);
    }

    /**
     * For testing purposes only. This avoids catching IllegalRequestException.
     *
     * @return
     */
    public Board getBoard() {
        return MOCKED_MANAGER.getBoard();
    }
}
