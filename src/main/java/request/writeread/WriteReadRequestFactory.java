package request.writeread;

import request.IllegalRequestException;
import request.NumericConstants;
import request.Request;
import request.manager.I2cManager;
import request.manager.InterfaceManager;
import request.manager.SpiManager;
import request.read.I2cReadRequest;
import request.read.SpiReadRequest;
import request.write.I2cWriteRequest;
import request.write.SpiWriteRequest;

public final class WriteReadRequestFactory {

    private static final int HEX = 16;

    private WriteReadRequestFactory() {
    }

    public static Request of(InterfaceManager manager, String... args)
            throws IllegalRequestException {
        if (manager instanceof I2cManager && args.length == 2) {
            return i2c((I2cManager) manager, args[0], args[1]);
        } else if (manager instanceof SpiManager && args.length == 2) {
            return spi((SpiManager) manager, args[0], args[1]);
        }
        throw new IllegalRequestException();
    }

    private static SpiWriteReadRequest spi(SpiManager spiManager, 
            String content, String content1) throws IllegalRequestException {
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
        final SpiReadRequest read 
                = new SpiReadRequest(spiManager, slaveIndex, tBuf);
        final SpiWriteRequest write 
                = new SpiWriteRequest(spiManager, slaveIndex, tBuf);
        return new SpiWriteReadRequest(write, read);
    }

    private static I2cWriteReadRequest i2c(I2cManager i2cManager, 
            String content, String content1) throws IllegalRequestException {
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
        final I2cReadRequest read 
                = new I2cReadRequest(i2cManager, slaveAddr, bytes.length);
        final I2cWriteRequest write 
                = new I2cWriteRequest(i2cManager, slaveAddr, bytes);
        return new I2cWriteReadRequest(write, read);
    }
}
