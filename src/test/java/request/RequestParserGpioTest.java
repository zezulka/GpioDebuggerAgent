package request;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import request.read.GpioReadRequest;
import request.write.GpioWriteRequest;

import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class RequestParserGpioTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void nonexistingPin() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "read:gpio:" + Integer.MAX_VALUE)).
                isInstanceOf(IllegalRequestException.class);
    }

    /**
     * Happy scenario GPIO test.
     */
    @Test
    public void gpioWriteTest() {
        try {
            Request gpioWrite = RequestParser.parse(RequestParserUtils.CONVERTER, "gpio:write:" + RequestParserUtils.REQUESTED_PIN_NAME);
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
            Request gpioRead = RequestParser.parse(RequestParserUtils.CONVERTER, "gpio:read:" + RequestParserUtils.REQUESTED_PIN_NAME);
            assertThat(gpioRead.getClass()).isEqualTo(GpioReadRequest.class);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }
}
