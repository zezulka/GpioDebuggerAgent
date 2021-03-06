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

import bulldog.TestInterruptListener;
import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.event.InterruptListener;
import io.silverspoon.bulldog.core.gpio.DigitalInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import protocol.request.interrupt.AgentInterruptListenerManager;
import protocol.request.interrupt.InterruptListenerManager;

import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Miloslav Zezulka
 */
public class AgentInterruptListenerManagerTest {

    private InterruptListenerManager MANAGER;

    @Before
    public void initialize() {
        MANAGER = AgentInterruptListenerManager.getInstance();
    }

    @After
    public void teardown() {
        MANAGER.clearAllListeners();
    }

    @Test
    public void interruptListenerEvents() {
        InterruptEventArgs args = new InterruptEventArgs(RequestParserUtils.REQUESTED_PIN, Edge.Rising);
        InterruptListener ail = new TestInterruptListener(args);
        ail.interruptRequest(new InterruptEventArgs(RequestParserUtils.REQUESTED_PIN, Edge.Falling));
    }

    /**
     * Happy scenario test.
     */
    @Test
    @Ignore
    public void deregisterExistingInterruptListener() {
        InterruptEventArgs args = new InterruptEventArgs(RequestParserUtils.REQUESTED_PIN, Edge.Both);
        try {
            MANAGER.registerListener(args, new TestInterruptListener(args));
            MANAGER.deregisterListener(args, new TestInterruptListener(args));
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
            MANAGER.registerListener(args, new TestInterruptListener(args));
            MANAGER.deregisterListener(args, new TestInterruptListener(args));
            MANAGER.registerListener(args, new TestInterruptListener(args));
            MANAGER.deregisterListener(args, new TestInterruptListener(args));
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
            MANAGER.registerListener(argsFalling, new TestInterruptListener(argsFalling));
            MANAGER.registerListener(argsRising, new TestInterruptListener(argsRising));
            MANAGER.registerListener(argsBoth, new TestInterruptListener(argsBoth));
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
            MANAGER.registerListener(args, new TestInterruptListener(args));
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
        assertThatThrownBy(() -> MANAGER.deregisterListener(args, new TestInterruptListener(args))).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    @Ignore
    public void registerInterruptListenerTwice() {
        InterruptEventArgs args = new InterruptEventArgs(RequestParserUtils.REQUESTED_PIN, Edge.Both);
        try {
            MANAGER.registerListener(args, new TestInterruptListener(args));
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
        assertThatThrownBy(() -> MANAGER.registerListener(args, new TestInterruptListener(args))).
                isInstanceOf(IllegalRequestException.class);
    }
}
