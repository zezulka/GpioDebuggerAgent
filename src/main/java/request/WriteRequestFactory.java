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

import core.DeviceManager;
import io.silverspoon.bulldog.core.pin.Pin;
import request.write.GpioWriteRequest;
import request.write.I2cWriteRequest;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class WriteRequestFactory {

    public static Request of(Interface interfc, String content) throws IllegalRequestException {
        switch (interfc) {
            case GPIO: {
                Pin pin = DeviceManager.getPin(content);
                if (pin == null) {
                    throw new IllegalRequestException("pin with the given descriptor has not been found");
                }
                return new GpioWriteRequest(pin);
            }
            default:
                throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static Request of(Interface interfc, String content1, String content2) throws IllegalRequestException {
        switch (interfc) {
            case GPIO: {
                int desiredVoltage;
                Pin pin;
                try {
                    desiredVoltage = Integer.decode(content2);
                    pin = DeviceManager.getPin(content1);
                    if (pin == null) {
                        throw new IllegalRequestException("pin with the given descriptor has not been found");
                    }
                } catch (NumberFormatException nfe) {
                    throw new IllegalRequestException(nfe);
                }
                return new GpioWriteRequest(pin, desiredVoltage != 0);
            }
            default:
                throw new UnsupportedOperationException("Not supported yet.");
        }
    }


    public static Request of(Interface interfc, String content, String content1, String content2) throws IllegalRequestException {
        switch (interfc) {
          case I2C: {
            int slaveAddress;
            int registerAddress;
            try {
                slaveAddress = Integer.decode(content);
                registerAddress = Integer.decode(content1);
            } catch(NumberFormatException nfe) {
                throw new IllegalRequestException(nfe);
            }
            String[] bytesStr = content2.split(" ");
            byte[] bytes = new byte[bytesStr.length];
            for(int i = 0; i < bytesStr.length; i++) {
                bytes[i] = Byte.decode(bytesStr[i]);
            }
            return new I2cWriteRequest(slaveAddress, registerAddress, bytes);
          }
          default:
             throw new UnsupportedOperationException("Not supported yet.");
        }
    }


}
