package protocol.request.write;

import org.junit.Test;
import protocol.request.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class SpiWriteRequestTest {

    public SpiWriteRequestTest() {
    }

    @Test
    public void spiWriteResponse() {
        try {
            Request spiWrite = RequestParser.parse(RequestParserUtils.CONVERTER, "spi:write:0x00:0000FFFFCA");
            assertThat(spiWrite.getFormattedResponse()).isEqualTo(StringConstants.SPI_WRITE_RESPONSE);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

}
