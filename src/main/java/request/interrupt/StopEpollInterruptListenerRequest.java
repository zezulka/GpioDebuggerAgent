package request.interrupt;

import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.IllegalRequestException;

public final class StopEpollInterruptListenerRequest
        extends AbstractEpollInterruptListenerRequest {

    private static final String PREFIX = "INTR_STOPPED";
    private static final Logger LOGGER
            = LoggerFactory.getLogger(StopEpollInterruptListenerRequest.class);
    private static final InterruptListenerManager MANAGER
            = EpollInterruptListenerManager.getInstance();

    public StopEpollInterruptListenerRequest(InterruptEventArgs arg) {
        super(arg);
    }

    @Override
    protected String getMessagePrefix() {
        return PREFIX;
    }

    @Override
    public void handleInterruptRequest() {
        try {
            MANAGER.deregisterListener(super.getArg());
        } catch (IllegalRequestException ex) {
            LOGGER.error(null, ex);
        }

    }
}
