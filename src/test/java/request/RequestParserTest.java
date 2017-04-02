package request;

import io.silverspoon.bulldog.raspberrypi.RaspiNames;
import mocks.MockedBoard;
import mocks.MockedDeviceManager;

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
public class RequestParserTest {

    private final String requestedPin = MockedDeviceManager.getPin(0).getName();
    
    public RequestParserTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Happy scenario test.
     */
    @Test
    public void gpioTest() {
        Request gpioRead, gpioWrite;
        try {
            gpioRead = RequestParser.parse("gpio: read:" + requestedPin);
            gpioWrite = RequestParser.parse("gpio:write:" + requestedPin);

            assertThat(gpioRead.getClass()).isEqualTo(GpioReadRequest.class);
            assertThat(gpioWrite.getClass()).isEqualTo(GpioWriteRequest.class);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void nullTest() throws Exception {
        assertThatThrownBy(() -> RequestParser.parse(null)).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void gpioWriteWithDesiredVoltageFail() {
        assertThatThrownBy(
                () -> RequestParser.parse("gpio: write:" + requestedPin + ":42"))
                .isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void nonsenseRequestTestEmptyString() throws Exception {
        assertThatThrownBy(() -> RequestParser.parse("")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void nonsenseRequestTestEmptyRequest() throws Exception {
        assertThatThrownBy(() -> RequestParser.parse("::")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void nonsenseRequestTestWrongOrderOfArgs() {
        assertThatThrownBy(() -> RequestParser.parse("read:gpio:14")).
                isInstanceOf(IllegalRequestException.class);
    }

}
