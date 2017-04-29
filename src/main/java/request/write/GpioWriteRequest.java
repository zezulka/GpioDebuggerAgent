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
            this.desiredVoltage = !GpioManager.getInstance().getBooleanRead(pin.getName());
        } catch (IllegalRequestException ex) {
            //something to write to logger
            throw new IllegalArgumentException("pin provided is not available on this board");
        }
    }

    @Override
    public void write() {
        try {
            GpioManager.getInstance().write(pin.getName(), desiredVoltage ? "1" : "0");
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
        ProtocolManager.getInstance().setMessageToSend(String.format(GpioManager.RESPONSE_FORMAT,
                this.pin.getName(), this.desiredVoltage ? "HIGH" : "LOW"));
    }

}
