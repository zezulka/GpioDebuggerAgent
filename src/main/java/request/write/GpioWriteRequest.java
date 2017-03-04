/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request.write;

import java.util.List;
import net.ConnectionManager;
import request.Interface;
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

    @Override
    public void write() {
        if(GpioManager.readVoltage(this.pinName)) {
            GpioManager.setLow(this.pinName);
        } else {
            GpioManager.setHigh(this.pinName);
        }
    }

    @Override
    public void giveFeedbackToClient() {
        ConnectionManager.getOutput().println(String.format("The pin %s is now %s", 
                this.pinName, GpioManager.readVoltage(this.pinName) ? "on" : "off"));
    }
    
}
