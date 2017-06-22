package request.interrupt;

import io.silverspoon.bulldog.core.gpio.DigitalInput;
import io.silverspoon.bulldog.core.pin.Pin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import request.IllegalRequestException;

public final class EpollInterruptListenerManager implements InterruptListenerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(EpollInterruptListenerManager.class);
    private static final Map<InterruptListenerArgs, DigitalInput> INPUT_MAP = new HashMap<>();
    private static final InterruptListenerManager INTR_MANAGER = new EpollInterruptListenerManager();

    private EpollInterruptListenerManager() {
    }

    public static InterruptListenerManager getInstance() {
        return INTR_MANAGER;
    }

    @Override
    public void registerInput(InterruptListenerArgs input) throws IllegalRequestException {
        if (INPUT_MAP.containsKey(input)) {
            LOGGER.error("Interrupt listener could not have been registered because it had been registered already.");
            throw new IllegalRequestException("Interrupt listener could not have been registered because it had been registered already.");
        }
        DigitalInput newDigitalInput = input.getDigitalInput();
        Pin pin = newDigitalInput.getPin();
        if (!pin.hasFeature(DigitalInput.class)) {
            newDigitalInput.setup();
            newDigitalInput.enableInterrupts();
            newDigitalInput.addInterruptListener(new LinuxEpollListenerImpl(input));
            newDigitalInput.activate();
            pin.addFeature(newDigitalInput);
            pin.activateFeature(DigitalInput.class);  
        }
        INPUT_MAP.put(input, newDigitalInput);
        if (!newDigitalInput.areInterruptsEnabled()) {
            throw new AssertionError("iterrupts not enabled");
        }
        if (!newDigitalInput.isSetup()) {
            throw new AssertionError("not setup");
        }
        if (!pin.hasFeature(DigitalInput.class)) {
            throw new AssertionError("feature missing");
        }
        INPUT_MAP.put(input, newDigitalInput);
        LOGGER.info(String.format(
                "New interrupt listener has been registered: GPIO : %s, interrupt type : %s",
                pin.getName(),
                input.getEdge()));
    }

    @Override
    public void deregisterInput(InterruptListenerArgs args) throws IllegalRequestException {
        if (INPUT_MAP.containsKey(args)) {
            DigitalInput input = INPUT_MAP.get(args);
            input.clearInterruptListeners();
            INPUT_MAP.remove(args);
            LOGGER.info(String.format(
                    "Interrupt listener has been deregistered: GPIO : %s, interrupt type : %s",
                    input.getPin().getName(),
                    args.getEdge()));
        } else {
            LOGGER.error("Interrupt listener could not have been deregistered because it had not been registered.");
            throw new IllegalRequestException("Interrupt listener could not have been deregistered because it had not been registered.");
        }
    }

    @Override
    public void clearAllListeners() {
        INPUT_MAP.values().forEach((input) -> input.clearInterruptListeners());
        INPUT_MAP.clear();
    }
}
