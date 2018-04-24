package protocol.request.interrupt;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.pin.Pin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.request.IllegalRequestException;
import protocol.request.Request;
import protocol.request.StringConstants;
import protocol.request.manager.GpioManager;
import protocol.request.manager.InterfaceManager;

/**
 * Handles client request for registering interrupt listener (edge-triggered).
 *
 */
public final class StartInterruptRequestFactory {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(StartInterruptRequestFactory.class);

    private StartInterruptRequestFactory() {
    }

    public static Request of(InterfaceManager gpioManager,
            String interruptEventArgs) throws IllegalRequestException {
        if (gpioManager instanceof GpioManager) {
            return StartInterruptRequestFactory.of((GpioManager) gpioManager,
                    interruptEventArgs);
        }
        throw new IllegalRequestException();
    }

    private static Request of(GpioManager pinAccessor,
            String intrEventArgs) throws IllegalRequestException {
        String[] strArr
                = intrEventArgs.split(StringConstants.VAL_SEPARATOR);
        if (strArr.length != 2) {
            throw new IllegalRequestException("Corrupted string format.");
        }

        Pin pin = pinAccessor.getPin(strArr[0]);
        if (pin == null) {
            throw new IllegalRequestException(String
                    .format("Pin %s not found.", strArr[0]));
        }
        Edge edge;
        try {
            edge = Edge.valueOf(getFirstUppercaseRestLowercase(strArr[1]));
        } catch (IllegalArgumentException ex) {
            throw new IllegalRequestException(ex);
        }
        return new StartInterruptListenerRequest(
                new InterruptEventArgs(pin, edge)
        );
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
