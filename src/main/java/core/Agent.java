package core;

import net.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationProperties;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Main class, which represents an entry point for the whole application. Only
 * one instance can be run at a time.
 *
 */
public final class Agent {

    private static final Logger LOGGER = LoggerFactory.getLogger(Agent.class);

    private Agent() {
    }

    public static void main(String[] args) {
        try {
            //Checks for already running instance of agent.
            new ServerSocket(ApplicationProperties.lockPort());
            new Thread(ConnectionManager.getDefaultManager()).start();
        } catch (IOException ex) {
            LOGGER.error("Application already running!");
            System.exit(1);
        }
    }
}
