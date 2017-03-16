/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request.manager;

import core.Agent;
import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.DigitalInput;
import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.pin.Pin;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class GpioManager {

    private GpioManager() {
        if (Agent.BOARD == null) {
            throw new IllegalStateException("Board has not been instantiated but client attempted to make a request.");
        }
    }

    private static void applyVoltage(Signal sig, String pinName) {
        Pin pin = Agent.BOARD.getPin(pinName);
        if (pin == null) {
            throw new IllegalArgumentException(String.format(
                    "Pin with the descriptor '%s' has not been found.", pinName));
        }
        DigitalOutput output = Agent.BOARD.getPin(pinName).as(DigitalOutput.class);
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

    public static boolean readVoltage(String pinName) {
        DigitalInput input = Agent.BOARD.getPin(pinName).as(DigitalInput.class);
        return input.read().getBooleanValue();
    }
}
