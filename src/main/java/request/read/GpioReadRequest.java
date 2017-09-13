package request.read;

import request.IllegalRequestException;
import request.StringConstants;

import request.manager.PinAccessor;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public final class GpioReadRequest extends AbstractReadRequest {

    private final PinAccessor gpioManager;
    private final String pinName;

    public GpioReadRequest(PinAccessor gpioManager, String pinName)
            throws IllegalRequestException {
        this.gpioManager = gpioManager;
        this.pinName = pinName;
    }

    @Override
    public String getFormattedResponse() {
        try {
            String voltageLvl;
            if (gpioManager.read(pinName)) {
                voltageLvl = "HIGH";
            } else {
                voltageLvl = "LOW";
            }
            return String.format(
                    StringConstants.GPIO_RESPONSE_FORMAT,
                    pinName,
                    voltageLvl);
        } catch (IllegalRequestException ex) {
            return StringConstants.ERROR_RESPONSE;
        }
    }
}
