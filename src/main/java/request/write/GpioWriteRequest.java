package request.write;

import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.Signal;

import java.io.IOException;

import net.ConnectionManager;

import request.IllegalRequestException;
import request.StringConstants;

import request.manager.GpioManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public final class GpioWriteRequest implements WriteRequest {

    private final Pin pin;
    private final boolean desiredVoltage;
    private static final Logger LOGGER
            = LoggerFactory.getLogger(GpioWriteRequest.class);
    private final GpioManager gpioManager;

    public GpioWriteRequest(GpioManager gpioManager, String pinName,
            String desiredVoltage) throws IllegalRequestException {
        try {
            this.desiredVoltage = Integer.parseInt(desiredVoltage) != 0;
        } catch (NumberFormatException nfe) {
            throw new IllegalRequestException("desiredVoltage not numeric");
        }
        this.gpioManager = gpioManager;
        this.pin = this.gpioManager.getPin(pinName);
        if (pin == null) {
            throw new IllegalRequestException("pin not found");
        }
    }

    /**
     * This constructor derives desired voltage level from the current one on
     * the pin; inverted value is written onto the pin as result.
     *
     * @throws IllegalRequestException pin provided does not
     * exist on this board.
     */
    public GpioWriteRequest(GpioManager gpioManager, String pinName)
            throws IllegalRequestException {
        this.gpioManager = gpioManager;
        this.pin = this.gpioManager.getPin(pinName);
        if (pin == null) {
            throw new IllegalRequestException("pin not found");
        }
        try {
            this.desiredVoltage = !this.gpioManager.read(pin).getBooleanValue();
        } catch (IllegalRequestException ex) {
            LOGGER.error(String.format("pin %s not available", pin.getName()));
            throw new IllegalRequestException("pin not available");
        }
    }

    /**
     * Writes value to the pin (both specified in GpioWriteRequest constructor)
     * and checks whether the value has been actually written.
     *
     * @throws IllegalStateException check mentioned fails
     */
    @Override
    public void write() {
        try {
            gpioManager.write(pin, desiredVoltage ? Signal.High : Signal.Low);
        } catch (IllegalRequestException ex) {
            LOGGER.error("GPIO write request failed.", ex);
        }
    }

    /**
     * Sends message to client whether the provided Pin (specified in
     * {@code GpioWriteRequest.getInstance(name)} by {@code name} argument) is
     * turned off or on.
     */
    @Override
    public void giveFeedbackToClient() throws IOException {
        try {
            ConnectionManager.setMessageToSend(String.format(
                    StringConstants.GPIO_RESPONSE_FORMAT,
                    pin.getName(),
                    gpioManager.read(pin).getBooleanValue() ? "HIGH" : "LOW")
            );
        } catch (IllegalRequestException ex) {
            LOGGER.error("read after modification failed", ex);
        }
    }

}
