package request.manager;

import io.silverspoon.bulldog.core.Signal;
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
     * @throws request.IllegalRequestException pin has not been found
     */
    public Signal read(Pin pin) throws IllegalRequestException;

    /**
     * Writes signal {@code sig} to {@code pin} given in the input.
     * @throws IllegalRequestException pin has not been found
     */
    public void write(Pin pin, Signal sig) throws IllegalRequestException;
    public Pin getPin(String pinName);
}
