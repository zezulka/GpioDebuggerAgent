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

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.gpio.DigitalIO;
import mocks.MockedDigitalIoFeature;
import static org.assertj.core.api.Assertions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import request.interrupt.EpollInterruptListenerManager;
import request.interrupt.InterruptListenerArgs;
import request.interrupt.InterruptListenerManager;

/**
 *
 * @author Miloslav Zezulka
 */
public class EpollInterruptListenerManagerTest {

    private InterruptListenerManager MANAGER;

    @Before
    public void initialize() {
        MANAGER = EpollInterruptListenerManager.getInstance();
    }

    @After
    public void teardown() {
        MANAGER.clearAllListeners();
    }

    /**
     * Happy scenario test.
     */
    @Test
    public void deregisterExistingInterruptListener() {
        InterruptListenerArgs args = new InterruptListenerArgs(RequestParserUtils.REQUESTED_PIN.as(MockedDigitalIoFeature.class), Edge.Both);
        try {
            MANAGER.registerInput(args);
            MANAGER.deregisterInput(args);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Happy scenario test.
     */
    @Test
    public void deregisterExistingInterruptListenerTwice() {
        InterruptListenerArgs args = new InterruptListenerArgs(RequestParserUtils.REQUESTED_PIN.as(MockedDigitalIoFeature.class), Edge.Both);
        try {
            MANAGER.registerInput(args);
            MANAGER.deregisterInput(args);
            MANAGER.registerInput(args);
            MANAGER.deregisterInput(args);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }
    
    /**
     * Happy scenario test.
     */
    @Test
    public void registerInterruptListenerSamePinDifferentEdge() {
        InterruptListenerArgs argsFalling = new InterruptListenerArgs(RequestParserUtils.REQUESTED_PIN.as(MockedDigitalIoFeature.class), Edge.Falling);
        InterruptListenerArgs argsRising = new InterruptListenerArgs(RequestParserUtils.REQUESTED_PIN.as(MockedDigitalIoFeature.class), Edge.Rising);
        InterruptListenerArgs argsBoth = new InterruptListenerArgs(RequestParserUtils.REQUESTED_PIN.as(MockedDigitalIoFeature.class), Edge.Both);
        try {
            MANAGER.registerInput(argsFalling);
            MANAGER.registerInput(argsRising);
            MANAGER.registerInput(argsBoth);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Happy scenario test.
     */
    @Test
    public void registerInterruptListener() {
        InterruptListenerArgs args = new InterruptListenerArgs(RequestParserUtils.REQUESTED_PIN.as(MockedDigitalIoFeature.class), Edge.Both);
        try {
            MANAGER.registerInput(args);
            DigitalIO digIo = RequestParserUtils.REQUESTED_PIN.as(DigitalIO.class); 
            assertThat(digIo.areInterruptsEnabled()).isTrue();
            assertThat(digIo.isSetup()).isTrue();
            assertThat(digIo.isActivatedFeature()).isTrue();
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void deregisterNonexistingInterruptListener() {
        InterruptListenerArgs args = new InterruptListenerArgs(RequestParserUtils.REQUESTED_PIN.as(MockedDigitalIoFeature.class), Edge.Both);
        assertThatThrownBy(() -> MANAGER.deregisterInput(args)).
                isInstanceOf(IllegalRequestException.class);
    }
    
    @Test
    public void registerInterruptListenerTwice() {
        InterruptListenerArgs args = new InterruptListenerArgs(RequestParserUtils.REQUESTED_PIN.as(MockedDigitalIoFeature.class), Edge.Both);
        try {
            MANAGER.registerInput(args);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
        assertThatThrownBy(() -> MANAGER.registerInput(args)).
                isInstanceOf(IllegalRequestException.class);
    }
}
