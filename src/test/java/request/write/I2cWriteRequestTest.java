package request.write;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import org.junit.Test;
import request.IllegalRequestException;
import request.Request;
import request.RequestParser;
import request.RequestParserUtils;
import request.StringConstants;

public class I2cWriteRequestTest {

    public I2cWriteRequestTest() {
    }

    @Test
    public void i2WriteResponse() {
        try {
            Request i2cWrite = RequestParser.parse(RequestParserUtils.CONVERTER, "i2c:write:0x00:0000FFFFCA");
            assertThat(i2cWrite.getFormattedResponse()).isEqualTo(StringConstants.I2C_WRITE_RESPONSE_FORMAT);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

}
