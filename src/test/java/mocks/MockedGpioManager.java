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

import request.manager.BoardManager;
import request.manager.GpioManager;
/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class MockedGpioManager implements GpioManager {
    private static final BoardManager MOCKED_MANAGER = MockedDeviceManager.getInstance();
    private static final MockedGpioManager INSTANCE = new MockedGpioManager();

    private MockedGpioManager() {
    }

    public static MockedGpioManager getInstance() {
        return INSTANCE;
    }

    private void applyVoltage(Signal sig, Pin pin) {
        pin.getFeature(MockedDigitalIoFeature.class).write(sig);
        assert(pin.getFeature(MockedDigitalIoFeature.class).read()).equals(sig);
    }

    /**
     * Reads voltage from the pin specified by {@code pin} argument.
     *
     * @return voltage level represented by boolean value
     * {@code 1: Signal.HIGH, 0: Signal.LOW}
     * @throws request.IllegalRequestException
     */
    @Override
    public Signal read(Pin pin) throws IllegalRequestException {
        if (pin == null) {
            throw new IllegalRequestException("pin cannot be null");
        }
        MockedDigitalIoFeature feat = pin.getFeature(MockedDigitalIoFeature.class);
        feat.unblockPin();
        return feat.read();
    }

    /**
     *
     * @param descriptor
     * @param message
     * @throws IllegalRequestException
     */
    @Override
    public void write(Pin pin, Signal sig) throws IllegalRequestException {
        if (pin == null) {
            throw new IllegalRequestException("pin cannot be null");
        }
        if(sig == null) {
            throw new IllegalRequestException("signal cannot be null");
        }
        applyVoltage(sig, pin);
    }

    @Override
    public Pin getPin(String pinName) {
        return MOCKED_MANAGER.getBoard().getPin(pinName);
    }
}
