package net;

import board.manager.BoardManager;
import board.manager.BulldogBoardManager;
import board.manager.TestingBoardManager;
import io.silverspoon.bulldog.linux.sysinfo.CpuInfo;

final class BoardManagerFactory {

    private BoardManagerFactory() {
    }

    static BoardManager getInstance() {
        // This is pretty much one of the few signs we have from bulldog
        // that the agent is not being run on the target device
        if (CpuInfo.getHardware() == null) {
            return new TestingBoardManager();
        } else {
            return new BulldogBoardManager();
        }
    }

}
