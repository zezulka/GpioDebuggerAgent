package request;

import board.test.BoardManager;
import mocks.MockedDeviceManager;
import mocks.MockedInitMessage;
import org.junit.Before;
import org.junit.Test;
import protocol.request.Request;
import protocol.request.StringConstants;

import static org.assertj.core.api.Assertions.assertThat;

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
        String expected = StringConstants.INIT_PREFIX + manager.getBoardName() + StringConstants.REQ_SEPARATOR + "GPIO INTERRUPTS";
        Request req = new MockedInitMessage(manager);
        assertThat(req.responseString()).startsWith(expected); // in case root runs this
    }

}
