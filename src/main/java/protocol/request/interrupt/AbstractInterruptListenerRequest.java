package protocol.request.interrupt;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.event.InterruptListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.request.BulldogRequestUtils;
import protocol.request.Request;

import java.time.LocalTime;
import java.util.Objects;

/**
 * Abstract class roofing all classes dealing with interrupt listeners.
 */
public abstract class AbstractInterruptListenerRequest
        implements Request, InterruptListener {

    private final InterruptEventArgs arg;
    private static final Logger LOGGER = LoggerFactory.getLogger(
            AbstractInterruptListenerRequest.class);

    /**
     * @throws IllegalArgumentException args is null
     */
    public AbstractInterruptListenerRequest(InterruptEventArgs arg) {
        Objects.requireNonNull(arg, "arg");
        this.arg = arg;
    }

    /**
     * .
     * Message contains pin on which the interrupt was registered. In its
     * prefix, information about whether the interrupt listener has been
     * (de)registered or generated, is contained.
     */
    @Override
    public final String responseString() {
        String response = String.format(getMessageFormatter(),
                arg.getPin().getName(), arg.getEdge(),
                LocalTime.now().format(BulldogRequestUtils.FORMATTER));
        LOGGER.info(String.format("sent to client: %s", response));
        return response;
    }

    @Override
    public void interruptRequest(InterruptEventArgs iea) {
        if (!shouldBeEventProcessed(iea)) {
            return;
        }
        LOGGER.debug(String.format("interrupt edge %s", iea.getEdge()));
        interruptRequestImpl(iea);
    }

    /*
     Extending classes can override this method but calling this method
     is a NO-OP by default.
     */
    protected void interruptRequestImpl(InterruptEventArgs iea) {
    }

    private boolean shouldBeEventProcessed(InterruptEventArgs input) {
        Edge registeredEdge = arg.getEdge();
        boolean registeredForBoth = registeredEdge.equals(Edge.Both);
        return registeredForBoth || registeredEdge.equals(input.getEdge());
    }

    protected final InterruptEventArgs getArg() {
        return this.arg;
    }

    protected abstract String getMessageFormatter();
}
