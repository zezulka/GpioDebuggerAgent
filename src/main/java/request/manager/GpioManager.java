/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request.manager;

import core.Agent;
import io.silverspoon.bulldog.core.gpio.DigitalInput;
import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import net.ConnectionManager;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class GpioManager {

    private GpioManager() {}
    
    public static void setHigh(String pinName) {
        DigitalOutput output = Agent.BOARD.getPin(pinName).as(DigitalOutput.class);
        output.high();
    }
    
    public static void setLow(String pinName) {
        DigitalOutput output = Agent.BOARD.getPin(pinName).as(DigitalOutput.class);
        output.high();
    }
    
    public static boolean readVoltage(String pinName) {
        DigitalInput input = Agent.BOARD.getPin(pinName).as(DigitalInput.class);
        return input.read().getBooleanValue();
    }
}
