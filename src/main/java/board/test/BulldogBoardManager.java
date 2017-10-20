package board.test;

import io.silverspoon.bulldog.core.platform.Platform;

/**
 * Class which takes care of communicating with the device itself, namely using
 * bulldog library (for more information about the library, please see
 * <a href="https://github.com/SilverThings/bulldog" target="_blank">Bulldog
 * github repository</a>).
 *
 */
public final class BulldogBoardManager extends AbstractBoardManager {

    public BulldogBoardManager() {
        super(Platform.createBoard());
    }
}
