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

    private EpollInterruptListenerManager() {}

    public static InterruptListenerManager getInstance() {
        return INTR_MANAGER;
    }

    @Override
    public boolean registerInputs(InterruptListenerArgs input) {
        LinuxDigitalInput newDigitalInput = new LinuxDigitalInput(input.getPin());
        newDigitalInput.setInterruptTrigger(input.getEdge());
        if (inputMap.containsKey(input)) {
            LOGGER.error("Interrupt listener could not have been registered because it had been registered already.");
            return false;
        }
        inputMap.put(input, newDigitalInput);
        LOGGER.info(String.format(
                "New interrupt listener has been registered: GPIO : %s, interrupt type : %s",
                input.getPin().getName(),
                input.getEdge()));
        return true;
    }

    @Override
    public boolean deregisterInputs(InterruptListenerArgs input) {
        if (!inputMap.containsKey(input)) {
            LinuxDigitalInput digitalInput = inputMap.get(input);
            //digitalInput.removeInterruptListener(...);
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
