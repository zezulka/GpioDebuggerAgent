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

import io.silverspoon.bulldog.core.pin.Pin;
import mocks.MockedDeviceManager;
import mocks.MockedGpioManager;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import request.IllegalRequestException;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class GpioManagerTest {
    private static final Pin PIN = MockedDeviceManager.getPin(0);
    private static final String PIN_NAME = PIN.getName();
    private final MockedGpioManager manager = MockedGpioManager.getInstance();
    
    @Before
    public void setUp() {
    }   
    
    @Test
    public void testWriteNegativeVoltage() {
        assertThatThrownBy(() -> manager.write(PIN_NAME, "A")).isInstanceOf(IllegalRequestException.class);
    }
    
    @Test
    public void testNonexistentPin() {
        assertThatThrownBy(() -> manager.read("PIN")).isInstanceOf(IllegalRequestException.class);
    }
    
    @Test
    public void testSetHigh() throws Exception {
        manager.write(PIN_NAME, "1");
        assertThat(manager.read(PIN_NAME)).isEqualTo("1");
    }
    
    @Test
    public void testSetLow() throws Exception {
        manager.write(PIN_NAME, "0");
        assertThat(manager.read(PIN_NAME)).isEqualTo("0");
    }

    @Test
    public void testSetHighApplyTwice() throws Exception {
        manager.write(PIN_NAME, "1");
        manager.write(PIN_NAME, "1");
        assertThat(manager.read(PIN_NAME)).isEqualTo("1");
    }
    
    @Test
    public void testSetToggle() throws Exception {
        manager.write(PIN_NAME, "1");
        assertThat(manager.read(PIN_NAME)).isEqualTo("1");
        manager.write(PIN_NAME, "0");
        assertThat(manager.read(PIN_NAME)).isEqualTo("0");
    }
}
