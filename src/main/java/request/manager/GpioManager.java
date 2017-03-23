/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request.manager;

import core.DeviceManager;
import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.DigitalInput;
import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.pin.Pin;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class GpioManager {

    private static final DeviceManager DEVICE_MANAGER = DeviceManager.getInstance();
    
    private GpioManager() {}

    private static void applyVoltage(Signal sig, String pinName) {
        Pin pin = DEVICE_MANAGER.getPinFromName(pinName);
        if (pin == null) {
            throw new IllegalArgumentException(String.format(
                    "Pin with the descriptor '%s' has not been found.", pinName));
        }
        DigitalOutput output = pin.as(DigitalOutput.class);
        if (GpioManager.readVoltage(pinName) != sig.getBooleanValue()) {
            output.applySignal(sig);
        }

    }
    /**
     * Sets pin's voltage to high.
     *
     * @param pinName
     * @throws IllegalArgumentException if pin specified by {@code pinName} has
     * not been found.
     */
    public static void setHigh(String pinName) {
        applyVoltage(Signal.High, pinName);
    }

    /**
     * Sets pin's voltage to low.
     *
     * @param pinName
     */
    public static void setLow(String pinName) {
        applyVoltage(Signal.Low, pinName);
    }

    
    /**
     * Reads voltage from the pin specified by {@code pinName} argument. 
     * @param pinName
     * @return voltage level represented by boolean value 
     * {@code 1: Signal.HIGH, 0: Signal.LOW}
     */
    public static boolean readVoltage(String pinName) {
        return readVoltage(DEVICE_MANAGER.getPinFromName(pinName));
    }
    
    public static boolean readVoltage(Pin pin) {
        if(pin == null) {
            throw new IllegalArgumentException("pin cannot be null");
        }
        DigitalInput input = pin.as(DigitalInput.class);
        return input.read().getBooleanValue();
    }
}
