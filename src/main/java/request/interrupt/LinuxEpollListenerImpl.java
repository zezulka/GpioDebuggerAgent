package request.interrupt;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.event.InterruptListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LinuxEpollListenerImpl
        extends AbstractEpollInterruptListenerRequest
        implements InterruptListener {

    private static final String PREFIX = "INTR_GENERATED";
    private static final Logger LOGGER
            = LoggerFactory.getLogger(LinuxEpollListenerImpl.class);

    @Override
    protected String getMessagePrefix() {
        return PREFIX;
    }

    public LinuxEpollListenerImpl(InterruptEventArgs arg) {
        super(arg);
    }

    @Override
    public void performRequest() {
        /**
         * NO-OP: this is not a client generated request, there is nothing to
         * do, only send message to client that the event happened
         */
    }

    @Override
    public void interruptRequest(InterruptEventArgs iea) {
        if (!shouldBeEventProcessed(iea)) {
            return;
        }
        LOGGER.debug(String.format("interrupt edge %s", iea.getEdge()));
        super.getFormattedResponse();
    }

    private boolean shouldBeEventProcessed(InterruptEventArgs input) {
        InterruptEventArgs regArg = getArg();
        Edge registeredEdge = regArg.getEdge();
        boolean registeredForBoth = registeredEdge.equals(Edge.Both);
        boolean filterSuccessful = registeredForBoth || registeredEdge
                .equals(input.getEdge());

        return filterSuccessful;
    }
}
