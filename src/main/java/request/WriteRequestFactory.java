/*
 * Copyright 2017 Miloslav.
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
import request.write.GpioWriteRequest;
import request.write.I2cWriteRequest;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class WriteRequestFactory {

    public static Request of(Interface interfc, String content) throws IllegalRequestException {
        switch(interfc) {
            case GPIO: {
                Pin pin = GpioManager.getPinFromName(content);
                return new GpioWriteRequest(pin);
            }
            case I2C: {
                return new I2cWriteRequest(content);
            }
            default: throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    public static Request of(Interface interfc, String content1, String content2) throws IllegalRequestException {
        switch(interfc) {
            case GPIO: {
                int desiredVoltage;
                Pin pin;
                try {
                    desiredVoltage = Integer.parseInt(content2);
                    pin = GpioManager.getPinFromName(content1);
                    if(desiredVoltage != 1 && desiredVoltage != 0) {
                        throw new IllegalRequestException("voltage 0 or 1, provided:" + desiredVoltage);
                    }
                } catch(NumberFormatException nfe) {
                    throw new IllegalRequestException(nfe);
                }
                return new GpioWriteRequest(pin, desiredVoltage == 1);
            }
            default: throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
}
