/*
 * Copyright 2017 Miloslav Zezulka.
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

import io.silverspoon.bulldog.core.pin.Pin;
import request.manager.GpioManager;
import request.read.GpioReadRequest;
import request.read.I2cReadRequest;
import request.read.SpiReadRequest;
import request.read.UartReadRequest;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class ReadRequestFactory {

    /**
     * Returns ReadRequest instance based on the {@code interfc} Interface.
     * @param interfc
     * @param content String variable which has got different meaning based
     * upon the interface it is being passed to
     * @return 
     */
    public static Request of(Interface interfc, String content) throws IllegalRequestException {
        switch (interfc) {
            case GPIO:
                Pin pin = GpioManager.getPinFromName(content);
                return new GpioReadRequest(pin);
            case I2C:
                //return new I2cReadRequest(content);
            case SPI:
                //return SpiReadRequest.getInstance();
            case UART:
                //return UartReadRequest.getInstance();
            default:
                throw new UnsupportedOperationException(interfc + 
                        "not supported with the name parameter");
        }
    }
    
    /**
     * Returns ReadRequest instance based on the {@code interfc} Interface.
     * @param interfc
     * @throws request.IllegalRequestException
     * @throws IllegalArgumentException if the input given is not supported 
     * by this implementation
     * @return 
     */
    public static Request of(Interface interfc) throws IllegalRequestException {
        switch (interfc) {
            case GPIO:
                throw new IllegalRequestException("Gpio interface must always"
                        + "specify the name of the pin the client wishes to read from");
            case I2C:
                return I2cReadRequest.getInstance();
            case SPI:
                //return SpiReadRequest.getInstance();
            case UART:
                //return UartReadRequest.getInstance();
            default:
                throw new UnsupportedOperationException(interfc + "not supported");
        }
    }
}
