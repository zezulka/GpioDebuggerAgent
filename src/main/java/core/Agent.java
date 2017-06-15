package core;

import java.io.IOException;
import net.AgentConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ProgramUtils;

/**
 * Main class, which represents an entry point for the whole application.
 * Only one instance can be run at a time.
 *
 * @author Miloslav Zezulka, 2017
 */
public class Agent {

    private static final Logger LOGGER = LoggerFactory.getLogger(Agent.class);

    private Agent() {
    }

    public static void main(String[] args) {
        try {
            if(ProgramUtils.checkIfAlreadyRunning()) {
                final String alreadyExists = "program stopped prematurely because one instance of the program already exists";
                LOGGER.info(alreadyExists);
                System.exit(1);
                return;
            }
        } catch (IOException ex) {
            final String appNotStarted = "could not start application: ";
            LOGGER.error(appNotStarted, ex);
            System.exit(1);
            return;
        }
        new Thread(AgentConnectionManager.getManagerWithDefaultPort()).start();
    }
}
