package request.interrupt;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.gpio.DigitalInput;

/**
  * DVO class for dealing with interrupts.
  *
  */
public class InterruptListenerArgs {
    
    private final DigitalInput digitalInput;
    private final Edge edge;

    /**
     * @throws IllegalArgumentException pin is null
     */
    public InterruptListenerArgs(DigitalInput pin, Edge edge) {
        if(pin == null) {
            throw new IllegalArgumentException("Pin featurescannot be null");
        }
        if(!(pin instanceof DigitalInput)) {
            throw new IllegalArgumentException("Pin feature must implement DigitaInput interface.");
        }
        this.digitalInput = pin;
        this.edge = edge;
    }

    public DigitalInput getDigitalInput() {
        return this.digitalInput;
    }

    public Edge getEdge() {
        return this.edge;
    }
}
