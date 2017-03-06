package request;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class RequestParserTest {
    
    public RequestParserTest() {
    }
    
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
     * Test of parse method, of class RequestParser.
     */
    @Test
    public void testParse() throws Exception {
        Logger.getAnonymousLogger().log(Level.INFO, "Test for parse:");
    }

    private void test1() throws IllegalRequestException {
        String clientInput = "gpio:read";
        RequestParser.parse(clientInput);
        assertEquals(this, this);
    }
    
}
