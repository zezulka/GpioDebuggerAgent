package protocol.request;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import protocol.request.read.GpioReadRequest;
import protocol.request.write.GpioWriteRequest;

import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class RequestUtilsGpioTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void nonexistingPin() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "read:gpio:" + Integer.MAX_VALUE)).
                isInstanceOf(IllegalRequestException.class);
    }

    /**
     * Happy scenario GPIO test.
     */
    @Test
    public void gpioWriteTest() {
        try {
            Request gpioWrite = RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "gpio:write:" + RequestParserUtils.REQUESTED_PIN_NAME);
            assertThat(gpioWrite.getClass()).isEqualTo(GpioWriteRequest.class);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Happy scenario GPIO test.
     */
    @Test
    public void gpioReadTest() {
        try {
            Request gpioRead = RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "gpio:read:" + RequestParserUtils.REQUESTED_PIN_NAME);
            assertThat(gpioRead.getClass()).isEqualTo(GpioReadRequest.class);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }
}
