package request.write;

import io.silverspoon.bulldog.core.Signal;
import static org.assertj.core.api.Assertions.*;
import mocks.MockedGpioManager;
import org.junit.Before;
import org.junit.Test;
import request.IllegalRequestException;
import request.Request;
import request.RequestParserUtils;
import request.StringConstants;
import request.manager.GpioManager;

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
        assertThat(req.getFormattedResponse()).isEqualTo(expectedLow);
    }

    @Test
    public void gpioErrorResponse() {
        Request req = new GpioWriteRequest(manager, "");
        assertThat(req.getFormattedResponse()).isEqualTo(StringConstants.ERROR_RESPONSE);
    }

}
