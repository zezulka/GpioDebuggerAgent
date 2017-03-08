package request;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import request.read.GpioReadRequest;
import request.read.I2cReadRequest;
import request.read.SpiReadRequest;
import request.write.GpioWriteRequest;
import request.write.SpiWriteRequest;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class RequestParserTest {

    public RequestParserTest() {
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Happy scenario test.
     */
    @Test
    public void allInterfacesTest() {
        String requestedPin = "22";
        Request gpioRead, gpioWrite, spiRead, spiWrite, i2cRead;
        try {

            gpioRead = RequestParser.parse("gpio: read:" + requestedPin);
            gpioWrite = RequestParser.parse("gpio:write:" + requestedPin);

            spiWrite = RequestParser.parse("spi:WritE");
            spiRead = RequestParser.parse("spi  :  read:");

            i2cRead = RequestParser.parse("i2C:read");
            //comparing the actual objects does not help - new instance of XXXRequest is created
            assertEquals(gpioRead.getClass(), GpioReadRequest.class);
            assertEquals(gpioWrite.getClass(), GpioWriteRequest.class);
            assertEquals(spiWrite.getClass(), SpiWriteRequest.class);
            assertEquals(spiRead.getClass(), SpiReadRequest.class);
            assertEquals(i2cRead.getClass(), I2cReadRequest.class);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void nullTest() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        RequestParser.parse(null);
    }
    
    @Test
    public void nonsenseRequestTest1() throws Exception {
        expectedException.expect(IllegalRequestException.class);
        RequestParser.parse("");
    }
    
    @Test
    public void nonsenseRequestTest2() throws Exception {
        expectedException.expect(IllegalRequestException.class);
        RequestParser.parse("::");
    }

}
