package protocol.request.interrupt;

import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import io.silverspoon.bulldog.core.event.InterruptListener;
import protocol.request.IllegalRequestException;

public interface InterruptListenerManager {

    /**
     * @throws IllegalRequestException listener has been already registered
     * @throws IllegalArgumentException input is null
     */
    void registerListener(InterruptEventArgs input, InterruptListener il)
            throws IllegalRequestException;

    /**
     * @throws IllegalRequestException no such interrupt listener exists
     * @throws IllegalArgumentException input is null
     */
    void deregisterListener(InterruptEventArgs input, InterruptListener il)
            throws IllegalRequestException;

    /**
     * Convenient method for deregistering all available interrupt listeners.
     */
    void clearAllListeners();
}
