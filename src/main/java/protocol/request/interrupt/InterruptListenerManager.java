package protocol.request.interrupt;

import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import protocol.request.IllegalRequestException;

public interface InterruptListenerManager {

    /**
     * Registers new interrupt listener.
     *
     * @param input
     * @throws IllegalRequestException listener has been already registered
     * @throws IllegalArgumentException input is null
     */
    void registerListener(InterruptEventArgs input)
            throws IllegalRequestException;

    /**
     * Deregisters already existing listener.
     *
     * @param input
     * @throws IllegalRequestException no such interrupt listener exists
     * @throws IllegalArgumentException input is null
     */
    void deregisterListener(InterruptEventArgs input)
            throws IllegalRequestException;

    /**
     * Convenient method for deregistering all available interrupt listeners.
     */
    void clearAllListeners();
}
