package net;

import board.BoardManagerFactory;
import board.test.BoardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQException;
import protocol.Feature;
import protocol.ProtocolManager;
import protocol.ProtocolMessages;
import protocol.request.IllegalRequestException;
import protocol.request.Request;
import util.ApplicationProperties;
import util.Os;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import java.util.Objects;

/**
 * Responsibility: manage all the connections binded to the device.
 */
public final class ConnectionManager implements Runnable {
    private static final ConnectionManager INSTANCE = new ConnectionManager();
    private static final BoardManager BOARD_MANAGER
            = BoardManagerFactory.getInstance();
    private static final ProtocolManager PROTOCOL_MANAGER
            = new ProtocolManager(BOARD_MANAGER);
    private static final Logger LOGGER
            = LoggerFactory.getLogger(ConnectionManager.class);
    private int port;
    private final Context zmqContext = ZMQ.context(1);
    private final Socket zmqSocket = zmqContext.socket(ZMQ.REP);

    private ConnectionManager() {
        this.port = ApplicationProperties.socketPort();
    }

    public static ConnectionManager getInstance() {
        return INSTANCE;
    }

    public void sendMessage(String message) {
        Objects.requireNonNull(message, "message");
        zmqSocket.send(message.getBytes(), 0);
    }

    /**
     * Runs in an infinite loop, therefore the only way to stop this thread from
     * running is to kill the whole process.
     */
    @Override
    public void run() {
        try {
            zmqSocket.bind("tcp://*:" + port);
        } catch (ZMQException zmq) {
            LOGGER.error(String.format("The port associated with the agent (%d)"
                    + " is already in use. This either means that the agent is "
                    + "already running or another application is already using "
                    + "this resource.", port));
            System.exit(1);
        }
        LOGGER.info("Launched.");
        LOGGER.info("Available features:");
        for (Feature f : Os.getAppFeatures()) {
            LOGGER.info("\t" + f.toString());
        }
        while (true) {
            while (!Thread.currentThread().isInterrupted()) {
                String request = new String(zmqSocket.recv(0));
                LOGGER.info(String.format("Received a msg '%s'.", request));
                try {
                    Request req = PROTOCOL_MANAGER.parseRequest(request);
                    LOGGER.info(ProtocolMessages.REQUEST_OK.toString());
                    req.action();
                    sendMessage(req.responseString());
                } catch (IllegalRequestException ex) {
                    LOGGER.error(null, ex);
                    sendMessage(ProtocolMessages.ILLEGAL_REQUEST.toString());
                }
            }
        }
    }
}
