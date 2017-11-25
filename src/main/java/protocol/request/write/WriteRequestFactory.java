package protocol.request.write;

import protocol.request.IllegalRequestException;
import protocol.request.NumericConstants;

import protocol.request.manager.I2cManager;
import protocol.request.manager.SpiManager;
import protocol.request.manager.InterfaceManager;
import protocol.request.manager.GpioManager;

public final class WriteRequestFactory {

    private static final int HEX = 16;

    private WriteRequestFactory() {
    }

    public static WriteRequest of(InterfaceManager manager, String... args)
            throws IllegalRequestException {
        if (manager instanceof GpioManager && args.length == 1) {
            return gpioValImplicit((GpioManager) manager, args[0]);
        } else if (manager instanceof I2cManager && args.length == 2) {
            return i2c((I2cManager) manager, args[0], args[1]);
        } else if (manager instanceof SpiManager && args.length == 2) {
            return spi((SpiManager) manager, args[0], args[1]);
        }
        throw new IllegalRequestException();
    }

    private static WriteRequest gpioValImplicit(GpioManager pinAccessor,
            String content) throws IllegalRequestException {
        return new GpioWriteRequest(pinAccessor, content.trim());
    }

    private static WriteRequest spi(SpiManager spiManager, String content,
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

    private static WriteRequest i2c(I2cManager i2cManager, String content,
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
