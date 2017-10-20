package core;

import java.io.IOException;
import java.net.ServerSocket;
import net.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class, which represents an entry point for the whole application. Only
 * one instance can be run at a time.
 *
 */
public final class Agent {

    private static final Logger LOGGER = LoggerFactory.getLogger(Agent.class);
    private static final int SERVER_SOCKET_PORT = 12345;

    private Agent() {
    }

    public static void main(String[] args) {
        try {
            //Checks for already running instance of agent.
            ServerSocket s = new ServerSocket(SERVER_SOCKET_PORT);
            new Thread(ConnectionManager.getManagerWithDefaultPort()).start();
        } catch (IOException ex) {
            LOGGER.error("Application already running!");
            System.exit(1);
        }
    }
}
