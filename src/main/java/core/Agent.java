package core;

import net.AgentConnectionManager;

/**
 * Main class, which represents an entry point for the whole application.
 *
 * @author Miloslav Zezulka, 2017
 */
public class Agent {

    private Agent() {
    }

    public static void main(String[] args) {
        new Thread(AgentConnectionManager.getManagerWithDefaultPort()).start();
    }
}
