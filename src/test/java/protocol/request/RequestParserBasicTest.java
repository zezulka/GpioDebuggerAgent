package protocol.request;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class RequestParserBasicTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void nullTest() throws Exception {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, null)).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void emptyString() throws Exception {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void emptyRequest() throws Exception {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "::")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void illegalSeparators() throws Exception {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "gpio;read;" + RequestParserUtils.REQUESTED_PIN_NAME)).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void nonexistingInterface() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "ggppiioo:read:" + RequestParserUtils.REQUESTED_PIN_NAME)).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void nonexistingOperation() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "gpio:noop:" + RequestParserUtils.REQUESTED_PIN_NAME)).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void wrongOrderOfArgs() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "read:gpio:14")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void missingOperation() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "gpio::" + RequestParserUtils.REQUESTED_PIN_NAME)).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void missingInterface() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, ":write:" + RequestParserUtils.REQUESTED_PIN_NAME)).
                isInstanceOf(IllegalRequestException.class);
    }
}
