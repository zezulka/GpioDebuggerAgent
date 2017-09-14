package request.interrupt;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import java.time.LocalTime;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.BulldogRequestUtils;
import request.Request;

/**
 * Abstract class roofing all classes dealing with interrupt listeners.
 *
 * @author Miloslav Zezulka
 */
public abstract class AbstractEpollInterruptListenerRequest
        implements Request {

    private final InterruptEventArgs arg;
    private boolean triggeredByBothEdgeListener = false;
    private Edge bothEdgeListenerEdge;
    private static final Logger LOGGER
            = LoggerFactory
                    .getLogger(AbstractEpollInterruptListenerRequest.class);

    /**
     * @throws IllegalArgumentException args is null
     */
    public AbstractEpollInterruptListenerRequest(InterruptEventArgs arg) {
        Objects.requireNonNull(arg, "arg");
        this.arg = arg;
    }

    /**
     * .
     * Message contains pin on which the interrupt was registered. In its
     * prefix, information about whether the interrupt listener has been
     * (de)registered or generated, is contained.
     *
     */
    @Override
    public final String getFormattedResponse() {
        String response = String.format(getMessageFormatter(),
                arg.getPin().getName(), (triggeredByBothEdgeListener
                ? bothEdgeListenerEdge : arg.getEdge()),
                LocalTime.now().format(BulldogRequestUtils.FORMATTER));
        triggeredByBothEdgeListener = false;
        LOGGER.info(String
                .format("sent to client: %s", response));
        return response;
    }

    protected final void setEdge(Edge edge) {
        this.bothEdgeListenerEdge = edge;
        this.triggeredByBothEdgeListener = true;
    }

    protected final InterruptEventArgs getArg() {
        return this.arg;
    }

    protected abstract String getMessageFormatter();
}
