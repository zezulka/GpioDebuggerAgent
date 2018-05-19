package core;

import net.ConnectionManager;

/**
 * Main class, which represents an entry point for the whole application. Only
 * one instance can be run at a time.
 */
public final class Agent {
    private Agent() {
    }

    public static void main(String[] args) {
        new Thread(ConnectionManager.getInstance()).start();
    }
}
