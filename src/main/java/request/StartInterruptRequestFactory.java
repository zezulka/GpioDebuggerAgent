package request;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.pin.Pin;

import request.interrupt.StartEpollInterruptListenerRequest;
import request.interrupt.InterruptListenerArgs;
import request.manager.GpioManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import request.manager.InterfaceManager;

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
            Edge edge;
            try {
                edge = Edge.valueOf(strArray[1]);
            } catch (IllegalArgumentException ex) {
                edge = null;
            }
            LOGGER.info(String.format("New interrupt listener request submitted: pin : %s, type: %s", pin.getName(), edge.toString()));
            return new StartEpollInterruptListenerRequest(new InterruptListenerArgs(pin, edge));
        }
        throw new IllegalRequestException("Corrupted string format.");
    }

}
