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

import request.read.GpioReadRequest;
import request.read.I2cReadRequest;
import request.read.SpiReadRequest;

import request.manager.I2cManager;
import request.manager.SpiManager;
import request.manager.GpioManager;
import request.manager.InterfaceManager;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class ReadRequestFactory {

     public static Request of(InterfaceManager interfaceManager, String... args) throws IllegalRequestException {
        if(interfaceManager instanceof GpioManager && args.length == 1) {
            return ReadRequestFactory.gpio((GpioManager)interfaceManager, args[0]);
        } else if(interfaceManager instanceof I2cManager && args.length == 3) {
            return ReadRequestFactory.i2c((I2cManager)interfaceManager, args[0], args[1], args[2]);
        } else if(interfaceManager instanceof SpiManager && args.length == 2) {
            return ReadRequestFactory.spi((SpiManager)interfaceManager, args[0], args[1]);
        }
        throw new IllegalRequestException();
     }

    /**
     * Returns ReadRequest instance based on the {@code interfc} Interface.
     * @param interfc
     * @param content String variable which has got different meaning based
     * upon the interface it is being passed to
     * @return
     * @throws request.IllegalRequestException
     */
    private static Request gpio(GpioManager gpioManager, String content) throws IllegalRequestException {
            return new GpioReadRequest(gpioManager, content.trim());
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
    private static Request spi(SpiManager deviceManager, String content, String content1)
                                                                      throws IllegalRequestException {
            int slaveIndex;
            byte[] rBuffer;
            try {
               slaveIndex = Integer.decode(content);
               String[] bytes = content1.split(StringConstants.VAL_SEPARATOR.toString());
               rBuffer = new byte[bytes.length];
               for(int i = 0; i < bytes.length; i++) {
                   rBuffer[i] = Short.decode(bytes[i]).byteValue();
               }
               return new SpiReadRequest(deviceManager, slaveIndex, rBuffer);
            } catch(NumberFormatException nfe) {
                throw new IllegalRequestException(nfe);
            }
    }

    private static Request i2c(I2cManager deviceManager, String content, String content1, String content2)
    throws IllegalRequestException {
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
          return new I2cReadRequest(deviceManager, slaveAddress, registerAddressLo, len);
    }

}
