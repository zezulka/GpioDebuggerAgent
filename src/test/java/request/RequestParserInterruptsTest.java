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
package request;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

import request.interrupt.*;
/**
 *
 * @author Miloslav Zezulka
 */
public class RequestParserInterruptsTest {

    @Test
    public void registerInterrupt() {
        try {
            Request gpioRead = RequestParser.parse(RequestParserUtils.CONVERTER, "GPIO:INTR_START:" + RequestParserUtils.REQUESTED_PIN_NAME + " FALLING");
            assertThat(gpioRead.getClass()).isEqualTo(StartEpollInterruptListenerRequest.class);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void deregisterInterrupt() {
        try {
            Request gpioRead = RequestParser.parse(RequestParserUtils.CONVERTER, "GPIO:INTR_STOP:" + RequestParserUtils.REQUESTED_PIN_NAME + " BOTH");
            assertThat(gpioRead.getClass()).isEqualTo(StopEpollInterruptListenerRequest.class);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void erroneousOperation() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "GPIO:INTR____:" + RequestParserUtils.REQUESTED_PIN_NAME + " FALLING")).
                isInstanceOf(IllegalRequestException.class);
    }
    
    @Test
    public void erroneousEdge() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "GPIO:INTR_STOP:" + RequestParserUtils.REQUESTED_PIN_NAME + " FAILING")).
                isInstanceOf(IllegalRequestException.class);
    }
    
    @Test
    public void erroneousPin() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "GPIO:INTR_STOP:123 FALLING")).
                isInstanceOf(IllegalRequestException.class);
    }
}
