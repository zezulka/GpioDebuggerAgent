package board;

import board.test.BoardManager;
import board.test.BulldogBoardManager;
import board.test.TestingBoardManager;
import io.silverspoon.bulldog.linux.sysinfo.CpuInfo;

public final class BoardManagerFactory {

    private BoardManagerFactory() {
    }

    public static BoardManager getInstance() {
        // This is pretty much one of the few signs we have from bulldog
        // that the agent is not being run on the target device
        if (CpuInfo.getHardware() == null) {
            return new TestingBoardManager();
        } else {
            return new BulldogBoardManager();
        }
    }

}
