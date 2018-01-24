package protocol.request.interrupt;

import io.silverspoon.bulldog.core.Edge;
import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.event.InterruptListener;
import net.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.request.StringConstants;

public final class LinuxEpollListenerImpl
        extends AbstractEpollInterruptListenerRequest
        implements InterruptListener {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(LinuxEpollListenerImpl.class);

    @Override
    protected String getMessageFormatter() {
        return StringConstants.GENERATED_INTR_RESPONSE_FORMAT;
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
        /**
         * Classes in this package should not invoke setMessageToSend, but
         * method of this class gets invoked asynchronously outside of the
         * message parser cycle (see ProtocolManager.performRequest for more
         * details). In general, interruptRequest method gets invoked outside of
         * the main thread, so additional threading needn't be dealt with.
         *
         */
        ConnectionManager.setMessage(super.getFormattedResponse());
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
