package request;

import board.manager.BoardManager;
import mocks.MockedDeviceManager;
import mocks.MockedInitMessage;
import org.junit.Test;
import org.junit.Before;
import static org.assertj.core.api.Assertions.*;

public class InitMessageTest {

    private static BoardManager manager;

    public InitMessageTest() {
    }

    @Before
    public void init() {
        manager = new MockedDeviceManager();
    }

    @Test
    public void initMessage() {
        String expected = StringConstants.INIT_PREFIX + manager.getBoardName() + StringConstants.REQ_SEPARATOR + "GPIO ";
        Request req = new MockedInitMessage(manager);
        assertThat(req.getFormattedResponse()).isEqualTo(expected);
    }

}
