package request;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.gpio.DigitalIO;
import io.silverspoon.bulldog.core.pin.Pin;

import request.interrupt.StartEpollInterruptListenerRequest;
import request.interrupt.InterruptListenerArgs;
import request.manager.GpioManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import request.manager.InterfaceManager;

/**
 * Handles client request for registering interrupt listener (edge-trigged).
 * @author Miloslav Zezulka
 */
public class StartInterruptRequestFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartInterruptRequestFactory.class);

    public static Request of(InterfaceManager gpioManager, String interruptListenerArgs) throws IllegalRequestException {
        if(gpioManager instanceof GpioManager) {
            return StartInterruptRequestFactory.of((GpioManager)gpioManager, interruptListenerArgs);
        }
        throw new IllegalRequestException();
    }

    private static Request of(GpioManager gpioManager, String interruptListenerArgs) throws IllegalRequestException {
        String[] strArray = interruptListenerArgs.split(StringConstants.VAL_SEPARATOR.toString());
        if (strArray.length == 2) {
            Pin pin = gpioManager.getPin(strArray[0]);
            if(pin == null) {
                throw new IllegalRequestException(String.format("Pin with name %s has not been found.", strArray[0]));
            }
            Edge edge;
            try {
                edge = Edge.valueOf(getFirstUppercaseRestLowercase(strArray[1]));
            } catch (IllegalArgumentException ex) {
                throw new IllegalRequestException(ex);
            }
            LOGGER.info(String.format("New interrupt listener request submitted: pin : %s, type: %s", pin.getName(), edge.toString()));
            return new StartEpollInterruptListenerRequest(new InterruptListenerArgs(pin.as(DigitalIO.class), edge));
        }
        throw new IllegalRequestException("Corrupted string format.");
    }
    
    private static String getFirstUppercaseRestLowercase(String string) throws IllegalRequestException {
        if(string == null) {
            throw new IllegalRequestException();
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

}
