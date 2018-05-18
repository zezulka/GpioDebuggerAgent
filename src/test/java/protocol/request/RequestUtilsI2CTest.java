package protocol.request;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import protocol.request.read.I2cReadRequest;
import protocol.request.write.I2cWriteRequest;

import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class RequestUtilsI2CTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Happy scenario I2C test.
     */
    @Test
    public void i2cReadRequest() {
        try {
            Request gpioRead = RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "i2c:read:0x68:1");
            assertThat(gpioRead.getClass()).isEqualTo(I2cReadRequest.class);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Happy scenario I2C test.
     */
    @Test
    public void i2cWriteRequest() {
        try {
            Request gpioWrite = RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "i2c:write:0x68:00FFAB1000");
            assertThat(gpioWrite.getClass()).isEqualTo(I2cWriteRequest.class);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void i2cReadNegativeSlave() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "i2c:read:-1:1")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void i2cWriteNegativeSlave() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "i2c:write:-1:1")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void i2cReadNegativeLen() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "i2c:read:0x68:-1")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void i2cReadZeroLen() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "i2c:read:0x68:0")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void i2cWriteErroneousByte() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "i2c:write:0x68:0xKK")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void i2cWriteErroneousBytes() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "i2c:write:0x68:0x000x00")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void i2cMissingLength() {
        assertThatThrownBy(() -> RequestUtils.parse(RequestParserUtils.BOARD_MANAGER, "i2c:read:0x68:")).
                isInstanceOf(IllegalRequestException.class);
    }
}
