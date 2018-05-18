package protocol.request;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class RequestUtilsBasicTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void nullTest() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, null)).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void emptyString() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void emptyRequest() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "::")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void illegalSeparators() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "gpio;read;" + RequestParserUtils.REQUESTED_PIN_NAME)).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void nonexistingInterface() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "ggppiioo:read:" + RequestParserUtils.REQUESTED_PIN_NAME)).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void nonexistingOperation() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "gpio:noop:" + RequestParserUtils.REQUESTED_PIN_NAME)).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void wrongOrderOfArgs() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "read:gpio:14")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void missingOperation() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "gpio::" + RequestParserUtils.REQUESTED_PIN_NAME)).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void missingInterface() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, ":write:" + RequestParserUtils.REQUESTED_PIN_NAME)).
                isInstanceOf(IllegalRequestException.class);
    }
}
