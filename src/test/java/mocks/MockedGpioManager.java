/*
 * Copyright 2017 Miloslav.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mocks;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.pin.Pin;
import request.IllegalRequestException;
import request.manager.InterfaceManager;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class MockedGpioManager implements InterfaceManager {

    private static final String ON = "1";
    private static final String OFF = "0";
    private static final MockedGpioManager INSTANCE = new MockedGpioManager();
    
    private MockedGpioManager() {
    }
    
    public static MockedGpioManager getInstance() {
        return INSTANCE;
    }

    private static void applyVoltage(Signal sig, Pin pin) {
        pin.getFeature(MockedDigitalIoFeature.class).write(sig);
        assert(pin.getFeature(MockedDigitalIoFeature.class).read()).equals(sig);
    }

    /**
     * Reads voltage from the pin specified by {@code pin} argument.
     *
     * @param descriptor
     * @return voltage level represented by boolean value
     * {@code 1: Signal.HIGH, 0: Signal.LOW}
     * @throws request.IllegalRequestException
     */
    @Override
    public String read(String descriptor) throws IllegalRequestException {
        if (descriptor == null) {
            throw new IllegalRequestException("descriptor cannot be null");
        }
        Pin pin = MockedDeviceManager.getPin(descriptor);
        if (pin == null) {
            throw new IllegalRequestException("descriptor does not have any mapping to it on this board");
        }
        MockedDigitalIoFeature feat = pin.getFeature(MockedDigitalIoFeature.class);
        feat.unblockPin();
        return Integer.toString(feat.read().getNumericValue());
    }

    /**
     *
     * @param descriptor
     * @param message
     * @throws IllegalRequestException
     */
    @Override
    public void write(String descriptor, String message) throws IllegalRequestException {
        Pin pin = MockedDeviceManager.getPin(descriptor);
        if (pin == null) {
            throw new IllegalRequestException("descriptor does not have any mapping to it on this board");
        }
        Signal sig;
        try {
            if(message.equals(ON)) {
                sig = Signal.High;
            } else if(message.equals(OFF)) {
                sig = Signal.Low;
            } else {
                throw new IllegalRequestException("value for voltage not valid");
            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalRequestException(ex);
        }
        applyVoltage(sig, pin);
    }
}
