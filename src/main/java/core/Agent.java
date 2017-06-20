package core;

import java.io.IOException;
import java.net.ServerSocket;
import net.AgentConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class, which represents an entry point for the whole application. Only
 * one instance can be run at a time.
 *
 * @author Miloslav Zezulka, 2017
 */
public class Agent {

    private static final Logger LOGGER = LoggerFactory.getLogger(Agent.class);

    private Agent() {
    }

    public static void main(String[] args) {
        try {
            ServerSocket s = new ServerSocket(12345);
            new Thread(AgentConnectionManager.getManagerWithDefaultPort()).start();
        } catch (IOException ex) {
            LOGGER.error("Application already running!");
            System.exit(1);
        }
    }
}
