package protocol;

import java.io.IOException;
import protocol.request.DeviceInterface;

import java.util.function.Function;
import net.ConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import protocol.request.IllegalRequestException;
import protocol.request.Request;
import protocol.request.RequestParser;

import protocol.request.manager.InterfaceManager;

/**
 * Responsibilities: provide utility methods which are going to enable request
 * processing.
 *
 */
public final class ProtocolManager {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(ProtocolManager.class);
    private final Function<DeviceInterface, InterfaceManager> converter;

    public ProtocolManager(
            Function<DeviceInterface, InterfaceManager> converter) {
        this.converter = converter;
    }

    /**
     * Takes message which was read from the input stream and tries to parse it.
     * If the String representing the current request is not valid,
     * IllegalRequestException is thrown. In case request is valid, request is
     * performed and appropriate response is sent back to client.
     *
     * @throws IOException
     */
    public void parseRequest(String receivedMessage)
            throws IllegalRequestException, IOException {
        LOGGER.info(ProtocolMessages.REQUEST_CAPTURED + " " + receivedMessage);
        Request req = RequestParser.parse(converter, receivedMessage);
        performRequest(req);
    }

    private void performRequest(Request request) throws IOException {
        LOGGER.info(String.format("%s has been submitted",
                request.getClass().getSimpleName()));
        request.performRequest();
        ConnectionManager.setMessageToSend(request.getFormattedResponse());
    }
}
