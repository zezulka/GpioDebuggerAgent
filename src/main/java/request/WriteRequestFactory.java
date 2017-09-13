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

import request.write.GpioWriteRequest;
import request.write.I2cWriteRequest;
import request.write.SpiWriteRequest;

import request.manager.I2cManager;
import request.manager.SpiManager;

import request.manager.InterfaceManager;
import request.manager.PinAccessor;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public final class WriteRequestFactory {

    private static final int HEX = 16;

    private WriteRequestFactory() {
    }

    public static Request of(InterfaceManager interfaceManager, String... args)
            throws IllegalRequestException {
        if (interfaceManager instanceof PinAccessor && args.length == 1) {
            if (args.length == 1) {
                return gpioValImplicit((PinAccessor) interfaceManager, args[0]);
            } else if (args.length == 2) {
                return gpioValExplicit((PinAccessor) interfaceManager, args[0],
                        args[1]);
            }
        } else if (interfaceManager instanceof I2cManager && args.length == 2) {
            return i2c((I2cManager) interfaceManager, args[0], args[1]);
        } else if (interfaceManager instanceof SpiManager && args.length == 2) {
            return spi((SpiManager) interfaceManager, args[0], args[1]);
        }
        throw new IllegalRequestException();
    }

    private static Request gpioValImplicit(PinAccessor pinAccessor,
            String content) throws IllegalRequestException {
        return new GpioWriteRequest(pinAccessor, content.trim());
    }

    private static Request gpioValExplicit(PinAccessor pinAccessor,
            String content, String content1) throws IllegalRequestException {
        return new GpioWriteRequest(pinAccessor, content.trim(),
                content1.trim());
    }

    private static Request spi(SpiManager spiManager, String content,
            String content1) throws IllegalRequestException {
        int slaveIndex;
        byte[] tBuf;
        try {
            slaveIndex = Integer.decode(content);
            if (slaveIndex < 0) {
                throw new IllegalRequestException();
            }
            if (content1.length() % 2 == 1) {
                throw new IllegalRequestException("odd number of digits");
            }
            tBuf = new byte[content1.length() / 2];
            for (int i = 0; i < content1.length(); i += 2) {
                tBuf[i / 2] = (byte) Short
                        .parseShort(content1.substring(i, i + 2), HEX);
            }
        } catch (NumberFormatException nfe) {
            throw new IllegalRequestException(nfe);
        }
        return new SpiWriteRequest(spiManager, slaveIndex, tBuf);
    }

    private static Request i2c(I2cManager i2cManager, String content,
            String content1) throws IllegalRequestException {
        int slaveAddr;
        byte[] bytes;
        try {
            slaveAddr = Integer.decode(content);
            if (slaveAddr < 0 || slaveAddr > NumericConstants.I2C_MAX_ADDR) {
                throw new IllegalRequestException(String
                        .format("slave address not in bounds [0;%d]",
                                NumericConstants.I2C_MAX_ADDR));
            }
            if (content1.length() % 2 == 1) {
                throw new IllegalRequestException("odd number of digits");
            }
            bytes = new byte[content1.length() / 2];
            for (int i = 0; i < content1.length(); i += 2) {
                bytes[i / 2] = (byte) Short
                        .parseShort(content1.substring(i, i + 2), HEX);
            }
        } catch (NumberFormatException nfe) {
            throw new IllegalRequestException(nfe);
        }
        return new I2cWriteRequest(i2cManager, slaveAddr, bytes);
    }
}
