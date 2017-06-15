package request.read;

import io.silverspoon.bulldog.core.pin.Pin;

import java.io.IOException;

import net.AgentConnectionManager;

import request.IllegalRequestException;
import request.StringConstants;

import request.manager.GpioManager;


/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class GpioReadRequest implements ReadRequest {

    private final Pin pin;
    private final GpioManager gpioManager;

    public GpioReadRequest(GpioManager gpioManager, String pinName) {
        this.gpioManager = gpioManager;
        this.pin = this.gpioManager.getPin(pinName);
        if(pin == null) {
            throw new IllegalArgumentException("pin with the given name has not been found");
        }
    }

    /**
     * Attempts to read input denoted in getInstance() method and if successful,
     * returns the pin numeric value as a result. String containing number is
     * returned due to interface which this class implements.
     * @return String numeric representation of the read signal
     */
    @Override
    public String read() {
        try {
            return String.valueOf(gpioManager.read(this.pin).getNumericValue());
        } catch (IllegalRequestException ex) {
            return "-1";
        }
    }

    @Override
    public void giveFeedbackToClient() throws IOException {
        int status = Integer.parseInt(read());
        String voltageLvl = "N/A";
        if(status == 0) {
            voltageLvl = "LOW";
        } else {
            voltageLvl = "HIGH";
        }
        AgentConnectionManager.setMessageToSend(String.format(
                StringConstants.GPIO_RESPONSE_FORMAT.toString(), voltageLvl, this.pin.getAddress(),
                this.pin.getIndexOnPort(),this.pin.getName()));
    }
}
