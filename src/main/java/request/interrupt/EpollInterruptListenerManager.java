package request.interrupt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.silverspoon.bulldog.linux.gpio.LinuxDigitalInput;
import java.util.HashMap;
import java.util.Map;

public final class EpollInterruptListenerManager implements InterruptListenerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(EpollInterruptListenerManager.class);
    private final Map<InterruptListenerArgs, LinuxDigitalInput> inputMap = new HashMap<>();
    private static final InterruptListenerManager INTR_MANAGER = new EpollInterruptListenerManager();

    private EpollInterruptListenerManager() {
    }

    public static InterruptListenerManager getInstance() {
        return INTR_MANAGER;
    }

    @Override
    public boolean registerInput(InterruptListenerArgs input) {
        if (inputMap.containsKey(input)) {
            LOGGER.error("Interrupt listener could not have been registered because it had been registered already.");
            return false;
        }
        LinuxDigitalInput newDigitalInput = new LinuxDigitalInput(input.getPin());
        if (!input.getPin().hasFeature(LinuxDigitalInput.class)) {
            newDigitalInput.setup();
            newDigitalInput.enableInterrupts();
            newDigitalInput.addInterruptListener(new LinuxEpollListenerImpl(input));
            input.getPin().addFeature(newDigitalInput);
            input.getPin().activateFeature(LinuxDigitalInput.class);
            newDigitalInput.activate();
        }
        inputMap.put(input, newDigitalInput);
        if (!newDigitalInput.areInterruptsEnabled()) {
            throw new AssertionError("iterrupts not enabled");
        }
        if (!newDigitalInput.isSetup()) {
            throw new AssertionError("not setup");
        }
        if (!input.getPin().isFeatureActive(newDigitalInput)) {
            throw new AssertionError("feature is not active");
        }
        if (!input.getPin().hasFeature(LinuxDigitalInput.class)) {
            throw new AssertionError("feature missing");
        }
        if (!newDigitalInput.isActivatedFeature()) {
            throw new AssertionError("feature not activated: " + newDigitalInput.getClass());
        }
        LOGGER.info(String.format(
                "New interrupt listener has been registered: GPIO : %s, interrupt type : %s",
                input.getPin().getName(),
                input.getEdge()));
        return true;
    }

    @Override
    public boolean deregisterInput(InterruptListenerArgs input) {
        if (!inputMap.containsKey(input)) {
            LinuxDigitalInput digitalInput = inputMap.get(input);
            digitalInput.clearInterruptListeners();
            inputMap.remove(input);
            LOGGER.info(String.format(
                    "Interrupt listener has been deregistered: GPIO : %s, interrupt type : %s",
                    input.getPin().getName(),
                    input.getEdge()));
            return true;
        } else {
            LOGGER.error("Interrupt listener could not have been deregistered because it had not been registered.");
            return false;
        }
    }
}
