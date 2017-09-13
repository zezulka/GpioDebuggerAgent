package request.manager;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.pin.Pin;
import request.IllegalRequestException;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public interface PinAccessor extends InterfaceManager {

    /**
     *
     * @return false if logical low, true otherwise
     * @throws request.IllegalRequestException pin with pinName does not exist
     * on this board
     */
    boolean read(String pinName) throws IllegalRequestException;

    void write(Signal sig, String pinName) throws IllegalRequestException;

    Pin getPin(String pinName) throws IllegalRequestException;
}
