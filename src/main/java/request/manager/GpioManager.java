package request.manager;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.base.DigitalIOFeature;
import io.silverspoon.bulldog.core.pin.Pin;
import request.IllegalRequestException;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public interface GpioManager extends InterfaceManager {

    /**
     * Reads voltage from the pin specified by {@code pin} argument.
     *
     * @param descriptor
     * @return voltage level represented by boolean value
     * {@code 1: Signal.HIGH, 0: Signal.LOW}
     * @throws request.IllegalRequestException
     */
    public Signal read(Pin pin) throws IllegalRequestException;

    /**
     * Writes signal onto specifed Pin.
     * @param descriptor
     * @param message
     * @throws IllegalRequestException
     */
    public void write(Pin pin, Signal sig) throws IllegalRequestException;
    public Pin getPin(String pinName);
}
