package request;

import request.read.GpioReadRequest;
import request.read.I2cReadRequest;
import request.read.SpiReadRequest;

import request.manager.I2cManager;
import request.manager.SpiManager;
import request.manager.InterfaceManager;
import request.manager.PinAccessor;

public final class ReadRequestFactory {

    private static final int HEX = 16;

    private ReadRequestFactory() {
    }

    public static Request of(InterfaceManager interfaceManager, String... args)
            throws IllegalRequestException {
        if (interfaceManager instanceof PinAccessor && args.length == 1) {
            return ReadRequestFactory.gpio((PinAccessor) interfaceManager,
                    args[0]);
        } else if (interfaceManager instanceof I2cManager && args.length == 2) {
            return ReadRequestFactory.i2c((I2cManager) interfaceManager,
                    args[0], args[1]);
        } else if (interfaceManager instanceof SpiManager && args.length == 2) {
            return ReadRequestFactory.spi((SpiManager) interfaceManager,
                    args[0], args[1]);
        }
        throw new IllegalRequestException();
    }

    /**
     * Returns ReadRequest instance based on the {@code interfc} Interface.
     *
     * @param interfc
     * @param content String variable which has got different meaning based upon
     * the interface it is being passed to
     * @return
     * @throws request.IllegalRequestException
     */
    private static Request gpio(PinAccessor pinAccessor, String content)
            throws IllegalRequestException {
        return new GpioReadRequest(pinAccessor, content);
    }

    private static Request spi(SpiManager deviceManager, String content,
            String content1)
            throws IllegalRequestException {
        int slaveIndex;
        byte[] rBuffer;
        try {
            slaveIndex = Integer.decode(content);
            if (slaveIndex < 0) {
                throw new IllegalRequestException("slave index is negative");
            }
            if (content1.length() % 2 == 1) {
                throw new IllegalRequestException("odd number of digits");
            }
            rBuffer = new byte[content1.length() / 2];
            for (int i = 0; i < content1.length(); i += 2) {
                rBuffer[i / 2] = (byte) Short
                        .parseShort(content1.substring(i, i + 2), HEX);
            }
            return new SpiReadRequest(deviceManager, slaveIndex, rBuffer);
        } catch (NumberFormatException nfe) {
            throw new IllegalRequestException(nfe);
        }
    }

    private static Request i2c(I2cManager deviceManager, String content,
            String content1)
            throws IllegalRequestException {
        int slaveAddr;
        int len;
        try {
            slaveAddr = Integer.decode(content);
            len = Integer.decode(content1);
            if (len <= 0) {
                throw new IllegalRequestException("len must be positive!");
            }
            if (slaveAddr < 0 || slaveAddr > NumericConstants.I2C_MAX_ADDR) {
                throw new IllegalRequestException(String
                        .format("slave address not in bounds [0;%d]",
                                NumericConstants.I2C_MAX_ADDR));
            }
        } catch (NumberFormatException nfe) {
            throw new IllegalRequestException(nfe);
        }
        return new I2cReadRequest(deviceManager, slaveAddr, len);
    }

}
