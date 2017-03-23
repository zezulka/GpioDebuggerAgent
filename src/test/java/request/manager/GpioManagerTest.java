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
package request.manager;

import io.silverspoon.bulldog.core.mocks.MockedBoard;
import io.silverspoon.bulldog.core.mocks.MockedDigitalInput;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.platform.Board;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class GpioManagerTest {

    private Board boardMock;

    @Before
    public void setUp() {
        boardMock = new MockedBoard();
    }

    @Test
    public void testReadPin() {
        int index = 0;
        Pin pin = boardMock.getPin(index);
        
        MockedDigitalInput di = new MockedDigitalInput(pin);
        assertThat(GpioManager.readVoltage(pin)).isEqualTo(di.read().getBooleanValue());
    }

    @Test
    public void testReadInvalidPin() {
        assertThatThrownBy(() -> GpioManager.readVoltage("NONEXISTENT")).isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testSetHigh() {

        Pin pin = boardMock.getPin(0);
        GpioManager.setHigh(pin.getName());
        assertThat(GpioManager.readVoltage(pin)).isTrue();
    }

    @Test
    public void testSetLow() {
        Pin pin = boardMock.getPin(0);
        GpioManager.setLow(pin.getName());
        assertThat(GpioManager.readVoltage(pin)).isFalse();
    }

    @Test
    public void testSetHighApplyTwice() {
        Pin pin = boardMock.getPin(0);
        GpioManager.setHigh(pin.getName());
        GpioManager.setHigh(pin.getName());
        assertThat(GpioManager.readVoltage(pin)).isTrue();
    }
    
    @Test
    public void testSetToggle() {
        Pin pin = boardMock.getPin(0);
        GpioManager.setHigh(pin.getName());
        assertThat(GpioManager.readVoltage(pin)).isTrue();
        GpioManager.setLow(pin.getName());
        assertThat(GpioManager.readVoltage(pin.getName())).isFalse();
    }
}
