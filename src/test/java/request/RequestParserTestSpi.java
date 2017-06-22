/*
 * Copyright 2017 miloslav.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package request;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import request.read.SpiReadRequest;
import request.write.SpiWriteRequest;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class RequestParserTestSpi {

    @Test
    public void spiRead() {
        try {
            Request spiRead = RequestParser.parse(RequestParserUtils.CONVERTER, "spi:read:0x00:0x00 0x00 0xFF 0xFF 0xCA");
            assertThat(spiRead.getClass()).isEqualTo(SpiReadRequest.class);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void spiWrite() {
        try {
            Request spiWrite = RequestParser.parse(RequestParserUtils.CONVERTER, "spi:write:0x00:0x00 0x00 0xFF 0xFF 0xCA");
            assertThat(spiWrite.getClass()).isEqualTo(SpiWriteRequest.class);
        } catch (IllegalRequestException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void spiReadMissingChipIndex() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "spi:read::0x00 0x00 0xFF 0xFF 0xCA")).
                isInstanceOf(IllegalRequestException.class);
    }
    
    @Test
    public void spiReadNegativeChipIndex() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "spi:read:-1:0x00 0x00 0xFF 0xFF 0xCA")).
                isInstanceOf(IllegalRequestException.class);
    }
    
    @Test
    public void spiReadNoData() {
        assertThatThrownBy(() -> RequestParser.parse(RequestParserUtils.CONVERTER, "spi:read:0x00:")).
                isInstanceOf(IllegalRequestException.class);
    }
}
