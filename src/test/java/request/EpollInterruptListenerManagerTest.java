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
import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.gpio.DigitalInput;
import static org.assertj.core.api.Assertions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import request.interrupt.EpollInterruptListenerManager;
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
        InterruptEventArgs args = new InterruptEventArgs(RequestParserUtils.REQUESTED_PIN, Edge.Both);
        try {
            MANAGER.registerListener(args);
            MANAGER.deregisterListener(args);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Happy scenario test.
     */
    @Test
    @Ignore
    public void deregisterExistingInterruptListenerTwice() {
        InterruptEventArgs args = new InterruptEventArgs(RequestParserUtils.REQUESTED_PIN, Edge.Both);
        try {
            MANAGER.registerListener(args);
            MANAGER.deregisterListener(args);
            MANAGER.registerListener(args);
            MANAGER.deregisterListener(args);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Happy scenario test.
     */
    @Test
    @Ignore
    public void registerInterruptListenerSamePinDifferentEdge() {
        InterruptEventArgs argsFalling = new InterruptEventArgs(RequestParserUtils.REQUESTED_PIN, Edge.Falling);
        InterruptEventArgs argsRising = new InterruptEventArgs(RequestParserUtils.REQUESTED_PIN, Edge.Rising);
        InterruptEventArgs argsBoth = new InterruptEventArgs(RequestParserUtils.REQUESTED_PIN, Edge.Both);
        try {
            MANAGER.registerListener(argsFalling);
            MANAGER.registerListener(argsRising);
            MANAGER.registerListener(argsBoth);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Happy scenario test.
     */
    @Test
    @Ignore
    public void registerInterruptListener() {
        InterruptEventArgs args = new InterruptEventArgs(RequestParserUtils.REQUESTED_PIN, Edge.Both);
        try {
            MANAGER.registerListener(args);
            DigitalInput digIo = RequestParserUtils.REQUESTED_PIN.as(DigitalInput.class);
            assertThat(digIo.areInterruptsEnabled()).isTrue();
            assertThat(digIo.isSetup()).isTrue();
            assertThat(digIo.isActivatedFeature()).isTrue();
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @Ignore
    public void deregisterNonexistingInterruptListener() {
        InterruptEventArgs args = new InterruptEventArgs(RequestParserUtils.REQUESTED_PIN, Edge.Both);
        assertThatThrownBy(() -> MANAGER.deregisterListener(args)).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    @Ignore
    public void registerInterruptListenerTwice() {
        InterruptEventArgs args = new InterruptEventArgs(RequestParserUtils.REQUESTED_PIN, Edge.Both);
        try {
            MANAGER.registerListener(args);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
        assertThatThrownBy(() -> MANAGER.registerListener(args)).
                isInstanceOf(IllegalRequestException.class);
    }
}
