package mocks;

import board.manager.BoardManager;
import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.platform.Board;

import request.IllegalRequestException;
import request.manager.PinAccessor;

public class MockedPinAccessor implements PinAccessor {
    private static final BoardManager MOCKED_MANAGER = MockedDeviceManager.getInstance();

    public MockedPinAccessor() {
    }

    private void applyVoltage(Signal sig, Pin pin) {
        pin.getFeature(MockedDigitalIoFeature.class).write(sig);
    }
    
    @Override
    public boolean read(String pinName) throws IllegalRequestException {
        Pin pin = getPin(pinName);
        MockedDigitalIoFeature feat = pin.getFeature(MockedDigitalIoFeature.class);
        feat.unblockPin();
        return feat.read().getBooleanValue();
    }

    @Override
    public void write(Signal sig, String pinName) throws IllegalRequestException {
        applyVoltage(sig, getPin(pinName));
    }

    @Override
    public Pin getPin(String pinName) {
        return MOCKED_MANAGER.getBoard().getPin(pinName);
    }
    
    /**
     * For testing purposes only. This avoids catching IllegalRequestException.
     * @return 
     */
    public Board getBoard(){
        return MOCKED_MANAGER.getBoard();
    }
}
