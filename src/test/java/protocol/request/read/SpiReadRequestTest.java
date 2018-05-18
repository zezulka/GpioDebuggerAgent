package protocol.request.read;

import mocks.MockedSpiManager;
import org.junit.Before;
import org.junit.Test;
import protocol.request.BulldogRequestUtils;
import protocol.request.Request;
import protocol.request.StringConstants;
import protocol.request.manager.SpiManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author miloslav
 */
public class SpiReadRequestTest {

    private static SpiManager manager;

    public SpiReadRequestTest() {
    }

    @Before
    public void init() {
        manager = new MockedSpiManager();
    }

    @Test
    public void spiReadResponse() {
        final String expected = String.format(StringConstants.SPI_READ_RESPONSE_FORMAT,
                BulldogRequestUtils.getFormattedByteArray(manager.readFromSpi(0, new byte[]{})));
        Request req = new SpiReadRequest(manager, 0, new byte[]{});
        assertThat(req.responseString()).isEqualTo(expected);
    }

}
