package request.write;

import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.Signal;

import java.io.IOException;

import net.ProtocolManager;

import request.IllegalRequestException;
import request.StringConstants;

import request.manager.GpioManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class GpioWriteRequest implements WriteRequest {

    private final Pin pin;
    private final boolean desiredVoltage;
    private static final Logger LOGGER = LoggerFactory.getLogger(GpioWriteRequest.class);
    private final GpioManager gpioManager;

    public GpioWriteRequest(GpioManager gpioManager, String pinName, String desiredVoltage) throws IllegalRequestException {
        try {
            this.desiredVoltage = Integer.parseInt(desiredVoltage) != 0;
        } catch(NumberFormatException nfe) {
            throw new IllegalRequestException("desiredVoltage not numeric");
        }
        this.gpioManager = gpioManager;
        this.pin = this.gpioManager.getPin(pinName);
        if(pin == null) {
            throw new IllegalRequestException("pin with the given name has not been found");
        }
    }

    /**
      * This constructor derives desired voltage level from the current one on the pin;
      * inverted value is written onto the pin as result.
      * @throws IllegalRequestException pin provided in the constructor does not
      * exist on this board.
      */
    public GpioWriteRequest(GpioManager gpioManager, String pinName) throws IllegalRequestException {
        this.gpioManager = gpioManager;
        this.pin = this.gpioManager.getPin(pinName);
        if(pin == null) {
            throw new IllegalRequestException("pin with the given name has not been found");
        }
        try {
            this.desiredVoltage = !this.gpioManager.read(pin).getBooleanValue();
        } catch (IllegalRequestException ex) {
            LOGGER.error(String.format("pin provided (%s) is not available on this board", pin.getName()));
            throw new IllegalRequestException("pin provided is not available on this board");
        }
    }

    /**
     * Writes value to the pin (both specified in GpioWriteRequest constructor)
     * and checks whether the value has been actually written.
     * @throws IllegalStateException check mentioned fails
     * @throws IllegalRequestException should the write operation fail
     */
    @Override
    public void write() {
        try {
            gpioManager.write(this.pin, desiredVoltage ? Signal.High : Signal.Low);
            //let's make sure the value is the one the client requested
            if(this.desiredVoltage != gpioManager.read(pin).getBooleanValue()) {
                throw new IllegalStateException(
                    String.format("Pin %s has been written to, expected value %b, got %b",
                    this.pin.getName(), this.desiredVoltage, !this.desiredVoltage)
                );
            }
        } catch (IllegalRequestException ex) {
            LOGGER.error("GPIO write request failed.", ex);
        }
    }

    /**
     * Sends message to client whether the provided Pin
     * (specified in {@code GpioWriteRequest.getInstance(name)} by {@code name} argument)
     * is turned off or on.
     */
    @Override
    public void giveFeedbackToClient() throws IOException {
        ProtocolManager.getInstance().setMessageToSend(String.format(
                StringConstants.GPIO_RESPONSE_FORMAT.toString(), this.desiredVoltage ? "HIGH" : "LOW", this.pin.getAddress(),
                this.pin.getIndexOnPort(),this.pin.getName()));
    }

}
