package request.write;

import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.Signal;

import java.io.IOException;

import net.ProtocolManager;

import request.IllegalRequestException;

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

    public GpioWriteRequest(Pin pin, boolean desiredVoltage) {
        this.pin = pin;
        this.desiredVoltage = desiredVoltage;
    }

    /**
      * This constructor derives desired voltage level from the current one on the pin;
      * inverted value is written onto the pin as result.
      * @throws IllegalArgumentException pin provided in the constructor does not
      * exist on this board.
      */
    public GpioWriteRequest(Pin pin) {
        this.pin = pin;
        try {
            this.desiredVoltage = !GpioManager.getInstance().getBooleanRead(pin);
        } catch (IllegalRequestException ex) {
            LOGGER.error(String.format("pin provided (%s) is not available on this board", pin.getName()));
            throw new IllegalArgumentException("pin provided is not available on this board");
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
            GpioManager.getInstance().write(this.pin, desiredVoltage ? Signal.High : Signal.Low);
            //let's make sure the value is the one the client requested
            if(this.desiredVoltage != GpioManager.getInstance().getBooleanRead(pin)) {
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
                GpioManager.RESPONSE_FORMAT, this.desiredVoltage ? "HIGH" : "LOW", this.pin.getAddress(),
                this.pin.getIndexOnPort(),this.pin.getName()));
    }

}
