package request.interrupt;

import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.gpio.DigitalInput;
import io.silverspoon.bulldog.linux.gpio.LinuxDigitalInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import request.IllegalRequestException;

public final class EpollInterruptListenerManager
        implements InterruptListenerManager {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(EpollInterruptListenerManager.class);

    private static final Map<InterruptEventArgs, DigitalInput> LISTENER_MAP
            = new HashMap<>();

    private static final InterruptListenerManager INTR_MANAGER
            = new EpollInterruptListenerManager();

    private EpollInterruptListenerManager() {
    }

    public static InterruptListenerManager getInstance() {
        return INTR_MANAGER;
    }

    @Override
    public void registerListener(InterruptEventArgs input)
            throws IllegalRequestException {
        if (anySuitableListenerActive(input)) {
            LOGGER.error("Interrupt listener could not have been registered"
                    + " because it had been registered already.");
            throw new IllegalRequestException("Interrupt listener could not "
                    + "have been registered because "
                    + "it had been registered already.");
        }
        //assignment to the same class (and not interface type) is here
        //intentional, activating this feature as DigitalInput breaks the
        //functionality
        LinuxDigitalInput newDigitalInput
                = new LinuxDigitalInput(input.getPin());
        newDigitalInput.setup();
        newDigitalInput.clearInterruptListeners();
        newDigitalInput.enableInterrupts();
        newDigitalInput.addInterruptListener(new LinuxEpollListenerImpl(input));
        if (!input.getPin().hasFeature(LinuxDigitalInput.class)) {
            input.getPin().addFeature(newDigitalInput);
        }
        input.getPin().activateFeature(LinuxDigitalInput.class);
        newDigitalInput.activate();
        LISTENER_MAP.put(input, newDigitalInput);
        deregisterListener(input);

        LinuxDigitalInput anotherDigIn
                = new LinuxDigitalInput(input.getPin());
        anotherDigIn.setup();
        anotherDigIn.clearInterruptListeners();
        anotherDigIn.enableInterrupts();
        anotherDigIn.addInterruptListener(new LinuxEpollListenerImpl(input));
        if (!input.getPin().hasFeature(LinuxDigitalInput.class)) {
            input.getPin().addFeature(anotherDigIn);
        }
        input.getPin().activateFeature(LinuxDigitalInput.class);
        anotherDigIn.activate();

        LISTENER_MAP.put(input, anotherDigIn);
        LOGGER.info(String.format(
                "Interrupt listener registered: pin: %s, interrupt type: %s",
                input.getPin().getName(),
                input.getEdge()));
    }

    @Override
    public void deregisterListener(InterruptEventArgs args)
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

    public static boolean anySuitableListenerActive(InterruptEventArgs input) {
        if (input == null) {
            return false;
        }
        return LISTENER_MAP.containsKey(input);
    }

    @Override
    public void clearAllListeners() {
        LISTENER_MAP.values().forEach((val) -> val.clearInterruptListeners());
        LISTENER_MAP.clear();
    }
}
