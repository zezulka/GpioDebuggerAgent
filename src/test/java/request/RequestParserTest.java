package request;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.mocks.MockedBoard;
import io.silverspoon.bulldog.core.mocks.MockedDigitalInput;
import io.silverspoon.bulldog.core.platform.Board;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import request.read.GpioReadRequest;
import request.write.GpioWriteRequest;
import static org.assertj.core.api.Assertions.*;
import request.write.WriteRequest;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class RequestParserTest {

    private Board boardMock;
    
    public RequestParserTest() {
    }

    @Before
    public void setUp() {
        boardMock = new MockedBoard();
    }

    @After
    public void tearDown() {
    }

    /**
     * Happy scenario test.
     */
    @Test
    public void gpioTest() {
        String requestedPin = "22";
        Request gpioRead, gpioWrite;
        try {
            gpioRead = RequestParser.parse("gpio: read:" + requestedPin);
            gpioWrite = RequestParser.parse("gpio:write:" + requestedPin);

            assertThat(gpioRead.getClass()).isEqualTo(GpioReadRequest.class);
            assertThat(gpioWrite.getClass()).isEqualTo(GpioWriteRequest.class);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void nullTest() throws Exception {
        assertThatThrownBy(() -> RequestParser.parse(null)).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void gpioWriteWithDesiredVoltageLow() {
        String requestedPin = "22";
        WriteRequest gpioWrite;
        try {
            gpioWrite = (WriteRequest) RequestParser.parse("gpio: write:" + requestedPin + ":0");
            gpioWrite.write();
            assertThat(new MockedDigitalInput(boardMock.getPin("P22"))
                    .read())
                    .isEqualTo(Signal.Low);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void gpioWriteWithDesiredVoltageHigh() {
        String requestedPin = "22";
        WriteRequest gpioWrite;
        try {
            gpioWrite = (WriteRequest) RequestParser.parse("gpio: write:" + requestedPin + ":1");
            gpioWrite.write();
            assertThat(new MockedDigitalInput(boardMock.getPin("P22"))
                    .read())
                    .isEqualTo(Signal.High);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void gpioWriteWithDesiredVoltageFail() {
        String requestedPin = "22";
        assertThatThrownBy(
                () -> RequestParser.parse("gpio: write:" + requestedPin + ":42"))
                .isInstanceOf(IllegalRequestException.class);
    }
    
    @Test
    public void gpioWriteExplicitWithImplicit() {
        String requestedPin = "22";
        WriteRequest gpioWrite;
        try {
            gpioWrite = (WriteRequest) RequestParser.parse("gpio: write:" + requestedPin + ":1");
            gpioWrite.write();
            assertThat(new MockedDigitalInput(boardMock.getPin("P22"))
                    .read())
                    .isEqualTo(Signal.High);
            gpioWrite = (WriteRequest) RequestParser.parse("gpio: write:" + requestedPin);
            gpioWrite.write();
            assertThat(new MockedDigitalInput(boardMock.getPin("P22"))
                    .read())
                    .isEqualTo(Signal.Low);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void nonsenseRequestTestEmptyString() throws Exception {
        assertThatThrownBy(() -> RequestParser.parse("")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void nonsenseRequestTestEmptyRequest() throws Exception {
        assertThatThrownBy(() -> RequestParser.parse("::")).
                isInstanceOf(IllegalRequestException.class);
    }

    @Test
    public void nonsenseRequestTestWrongOrderOfArgs() {
        assertThatThrownBy(() -> RequestParser.parse("read:gpio:14")).
                isInstanceOf(IllegalRequestException.class);
    }

}
