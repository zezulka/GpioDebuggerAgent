package protocol.request.interrupt;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.event.InterruptListener;
import io.silverspoon.bulldog.core.gpio.DigitalInput;
import io.silverspoon.bulldog.core.gpio.base.DigitalIOFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.request.IllegalRequestException;

import java.util.HashMap;
import java.util.Map;

public final class AgentInterruptListenerManager
        implements InterruptListenerManager {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(AgentInterruptListenerManager.class);

    private static final Map<InterruptEventArgs, DigitalInput> LISTENER_MAP
            = new HashMap<>();

    private static final InterruptListenerManager INTR_MANAGER
            = new AgentInterruptListenerManager();

    private AgentInterruptListenerManager() {
    }

    public static InterruptListenerManager getInstance() {
        return INTR_MANAGER;
    }

    @Override
    public void registerListener(InterruptEventArgs input,
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
                input.getPin().getName(), input.getEdge()));
    }

    @Override
    public void deregisterListener(InterruptEventArgs args,
                                   InterruptListener il)
            throws IllegalRequestException {
        if (anySuitableListenerActive(args)) {
            DigitalInput input = LISTENER_MAP.get(args);
            input.clearInterruptListeners();
            LISTENER_MAP.remove(args);
            LOGGER.info(String.format("Interrupt listener deregistered:"
                            + " pin: %s, interrupt type: %s",
                    input.getPin().getName(), args.getEdge()));
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

    @Override
    public void clearAllListeners() {
        LISTENER_MAP.values().forEach(DigitalInput::clearInterruptListeners);
        LISTENER_MAP.clear();
    }
}
