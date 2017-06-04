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
public class GpioManager {

    private static final GpioManager INSTANCE = new GpioManager();
    private static final String ON = "1";
    private static final String OFF = "0";
    public static final String RESPONSE_FORMAT = "Gpio request response\n"+
                                                  "Pin voltage: %s\n"+
                                                  "Gpio address: %d\n"+
                                                  "Port index: %d\n"+
                                                  "Name: %s\n";

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
    public String read(Pin pin) throws IllegalRequestException {
        if (pin == null) {
            throw new IllegalRequestException("pin cannot be null");
        }
        return Integer.toString(pin.getFeature(DigitalIOFeature.class).read().getNumericValue());
    }

    public boolean getBooleanRead(Pin pin) throws IllegalRequestException {
        return read(pin).equals(ON);
    }


    /**
     * Writes signal onto specifed Pin.
     * @param descriptor
     * @param message
     * @throws IllegalRequestException
     */
    public void write(Pin pin, Signal sig) throws IllegalRequestException {
        if (pin == null) {
            throw new IllegalRequestException("descriptor does not have any mapping to it on this board");
        }
        applyVoltage(sig, pin);
    }
}
