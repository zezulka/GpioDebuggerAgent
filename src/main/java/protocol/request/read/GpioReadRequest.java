package protocol.request.read;

import protocol.request.IllegalRequestException;
import protocol.request.StringConstants;
import protocol.request.manager.GpioManager;

public final class GpioReadRequest extends AbstractReadRequest {

    private final GpioManager gpioManager;
    private final String pinName;

    public GpioReadRequest(GpioManager gpioManager, String pinName) {
        this.gpioManager = gpioManager;
        this.pinName = pinName;
    }

    @Override
    public String responseString() {
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
