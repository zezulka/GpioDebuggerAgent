package request;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.pin.Pin;
import request.interrupt.InterruptListenerArgs;
import request.interrupt.StopEpollInterruptListenerRequest;
import request.manager.GpioManager;
import request.manager.InterfaceManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopInterruptRequestFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopInterruptRequestFactory.class);

    public static Request of(InterfaceManager gpioManager, String interruptListenerArgs) throws IllegalRequestException {
        if(gpioManager instanceof GpioManager) {
            return StopInterruptRequestFactory.of((GpioManager)gpioManager, interruptListenerArgs);
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
                throw new IllegalRequestException(ex);
            }
            LOGGER.info(String.format("Interrupt listener removal request submitted: pin : %s, type: %s", pin.getName(), edge.toString()));
            return new StopEpollInterruptListenerRequest(new InterruptListenerArgs(pin, edge));
        }
        throw new IllegalRequestException("Corrupted string format.");
    }

}
