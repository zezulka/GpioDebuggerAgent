package request.read;

import io.silverspoon.bulldog.core.Signal;
import mocks.MockedGpioManager;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import org.junit.Test;
import org.junit.Before;
import request.IllegalRequestException;
import request.Request;
import request.RequestParserUtils;
import request.StringConstants;
import request.manager.GpioManager;

public class GpioReadRequestTest {

    private GpioManager manager;

    public GpioReadRequestTest() {
    }

    @Before
    public void init() {
        manager = new MockedGpioManager();
    }

    @Test
    public void gpioResponse() {
        Request req = new GpioReadRequest(manager, RequestParserUtils.REQUESTED_PIN_NAME);
        String expectedLow = String.format(StringConstants.GPIO_RESPONSE_FORMAT,
                RequestParserUtils.REQUESTED_PIN_NAME,
                Signal.Low.toString().toUpperCase());
        assertThat(req.getFormattedResponse()).isEqualTo(expectedLow);
    }

    @Test
    public void gpioErrorResponse() {
        Request req = new GpioReadRequest(manager, "");
        assertThat(req.getFormattedResponse()).isEqualTo(StringConstants.ERROR_RESPONSE);
    }

}
