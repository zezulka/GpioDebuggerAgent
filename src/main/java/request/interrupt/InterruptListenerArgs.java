package request.interrupt;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.pin.Pin;

/**
  * Placeholder class for dealing with interrupts.
  *
  */
public class InterruptListenerArgs {
    private final Pin pin;
    private final Edge edge;

    /**
     * @throws IllegalArgumentException pin is null
     */
    public InterruptListenerArgs(Pin pin, Edge edge) {
        if(pin == null) {
            throw new IllegalArgumentException("Pin cannot be null");
        }
        this.pin = pin;
        this.edge = edge;
    }

    public Pin getPin() {
        return this.pin;
    }

    public Edge getEdge() {
        return this.edge;
    }
}
