package request.write;

import io.silverspoon.bulldog.core.Signal;

import request.IllegalRequestException;
import request.StringConstants;
import request.manager.PinAccessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.Request;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public final class GpioWriteRequest implements Request {

    private final boolean desiredVoltage;
    private static final Logger LOGGER
            = LoggerFactory.getLogger(GpioWriteRequest.class);
    private final PinAccessor pinAccessor;
    private final String pinName;

    public GpioWriteRequest(PinAccessor pinAccessor, String pinName,
            String desiredVoltage) throws IllegalRequestException {
        try {
            this.desiredVoltage = Integer.parseInt(desiredVoltage) != 0;
        } catch (NumberFormatException e) {
            throw new IllegalRequestException("desiredVoltage not numeric");
        }
        this.pinName = pinName;
        this.pinAccessor = pinAccessor;
    }

    /**
     * This constructor derives desired voltage level from the current one on
     * the pin; inverted value is written onto the pin as result.
     *
     * @throws IllegalRequestException pin provided does not exist on this
     * board.
     */
    public GpioWriteRequest(PinAccessor gpioManager, String pinName)
            throws IllegalRequestException {
        this.pinAccessor = gpioManager;
        this.pinName = pinName;
        this.desiredVoltage = !this.pinAccessor.read(this.pinName);
    }

    @Override
    public void performRequest() {
        try {
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
    public String getFormattedResponse() {
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
