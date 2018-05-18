package protocol.request.write;

import io.silverspoon.bulldog.core.Signal;
import mocks.MockedGpioManager;
import org.junit.Before;
import org.junit.Test;
import protocol.request.Request;
import protocol.request.RequestParserUtils;
import protocol.request.StringConstants;
import protocol.request.manager.GpioManager;

import static org.assertj.core.api.Assertions.assertThat;

public class GpioWriteRequestTest {

    private GpioManager manager;

    public GpioWriteRequestTest() {
    }

    @Before
    public void init() {
        manager = new MockedGpioManager();
    }

    @Test
    public void gpioResponse() {
        Request req = new GpioWriteRequest(manager, RequestParserUtils.REQUESTED_PIN_NAME);
        String expectedLow = String.format(StringConstants.GPIO_RESPONSE_FORMAT,
                RequestParserUtils.REQUESTED_PIN_NAME,
                Signal.Low.toString().toUpperCase());
        assertThat(req.responseString()).isEqualTo(expectedLow);
    }

    @Test
    public void gpioErrorResponse() {
        Request req = new GpioWriteRequest(manager, "");
        assertThat(req.responseString()).isEqualTo(StringConstants.ERROR_RESPONSE);
    }

}
