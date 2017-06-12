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

import core.DeviceManager;

import io.silverspoon.bulldog.core.pin.Pin;

import request.read.GpioReadRequest;
import request.read.I2cReadRequest;
import request.read.SpiReadRequest;

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
     * @throws request.IllegalRequestException
     */
    public static Request of(Interface interfc, String content) throws IllegalRequestException {
        switch (interfc) {
            case GPIO:
                Pin pin = DeviceManager.getPin(content);
                if(pin == null) {
                    throw new IllegalRequestException("pin with the given descriptor has not been found");
                }
                return new GpioReadRequest(pin);
            default:
                throw new UnsupportedOperationException(interfc +
                        "not supported with the name parameter");
        }
    }

    /**
     * Returns ReadRequest instance based on the {@code interfc} Interface.
     * @param interfc
     * @param content String variable which has got different meaning based
     * upon the interface it is being passed to
     * @param content1 another String variable which has got different meaning based
     * upon the interface it is being passed to
     * @return
     * @throws request.IllegalRequestException
     */
    public static Request of(Interface interfc, String content, String content1) throws IllegalRequestException {
        switch (interfc) {
            case SPI:
                int slaveIndex;
                byte[] rBuffer;
                try {
                   slaveIndex = Integer.decode(content);
                   String[] bytes = content1.split(StringConstants.VAL_SEPARATOR.toString());
                   rBuffer = new byte[bytes.length];
                   for(int i = 0; i < bytes.length; i++) {
                       rBuffer[i] = Short.decode(bytes[i]).byteValue();
                   }
                   return new SpiReadRequest(slaveIndex, rBuffer);
                } catch(NumberFormatException nfe) {
                  throw new IllegalRequestException(nfe);
                }
            default:
                throw new UnsupportedOperationException(interfc +
                        "not supported with the name parameter");
        }
    }

    public static Request of(Interface interfc, String content, String content1, String content2)
    throws IllegalRequestException {
      switch (interfc) {
        case I2C:
            int slaveAddress;
            int registerAddressLo;
            int len;
            try {
                slaveAddress = Integer.decode(content);
                registerAddressLo = Integer.decode(content1);
                len = Integer.decode(content2);
            } catch(NumberFormatException nfe) {
                throw new IllegalRequestException(nfe);
            }
            return I2cReadRequest.getInstance(slaveAddress, registerAddressLo, len);
          default:
              throw new UnsupportedOperationException(interfc +
                      "not supported with the name parameter");
      }
    }

}
