/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request.write;

import java.io.IOException;
import net.ProtocolManager;
import request.manager.GpioManager;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class GpioWriteRequest implements WriteRequest {
    private final String pinName;
    private final boolean desiredVoltage;
    
    private GpioWriteRequest(String pinName, boolean desiredVoltage) {
        this.pinName = pinName;
        this.desiredVoltage = desiredVoltage;
    }
    
    private GpioWriteRequest(String pinName) {
        this(pinName, !GpioManager.readVoltage(pinName));
    }
    
    public static GpioWriteRequest getInstanceImplicitVoltage(String name) {
        return new GpioWriteRequest(name);
    }

    public static GpioWriteRequest getInstanceExplicitVoltage(String name, boolean desiredVoltage) {
        return new GpioWriteRequest(name, desiredVoltage);
    }
    
    /**
     * Toggles the current value.
     */
    @Override
    public void write() {
        if(desiredVoltage) {
            GpioManager.setHigh(this.pinName);
        } else {
            GpioManager.setLow(this.pinName);
        }
    }

    /**
     * Sends message to client whether the provided Pin (specified in {@code GpioWriteRequest.getInstance(name)} by {@code name} argument)
     * is turned off or on.
     */
    @Override
    public void giveFeedbackToClient() throws IOException {
        ProtocolManager.getInstance().setMessageToSend(String.format("The pin %s is now %s", 
                this.pinName, GpioManager.readVoltage(this.pinName) ? "on" : "off"));
    }
    
}
