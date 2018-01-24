package protocol.request.interrupt;

import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.request.IllegalRequestException;
import protocol.request.StringConstants;

public final class StopEpollInterruptListenerRequest
        extends AbstractEpollInterruptListenerRequest {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(StopEpollInterruptListenerRequest.class);
    private static final InterruptListenerManager MANAGER
            = EpollInterruptListenerManager.getInstance();

    StopEpollInterruptListenerRequest(InterruptEventArgs arg) {
        super(arg);
    }

    @Override
    protected String getMessageFormatter() {
        return StringConstants.STOP_INTRS_RESPONSE_FORMAT;
    }

    @Override
    public void action() {
        try {
            MANAGER.deregisterListener(super.getArg());
        } catch (IllegalRequestException ex) {
            LOGGER.error(null, ex);
        }

    }
}
