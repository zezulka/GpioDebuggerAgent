package board;

import board.test.BoardManager;
import board.test.BulldogBoardManager;
import board.test.TestingBoardManager;
import io.silverspoon.bulldog.linux.sysinfo.CpuInfo;
import util.OsUtil;

public final class BoardManagerFactory {

    private BoardManagerFactory() {
    }

    public static BoardManager getInstance() {
        // This is pretty much one of the few signs we have from bulldog
        // that the agent is not being run on the target device
        //
        // Bulldog does not support Windows, so run it in testing
        // mode instead
        if (OsUtil.isWindows() || CpuInfo.getHardware() == null) {
            return new TestingBoardManager();
        } else {
            return new BulldogBoardManager();
        }
    }

}
