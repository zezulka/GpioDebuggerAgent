package request.interrupt;

import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import net.ConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class roofing all classes dealing with interrupt listeners.
 *
 * @author Miloslav Zezulka
 */
public abstract class AbstractEpollInterruptListenerRequest
        implements InterruptListenerRequest {

    private final InterruptEventArgs arg;
    private static final Logger LOGGER
            = LoggerFactory
                    .getLogger(AbstractEpollInterruptListenerRequest.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("HH.mm.ss.S");

    /**
     * @throws IllegalArgumentException args is null
     */
    public AbstractEpollInterruptListenerRequest(InterruptEventArgs arg) {
        if (arg == null) {
            throw new IllegalArgumentException("Args cannot be null");
        }
        this.arg = arg;
    }

    /**
     * .
     * Message contains pin on which the interrupt was registered. In its
     * prefix, information about whether the interrupt listener has been
     * (de)registered or generated, is contained.
     *
     * @throws IOException
     */
    @Override
    public final void giveFeedbackToClient() throws IOException {
        String response = getMessagePrefix()
                + ':'
                + arg.getPin().getName()
                + ':' + arg.getEdge()
                + ':'
                + LocalTime.now().format(FORMATTER)
                + '\n';
        LOGGER.info(String
                .format("[Interrupt listeners] sent to client: %s", response));
        ConnectionManager.setMessageToSend(response);
    }

    protected final InterruptEventArgs getArg() {
        return this.arg;
    }

    protected abstract String getMessagePrefix();
}
