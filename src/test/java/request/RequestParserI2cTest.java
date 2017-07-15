package request;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

import request.read.*;
import request.write.*;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class RequestParserI2cTest {

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
            Request gpioRead = RequestParser.parse(RequestParserUtils.CONVERTER, "i2c:read:0x68:1");
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
            Request gpioWrite = RequestParser.parse(RequestParserUtils.CONVERTER, "i2c:write:0x68:00FFAB1000");
            assertThat(gpioWrite.getClass()).isEqualTo(I2cWriteRequest.class);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void i2cReadNegativeSlave() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "i2c:read:-1:1")).
                isInstanceOf(IllegalRequestException.class);
    }
    
    @Test
    public void i2cWriteNegativeSlave() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "i2c:write:-1:1")).
                isInstanceOf(IllegalRequestException.class);
    }
    
    @Test
    public void i2cReadNegativeLen() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "i2c:read:0x68:-1")).
                isInstanceOf(IllegalRequestException.class);
    }
    
    @Test
    public void i2cReadZeroLen() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "i2c:read:0x68:0")).
                isInstanceOf(IllegalRequestException.class);
    }
    
    @Test
    public void i2cWriteErroneousByte() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "i2c:write:0x68:0xKK")).
                isInstanceOf(IllegalRequestException.class);
    }
    
    @Test
    public void i2cWriteErroneousBytes() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "i2c:write:0x68:0x000x00")).
                isInstanceOf(IllegalRequestException.class);
    }
    
    @Test
    public void i2cMissingLength() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "i2c:read:0x68:")).
                isInstanceOf(IllegalRequestException.class);
    }
}

