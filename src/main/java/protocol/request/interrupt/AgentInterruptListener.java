package protocol.request.interrupt;

import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import net.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.request.StringConstants;

public final class AgentInterruptListener
        extends AbstractInterruptListenerRequest {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(AgentInterruptListener.class);

    public AgentInterruptListener(InterruptEventArgs arg) {
        super(arg);
    }

    @Override
    protected String getMessageFormatter() {
        return StringConstants.GENERATED_INTR_RESPONSE_FORMAT;
    }

    @Override
    public void action() {
        /**
         * NO-OP: this is not a client generated request, there is nothing to
         * do, only send message to client that the event happened
         */
    }

    @Override
    protected void interruptRequestImpl(InterruptEventArgs iea) {
        /**
         * Classes in this package should not invoke setMessageToSend, but
         * method of this class gets invoked asynchronously outside of the
         * message parser cycle (see ProtocolManager.action for more
         * details). In general, interruptRequest method gets invoked outside of
         * the main thread, so additional threading needn't be dealt with.
         *
         */
        ConnectionManager.getInstance().sendMessage(super.responseString());
    }
}
