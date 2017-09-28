package request.read;

import mocks.MockedI2cManager;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;
import request.BulldogRequestUtils;
import request.Request;
import request.StringConstants;
import request.manager.I2cManager;

/**
 *
 * @author miloslav
 */
public class I2cReadRequestTest {

    private static I2cManager manager;

    public I2cReadRequestTest() {
    }

    @Before
    public void init() {
        manager = new MockedI2cManager();
    }

    @Test
    public void i2cReadResponse() {
        final String expected = String.format(StringConstants.I2C_READ_RESPONSE_FORMAT,
                BulldogRequestUtils.getFormattedByteArray(manager.readFromI2c(0, 2)));
        Request req = new I2cReadRequest(manager, 0, 2);
        assertThat(req.getFormattedResponse()).isEqualTo(expected);
    }
}
