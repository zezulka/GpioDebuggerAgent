package core;

import io.silverspoon.bulldog.core.platform.Board;
import io.silverspoon.bulldog.core.platform.Platform;
import net.ConnectionManager;

/**
 * Main class, which represents an entry point for the whole application.
 * @author Miloslav Zezulka, 2017
 */
public class Agent {
    public final static Board BOARD = Platform.createBoard();
    public final static ConnectionManager CM = ConnectionManager.getManagerWithDefaultPort();
    
    private Agent() {
    }
    
    public static void main(String[] args) {
        new Thread(CM).start();
//        try {
//            Thread.sleep(180 * 1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
