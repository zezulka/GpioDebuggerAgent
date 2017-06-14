package request;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.pin.Pin;

import request.interrupt.StartEpollInterruptListenerRequest;
import request.interrupt.InterruptListenerArgs;
import request.manager.GpioManager;

import request.manager.InterfaceManager;

public class StartInterruptRequestFactory {

    public static Request of(InterfaceManager gpioManager, String interruptListenerArgs) throws IllegalRequestException {
        if(gpioManager instanceof GpioManager) {
            return StartInterruptRequestFactory.of(gpioManager, interruptListenerArgs);  
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
            return new StartEpollInterruptListenerRequest(new InterruptListenerArgs(pin, edge));
        }
        throw new IllegalRequestException("Corrupted string format.");
    }

}
