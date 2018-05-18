/*
 * Copyright 2017 miloslav.
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
package protocol.request;

import org.junit.Test;
import protocol.request.interrupt.StartInterruptListenerRequest;
import protocol.request.interrupt.StopInterruptListenerRequest;

import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Miloslav Zezulka
 */
public class RequestUtilsInterruptsTest {

    @Test
    public void registerInterrupt() {
        try {
            Request gpioRead = RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "GPIO:INTR_START:" + RequestParserUtils.REQUESTED_PIN_NAME + " FALLING");
            assertThat(gpioRead.getClass()).isEqualTo(StartInterruptListenerRequest.class);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void deregisterInterrupt() {
        try {
            Request gpioRead = RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "GPIO:INTR_STOP:" + RequestParserUtils.REQUESTED_PIN_NAME + " BOTH");
            assertThat(gpioRead.getClass()).isEqualTo(StopInterruptListenerRequest.class);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void erroneousOperation() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "GPIO:INTR____:" + RequestParserUtils.REQUESTED_PIN_NAME + " FALLING")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void erroneousEdge() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "GPIO:INTR_STOP:" + RequestParserUtils.REQUESTED_PIN_NAME + " FAILING")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void erroneousPin() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "GPIO:INTR_STOP:123 FALLING")).
                isInstanceOf(IllegalRequestException.class);
    }
}
