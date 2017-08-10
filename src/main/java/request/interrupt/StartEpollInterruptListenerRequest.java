package request.interrupt;

import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.IllegalRequestException;

public final class StartEpollInterruptListenerRequest extends AbstractEpollInterruptListenerRequest {

    private static final String PREFIX = "INTR_STARTED";
    private static final Logger LOGGER = LoggerFactory.getLogger(StartEpollInterruptListenerRequest.class);
    private static final InterruptListenerManager MANAGER = EpollInterruptListenerManager.getInstance();

    public StartEpollInterruptListenerRequest(InterruptEventArgs arg) {
        super(arg);
    }

    @Override
    protected String getMessagePrefix() {
        return PREFIX;
    }

    @Override
    public void handleInterruptRequest() {
        try {
            MANAGER.registerListener(super.getArg());
        } catch (IllegalRequestException ex) {
            LOGGER.error(null, ex);
        }
    }
}
