package request.write;

import io.silverspoon.bulldog.core.pin.Pin;
import java.io.IOException;
import net.ProtocolManager;
import request.IllegalRequestException;
import request.manager.GpioManager;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class GpioWriteRequest implements WriteRequest {
    private final Pin pin;
    private final boolean desiredVoltage;

    public GpioWriteRequest(Pin pin, boolean desiredVoltage) {
        this.pin = pin;
        this.desiredVoltage = desiredVoltage;
    }

    public GpioWriteRequest(Pin pin) {
        this.pin = pin;
        try {
            this.desiredVoltage = !GpioManager.getInstance().getBooleanRead(pin);
        } catch (IllegalRequestException ex) {
            //something to write to logger
            throw new IllegalArgumentException("pin provided is not available on this board");
        }
    }

    @Override
    public void write() {
        try {
            GpioManager.getInstance().write(this.pin, desiredVoltage ? "1" : "0");
            //let's make sure the value is the one the client requested
            if(this.desiredVoltage != GpioManager.getInstance().getBooleanRead(pin)) {
                throw new IllegalStateException(
                    String.format("Pin %s has been written to, expected value %b, got %b",
                    this.pin.getName(), this.desiredVoltage, !this.desiredVoltage)
                );
            }
        } catch (IllegalRequestException ex) {
            //something to write to logger?
        }
    }

    /**
     * Sends message to client whether the provided Pin (specified in {@code GpioWriteRequest.getInstance(name)} by {@code name} argument)
     * is turned off or on.
     */
    @Override
    public void giveFeedbackToClient() throws IOException {
        ProtocolManager.getInstance().setMessageToSend(String.format(
                GpioManager.RESPONSE_FORMAT, this.desiredVoltage ? "HIGH" : "LOW", this.pin.getAddress(),
                this.pin.getIndexOnPort(),this.pin.getName()));
    }

}
