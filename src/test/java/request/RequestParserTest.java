package request;

import java.util.function.Function;
import mocks.MockedDeviceManager;
import mocks.MockedGpioManager;

import request.manager.BoardManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import request.read.GpioReadRequest;
import request.write.GpioWriteRequest;

import static org.assertj.core.api.Assertions.*;

import request.manager.GpioManager;
import request.manager.GpioManagerBulldogImpl;
import request.manager.InterfaceManager;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class RequestParserTest {
    private final GpioManager mockedGpioManager = MockedGpioManager.getInstance();
    private final String requestedPin = mockedGpioManager.getPin("P0").getName();
    private final Function<Interface, InterfaceManager> converter = (t) -> {
        switch (t) {
            case GPIO:
                return MockedGpioManager.getInstance();
            default:
                throw new IllegalArgumentException();
        }
    };

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
            gpioRead = RequestParser.parse(converter, "gpio: read:" + requestedPin);
            gpioWrite = RequestParser.parse(converter, "gpio:write:" + requestedPin);

            assertThat(gpioRead.getClass()).isEqualTo(GpioReadRequest.class);
            assertThat(gpioWrite.getClass()).isEqualTo(GpioWriteRequest.class);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void nullTest() throws Exception {
        assertThatThrownBy(() -> RequestParser.parse(converter, null)).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void testEmptyString() throws Exception {
        assertThatThrownBy(() -> RequestParser.parse(converter, "")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void testEmptyRequest() throws Exception {
        assertThatThrownBy(() -> RequestParser.parse(converter, "::")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void testWrongOrderOfArgs() {
        assertThatThrownBy(() -> RequestParser.parse(converter, "read:gpio:14")).
                isInstanceOf(IllegalRequestException.class);
    }

}
