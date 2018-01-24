package protocol.request;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BulldogRequestUtilsTest {

    public BulldogRequestUtilsTest() {
    }

    @Test
    public void testByteArrayFormatterEmptyString() {
        assertThat(BulldogRequestUtils.getFormattedByteArray(new byte[]{})).isEqualTo("");
    }

    @Test
    public void testByteArrayFormatterThreeBytes() {
        assertThat(BulldogRequestUtils.getFormattedByteArray(new byte[]{10, -1, 17})).isEqualToIgnoringCase("0AFF11");
    }
}
