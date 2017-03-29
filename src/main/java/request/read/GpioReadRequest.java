/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request.read;
import core.DeviceManager;
import io.silverspoon.bulldog.core.pin.Pin;
import java.io.IOException;
import net.ProtocolManager;
import request.manager.GpioManager;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class GpioReadRequest implements ReadRequest {
    private final Pin pin;
    
    public GpioReadRequest(Pin pin) {
        this.pin = pin;
    }

    /**
     * Attempts to read input denoted in getInstance() method and if successful, 
     * returns the pin numeric value as a result. String containing number is
     * returned due to interface which this class implements. 
     * @return String numeric representation of the read signal
     */
    @Override
    public String read() {
        return GpioManager.readVoltage(this.pin) ? "1" : "0";
    }

    @Override
    public void giveFeedbackToClient() throws IOException {
        ProtocolManager.getInstance().setMessageToSend(String.format(
                "Pin '%s' is currently %s", this.pin.getName(), 
                Integer.parseInt(read()) == 0 ? "off" : "on"));
    }
}
