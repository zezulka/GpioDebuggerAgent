package request.interrupt;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.event.InterruptListener;

import java.io.IOException;

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
    public void handleInterruptRequest() {
        /**
         * NO-OP: this is not a client generated request, there is nothing to
         * parse, only send message to client that the event happened
         */
    }

    @Override
    public void interruptRequest(InterruptEventArgs iea) {
        if (!shouldBeEventProcessed(iea)) {
            LOGGER.debug(String.format("interrupt edge was %s, skipping...",
                    iea.getEdge()));
            return;
        }
        try {
            LOGGER.debug(String.format("interrupt edge was %s", iea.getEdge()));
            super.giveFeedbackToClient();
        } catch (IOException ex) {
            LOGGER.error(null, ex);
        }
    }

    private boolean shouldBeEventProcessed(InterruptEventArgs input) {
        InterruptEventArgs regArg = getArg();
        boolean isRegisteredForBoth = regArg.getEdge().equals(Edge.Both)
                && regArg.getPin().equals(input.getPin());
        return EpollInterruptListenerManager.anySuitableListenerActive(input)
                && (isRegisteredForBoth || regArg.equals(input));
    }
}
