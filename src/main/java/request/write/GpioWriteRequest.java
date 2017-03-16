/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request.write;

import java.io.IOException;
import net.ConnectionManager;
import request.manager.GpioManager;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class GpioWriteRequest implements WriteRequest {
    private final String pinName;
    
    private GpioWriteRequest(String pinName) {
        this.pinName = pinName;
    }
    
    public static GpioWriteRequest getInstance(String name) {
        return new GpioWriteRequest(name);
    }

    /**
     * Toggles the current value.
     */
    @Override
    public void write() {
        if(GpioManager.readVoltage(this.pinName)) {
            GpioManager.setLow(this.pinName);
        } else {
            GpioManager.setHigh(this.pinName);
        }
    }

    /**
     * Sends message to client whether the provided Pin (specified in {@code GpioWriteRequest.getInstance(name)} by {@code name} argument)
     * is turned off or on.
     */
    @Override
    public void giveFeedbackToClient() throws IOException {
        ConnectionManager.getManagerWithDefaultPort().setMessage(String.format("The pin %s is now %s", 
                this.pinName, GpioManager.readVoltage(this.pinName) ? "on" : "off"));
    }
    
}
