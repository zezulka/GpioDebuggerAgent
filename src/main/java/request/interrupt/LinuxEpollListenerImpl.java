package request.interrupt;

import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.event.InterruptListener;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinuxEpollListenerImpl extends AbstractEpollInterruptListenerRequest
                                    implements InterruptListener {

    private static final String PREFIX = "INTR_GENERATED";
    private static final Logger LOGGER = LoggerFactory.getLogger(LinuxEpollListenerImpl.class);

    protected String getMessagePrefix() {
        return PREFIX;
    }

    public LinuxEpollListenerImpl(InterruptListenerArgs arg) {
        super(arg);
    }

    @Override
    public void handleInterruptRequest() {
    }

    @Override
    public void interruptRequest(InterruptEventArgs iea) {
        try{
            System.out.println(iea.getEdge() + "   ASDFASDAFDFASDFAD");
            super.giveFeedbackToClient();
        } catch(IOException ex) {
            LOGGER.error(null, ex);
        }
    }
}
