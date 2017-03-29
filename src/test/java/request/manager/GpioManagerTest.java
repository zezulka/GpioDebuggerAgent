///*
// * Copyright 2017 Miloslav.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package request.manager;
//
//import core.DeviceManager;
//import io.silverspoon.bulldog.core.mocks.MockedBoard;
//import io.silverspoon.bulldog.core.pin.Pin;
//import io.silverspoon.bulldog.core.platform.Board;
//import io.silverspoon.bulldog.raspberrypi.RaspiNames;
//import java.lang.reflect.Field;
//import java.lang.reflect.Modifier;
//import org.junit.Before;
//import org.junit.Test;
//import static org.assertj.core.api.Assertions.*;
//
///**
// *
// * @author Miloslav Zezulka, 2017
// */
//public class GpioManagerTest {
//    private final Pin pinName = DeviceManager.getInstance().getPin(RaspiNames.P1_3);
//    
//    @Before
//    public void setUp() {
//        Board board = new MockedBoard();
//    }
//
//    @Test
//    public void testReadPin() {
//        GpioManager.setHigh(pinName);
//        assertThat(GpioManager.readVoltage(pinName)).isTrue();
//    }
//
//    @Test
//    public void testReadInvalidPin() {
//        assertThatThrownBy(() -> GpioManager.readVoltage(null)).isInstanceOf(IllegalArgumentException.class);
//    }
//    
//    @Test
//    public void testSetHigh() {
//        GpioManager.setHigh(pinName);
//        assertThat(GpioManager.readVoltage(pinName)).isTrue();
//    }
//
//    @Test
//    public void testSetLow() {
//        GpioManager.setLow(pinName);
//        assertThat(GpioManager.readVoltage(pinName)).isFalse();
//    }
//
//    @Test
//    public void testSetHighApplyTwice() {
//        GpioManager.setHigh(pinName);
//        GpioManager.setHigh(pinName);
//        assertThat(GpioManager.readVoltage(pinName)).isTrue();
//    }
//    
//    @Test
//    public void testSetToggle() {
//        GpioManager.setHigh(pinName);
//        assertThat(GpioManager.readVoltage(pinName)).isTrue();
//        GpioManager.setLow(pinName);
//        assertThat(GpioManager.readVoltage(pinName)).isFalse();
//    }
//}
