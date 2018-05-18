package core;

import net.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Main class, which represents an entry point for the whole application. Only
 * one instance can be run at a time.
 */
public final class Agent {

    private static final Logger LOGGER = LoggerFactory.getLogger(Agent.class);

    private Agent() {
    }

    public static void main(String[] args) {
        boolean ok;
        try {
            ok = new RandomAccessFile("/tmp/lock", "rw").
                    getChannel().tryLock() != null;
        } catch (IOException ex) {
            ok = false;
        }
        if (ok) {
            new Thread(ConnectionManager.getInstance()).start();
        } else {
            LOGGER.error("Application already running!");
            System.exit(1);
        }
    }
}
