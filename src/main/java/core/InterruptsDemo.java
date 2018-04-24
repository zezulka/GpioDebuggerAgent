package core;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.event.InterruptListener;
import io.silverspoon.bulldog.core.gpio.DigitalInput;
import io.silverspoon.bulldog.core.gpio.base.DigitalIOFeature;
import io.silverspoon.bulldog.core.platform.Board;
import io.silverspoon.bulldog.core.platform.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.request.IllegalRequestException;
import protocol.request.interrupt.AbstractInterruptListenerRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
 * Playground class for examining behavior of the Bulldog interrupt API.
 */
public final class InterruptsDemo {
    public static final Board BOARD = Platform.createBoard();
    private static final Logger LOGGER = LoggerFactory.getLogger(
            InterruptsDemo.class);
    private static final Map<InterruptEventArgs, DigitalInput> LISTENER_MAP
            = new HashMap<>();
    private static final int WAIT_PERIOD = 10;

    private InterruptsDemo() {
    }

    public static void main(String[] args) {
        LOGGER.error("Started.");
        try {
            InterruptEventArgs iea = new InterruptEventArgs(
                    BOARD.getPin(3), Edge.Falling);
            LOGGER.error("got InterruptEventArgs object");
            registerListener(iea, new DemoInterruptListener(iea));
            LOGGER.error("Registered listener on the pin "
                    + iea.getPin().getName());
            TimeUnit.SECONDS.sleep(WAIT_PERIOD);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    public static void registerListener(InterruptEventArgs input,
                                        InterruptListener il)
            throws IllegalRequestException {
        if (anySuitableListenerActive(input)) {
            LOGGER.error("Interrupt listener could not have been registered"
                    + " because it had been registered already.");
            throw new IllegalRequestException("Interrupt listener could not "
                    + "have been registered because "
                    + "it had been registered already.");
        }
        DigitalIOFeature diof = input.getPin()
                .getFeature(DigitalIOFeature.class);
        diof.addInterruptListener(il);
        diof.activate();
        diof.enableInterrupts();
        LOGGER.info(String.format(
                "Interrupt listener registered: pin: %s, interrupt type: %s",
                input.getPin().getName(),
                input.getEdge()));
    }

    public static void deregisterListener(InterruptEventArgs args)
            throws IllegalRequestException {
        if (anySuitableListenerActive(args)) {
            DigitalInput input = LISTENER_MAP.get(args);
            input.clearInterruptListeners();
            LISTENER_MAP.remove(args);
            LOGGER.info(String.format("Interrupt listener deregistered:"
                            + " pin: %s, interrupt type: %s",
                    input.getPin().getName(),
                    args.getEdge()));
        } else {
            LOGGER.error("Interrupt listener could not have been deregistered "
                    + "because it had not been registered.");
            throw new IllegalRequestException("Interrupt listener could not"
                    + " have been deregistered because "
                    + "it had not been registered.");
        }
    }

    private static boolean anySuitableListenerActive(InterruptEventArgs input) {
        if (input == null) {
            return false;
        }
        if (LISTENER_MAP.containsKey(input)) {
            return true;
        }

        for (InterruptEventArgs iea : LISTENER_MAP.keySet()) {
            if (iea.getEdge().equals(Edge.Both)
                    && iea.getPin().equals(input.getPin())) {
                return true;
            }
        }
        return false;
    }

    public static void clearAllListeners() {
        LISTENER_MAP.values().forEach(DigitalInput::clearInterruptListeners);
        LISTENER_MAP.clear();
    }

    public static class DemoInterruptListener
            extends AbstractInterruptListenerRequest {

        /**
         * @param arg
         * @throws IllegalArgumentException args is null
         */
        public DemoInterruptListener(InterruptEventArgs arg) {
            super(arg);
        }

        @Override
        protected String getMessageFormatter() {
            return null;
        }

        @Override
        public void action() {

        }

        @Override
        protected void interruptRequestImpl(InterruptEventArgs iea) {
            LOGGER.error(iea.toString());
        }
    }
}
