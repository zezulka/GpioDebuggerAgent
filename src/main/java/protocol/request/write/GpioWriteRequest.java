package protocol.request.write;

import io.silverspoon.bulldog.core.Signal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.request.IllegalRequestException;
import protocol.request.StringConstants;
import protocol.request.manager.GpioManager;

public final class GpioWriteRequest implements WriteRequest {

    private Boolean desiredVoltage = null;
    private static final Logger LOGGER
            = LoggerFactory.getLogger(GpioWriteRequest.class);
    private final GpioManager pinAccessor;
    private final String pinName;

    public GpioWriteRequest(GpioManager pinAccessor, String pinName,
            String desiredVoltage) {
        try {
            this.desiredVoltage = Integer.parseInt(desiredVoltage) != 0;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("desiredVoltage not numeric");
        }
        this.pinName = pinName;
        this.pinAccessor = pinAccessor;
    }

    /**
     * This constructor derives desired voltage level from the current one on
     * the pin; inverted value is written onto the pin as result.
     *
     */
    public GpioWriteRequest(GpioManager gpioManager, String pinName) {
        this.pinAccessor = gpioManager;
        this.pinName = pinName;
    }

    @Override
    public void action() {
        try {
            if (desiredVoltage == null) {
                this.desiredVoltage = !this.pinAccessor.read(this.pinName);
            }
            pinAccessor.write(desiredVoltage ? Signal.High : Signal.Low,
                    pinName);
        } catch (IllegalRequestException ex) {
            LOGGER.error("GPIO write request failed", ex);
        }
    }

    /**
     * Sends message to client whether the provided Pin (specified in
     * {@code GpioWriteRequest.getInstance(name)} by {@code name} argument) is
     * turned off or on.
     */
    @Override
    public String responseString() {
        try {
            return String.format(
                    StringConstants.GPIO_RESPONSE_FORMAT,
                    pinName,
                    pinAccessor.read(pinName) ? "HIGH" : "LOW");
        } catch (IllegalRequestException ex) {
            LOGGER.error("could not read from pin after GPIO write", ex);
            return StringConstants.ERROR_RESPONSE;
        }
    }

}
