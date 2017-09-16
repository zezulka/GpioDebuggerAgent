package request.write;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import request.IllegalRequestException;
import request.Request;
import request.RequestParser;
import request.RequestParserUtils;
import request.StringConstants;

public class SpiWriteRequestTest {
    
    public SpiWriteRequestTest() {
    }

    @Test
    public void spiWriteResponse() {
        try {
            Request spiWrite = RequestParser.parse(RequestParserUtils.CONVERTER, "spi:write:0x00:0000FFFFCA");
            assertThat(spiWrite.getFormattedResponse()).isEqualTo(StringConstants.SPI_WRITE_RESPONSE_FORMAT);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }
    
}
