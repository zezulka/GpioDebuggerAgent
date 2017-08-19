package request;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.pin.Pin;
import request.interrupt.StopEpollInterruptListenerRequest;
import request.manager.GpioManager;
import request.manager.InterfaceManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles client request for deregistering interrupt listener (edge-triggered).
 *
 * @author Miloslav Zezulka, 2017
 */
public final class StopInterruptRequestFactory {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(StopInterruptRequestFactory.class);

    private StopInterruptRequestFactory() {
    }

    public static Request of(InterfaceManager gpioManager, String intrEventArgs)
            throws IllegalRequestException {
        if (gpioManager instanceof GpioManager) {
            return StopInterruptRequestFactory.of((GpioManager) gpioManager,
                    intrEventArgs);
        }
        throw new IllegalRequestException();
    }

    private static Request of(GpioManager gpioManager, String intrEventArgs)
            throws IllegalRequestException {
        String[] strArr
                = intrEventArgs.split(StringConstants.VAL_SEPARATOR.toString());
        if (strArr.length == 2) {
            Pin pin = gpioManager.getPin(strArr[0]);
            if (pin == null) {
                throw new IllegalRequestException(String
                        .format("Pin %s has not found.", strArr[0]));
            }
            Edge edge;
            try {
                edge = Edge.valueOf(getFirstUppercaseRestLowercase(strArr[1]));
            } catch (IllegalArgumentException ex) {
                throw new IllegalRequestException(ex);
            }
            LOGGER.info(String
                    .format("Interrupt listener removal request submitted: %s",
                            intrEventArgs));
            return new StopEpollInterruptListenerRequest(
                    new InterruptEventArgs(pin, edge)
            );
        }
        throw new IllegalRequestException("Corrupted string format.");
    }

    private static String getFirstUppercaseRestLowercase(String string)
            throws IllegalRequestException {
        if (string == null) {
            throw new IllegalRequestException();
        }
        return string.substring(0, 1).toUpperCase()
                + string.substring(1).toLowerCase();
    }
}
