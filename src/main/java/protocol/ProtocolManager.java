package protocol;

import board.test.BoardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.request.IllegalRequestException;
import protocol.request.Request;
import protocol.request.RequestUtils;

/**
 * Responsibilities: provide utility methods which are going to enable request
 * processing.
 */
public final class ProtocolManager {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(ProtocolManager.class);
    private final BoardManager boardManager;

    public ProtocolManager(BoardManager manager) {
        this.boardManager = manager;
    }

    /**
     * Takes message which was read from the input stream and tries to parse it.
     * If the String representing the current request is not valid,
     * IllegalRequestException is thrown.
     *
     */
    public Request parseRequest(String receivedMessage)
            throws IllegalRequestException {
        LOGGER.info(ProtocolMessages.REQUEST_CAPTURED + " " + receivedMessage);
        return RequestUtils.parse(boardManager, receivedMessage);
    }
}
