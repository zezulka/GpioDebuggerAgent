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
import request.IllegalRequestException;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class GpioManager {

    private GpioManager() {
    }

    private static void applyVoltage(Signal sig, Pin pin) {
        DigitalOutput output = pin.as(DigitalOutput.class);
        if (GpioManager.readVoltage(pin) != sig.getBooleanValue()) {
            output.applySignal(sig);
        }
    }
    
    public static Pin getPinFromName(String name) throws IllegalRequestException {
        Pin pin =  DeviceManager.getInstance().getPin(name);
        if(pin == null) {
            throw new IllegalRequestException("pin " + name + " has not been found on this board");
        }
        return pin;
    }

    /**
     * Sets pin's voltage to high.
     *
     * @param pin
     * @throws IllegalArgumentException if pin specified by {@code pinName} has
     * not been found.
     */
    public static void setHigh(Pin pin) {
        applyVoltage(Signal.High, pin);
    }

    /**
     * Sets pin's voltage to low.
     *
     * @param pin
     */
    public static void setLow(Pin pin) {
        applyVoltage(Signal.Low, pin);
    }

    /**
     * Reads voltage from the pin specified by {@code pin} argument.
     *
     * @param pin
     * @return voltage level represented by boolean value
     * {@code 1: Signal.HIGH, 0: Signal.LOW}
     */
    public static boolean readVoltage(Pin pin) {
        if (pin == null) {
            throw new IllegalArgumentException("pin cannot be null");
        }
        DigitalInput input = pin.as(DigitalInput.class);
        return input.read().getBooleanValue();
    }
}
