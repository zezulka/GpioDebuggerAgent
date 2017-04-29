package request.manager;

import core.DeviceManager;
import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.base.DigitalIOFeature;
import io.silverspoon.bulldog.core.pin.Pin;
import request.IllegalRequestException;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class GpioManager implements InterfaceManager {

    private static final GpioManager INSTANCE = new GpioManager();
    private static final String ON = "1";
    private static final String OFF = "0";
    public static final String RESPONSE_FORMAT = "Voltage level of pin '%s' is %s";

    private GpioManager() {
    }

    public static GpioManager getInstance() {
        return INSTANCE;
    }

    private static void applyVoltage(Signal sig, Pin pin) {
        pin.getFeature(DigitalIOFeature.class).write(sig);
    }

    /**
     * Reads voltage from the pin specified by {@code pin} argument.
     *
     * @param descriptor
     * @return voltage level represented by boolean value
     * {@code 1: Signal.HIGH, 0: Signal.LOW}
     * @throws request.IllegalRequestException
     */
    @Override
    public String read(String descriptor) throws IllegalRequestException {
        if (descriptor == null) {
            throw new IllegalRequestException("descriptor cannot be null");
        }
        Pin pin = DeviceManager.getPin(descriptor);
        if (pin == null) {
            throw new IllegalRequestException("descriptor does not have any mapping to it on this board");
        }
        return Integer.toString(pin.getFeature(DigitalIOFeature.class).read().getNumericValue());
    }

    public boolean getBooleanRead(String descriptor) throws IllegalRequestException {
        return read(descriptor).equals(ON);
    }


    /**
     *
     * @param descriptor
     * @param message
     * @throws IllegalRequestException
     */
    @Override
    public void write(String descriptor, String message) throws IllegalRequestException {
        Pin pin = DeviceManager.getPin(descriptor);
        if (pin == null) {
            throw new IllegalRequestException("descriptor does not have any mapping to it on this board");
        }
        Signal sig;
        try {
            sig = Signal.fromString(message);
        } catch (IllegalArgumentException ex) {
            throw new IllegalRequestException(ex);
        }
        applyVoltage(sig, pin);
    }
}
