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

import request.write.GpioWriteRequest;
import request.write.I2cWriteRequest;
import request.write.SpiWriteRequest;

import request.manager.I2cManager;
import request.manager.SpiManager;
import request.manager.GpioManager;

import request.manager.InterfaceManager;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class WriteRequestFactory {

    public static Request of(InterfaceManager interfaceManager, String... args) throws IllegalRequestException {
        if (interfaceManager instanceof GpioManager && args.length == 1) {
            if (args.length == 1) {
                return WriteRequestFactory.gpioValueImplicit((GpioManager) interfaceManager, args[0]);
            } else if (args.length == 2) {
                return WriteRequestFactory.gpioValueExplicit((GpioManager) interfaceManager, args[0], args[1]);
            }
        } else if (interfaceManager instanceof I2cManager && args.length == 2) {
            return WriteRequestFactory.i2c((I2cManager) interfaceManager, args[0], args[1]);
        } else if (interfaceManager instanceof SpiManager && args.length == 2) {
            return WriteRequestFactory.spi((SpiManager) interfaceManager, args[0], args[1]);
        }
        throw new IllegalRequestException();
    }

    private static Request gpioValueImplicit(GpioManager gpioManager, String content) throws IllegalRequestException {
        return new GpioWriteRequest(gpioManager, content.trim());
    }

    private static Request gpioValueExplicit(GpioManager gpioManager, String content, String content1) throws IllegalRequestException {
        return new GpioWriteRequest(gpioManager, content.trim(), content1.trim());
    }

    private static Request spi(SpiManager spiManager, String content, String content1) throws IllegalRequestException {
        int slaveIndex;
        byte[] tBuffer;
        try {
            slaveIndex = Integer.decode(content);
            if(slaveIndex < 0) {
                throw new IllegalRequestException();
            }
            String[] bytes = content1.split(StringConstants.VAL_SEPARATOR.toString());
            tBuffer = new byte[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                tBuffer[i] = Short.decode(bytes[i]).byteValue();
            }
        } catch (NumberFormatException nfe) {
            throw new IllegalRequestException(nfe);
        }
        return new SpiWriteRequest(spiManager, slaveIndex, tBuffer);
    }

    private static Request i2c(I2cManager i2cManager, String content, String content1) throws IllegalRequestException {
        int slaveAddress;
        byte[] bytes;
        try {
            slaveAddress = Integer.decode(content);
            if (slaveAddress < 0 || slaveAddress > NumericConstants.I2C_MAX_SLAVE_ADDR) {
                throw new IllegalRequestException(String.format("slave address not in bounds [0;%d]", NumericConstants.I2C_MAX_SLAVE_ADDR));
            }
            String[] bytesStr = content1.split(StringConstants.VAL_SEPARATOR.toString());
            bytes = new byte[bytesStr.length];
            for (int i = 0; i < bytesStr.length; i++) {
                bytes[i] = Short.decode(bytesStr[i]).byteValue();
            }
        } catch (NumberFormatException nfe) {
            throw new IllegalRequestException(nfe);
        }
        return new I2cWriteRequest(i2cManager, slaveAddress, bytes);
    }
}
