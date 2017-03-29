package request.write;

import io.silverspoon.bulldog.core.pin.Pin;
import java.io.IOException;
import net.ProtocolManager;
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
        this(pin, !GpioManager.readVoltage(pin));
    }
    
    /**
     * Toggles the current value.
     */
    @Override
    public void write() {
        if(desiredVoltage) {
            GpioManager.setHigh(this.pin);
        } else {
            GpioManager.setLow(this.pin);
        }
    }

    /**
     * Sends message to client whether the provided Pin (specified in {@code GpioWriteRequest.getInstance(name)} by {@code name} argument)
     * is turned off or on.
     */
    @Override
    public void giveFeedbackToClient() throws IOException {
        ProtocolManager.getInstance().setMessageToSend(String.format("The pin %s is now %s", 
                this.pin.getName(), GpioManager.readVoltage(this.pin) ? "on" : "off"));
    }
    
}
