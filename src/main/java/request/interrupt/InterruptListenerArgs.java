package request.interrupt;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.pin.Pin;
import java.util.Objects;

/**
  * DVO class for dealing with interrupts.
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
            throw new IllegalArgumentException("Pin feature cannot be null");
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.pin.hashCode();
        hash = 53 * hash + this.edge.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InterruptListenerArgs other = (InterruptListenerArgs) obj;
        if (!Objects.equals(this.pin, other.pin)) {
            return false;
        }
        return this.edge.equals(other.edge);
    }
}
