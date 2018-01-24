package protocol.request.interrupt;

import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.request.IllegalRequestException;
import protocol.request.StringConstants;

public final class StartEpollInterruptListenerRequest
        extends AbstractEpollInterruptListenerRequest {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(StartEpollInterruptListenerRequest.class);
    private static final InterruptListenerManager MANAGER
            = EpollInterruptListenerManager.getInstance();

    StartEpollInterruptListenerRequest(InterruptEventArgs arg) {
        super(arg);
    }

    @Override
    protected String getMessageFormatter() {
        return StringConstants.START_INTRS_RESPONSE_FORMAT;
    }

    @Override
    public void action() {
        try {
            MANAGER.registerListener(super.getArg());
        } catch (IllegalRequestException ex) {
            LOGGER.error(null, ex);
        }
    }
}
