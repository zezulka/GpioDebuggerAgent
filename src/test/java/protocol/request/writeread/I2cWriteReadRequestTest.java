/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol.request.writeread;

import mocks.MockedI2cManager;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import protocol.request.IllegalRequestException;
import protocol.request.Request;
import protocol.request.RequestParser;
import protocol.request.RequestParserUtils;

import protocol.request.manager.I2cManager;

/**
 *
 * @author miloslav
 */
public class I2cWriteReadRequestTest {
    
    private static I2cManager manager;

    public I2cWriteReadRequestTest() {
    }

    @Before
    public void init() {
        manager = new MockedI2cManager();
    }

    @Test
    public void i2WriteReadFormat() {
        try {
            Request i2cWriteRead = RequestParser.parse(RequestParserUtils.CONVERTER, "i2c:write_read:0x00:10:0000FFFFCA");
            assertThat(i2cWriteRead).isInstanceOf(I2cWriteReadRequest.class);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void i2WriteReadResponse() {
        try {
            final int requestLen = 10;
            Request i2cWriteRead = RequestParser.parse(RequestParserUtils.CONVERTER, "i2c:write_read:0x00:10:0000FFFFCA");
            // I2C:[byte sequence], hence plus 4
            assertThat(i2cWriteRead.getFormattedResponse().length()).isEqualTo(requestLen * 2 + 4);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }

    
}
