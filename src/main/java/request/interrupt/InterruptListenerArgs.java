package request.interrupt;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.gpio.DigitalIO;

/**
  * DVO class for dealing with interrupts.
  *
  */
public class InterruptListenerArgs {
    
    private final DigitalIO digitalIoFeature;
    private final Edge edge;

    /**
     * @throws IllegalArgumentException pin is null
     */
    public InterruptListenerArgs(DigitalIO pin, Edge edge) {
        if(pin == null) {
            throw new IllegalArgumentException("Pin featurescannot be null");
        }
        this.digitalIoFeature = pin;
        this.edge = edge;
    }

    public DigitalIO getDigitalIoFeature() {
        return this.digitalIoFeature;
    }

    public Edge getEdge() {
        return this.edge;
    }
}
