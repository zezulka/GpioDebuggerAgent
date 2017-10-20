package protocol.request.read;

import java.util.Objects;
import protocol.request.StringConstants;

import protocol.request.manager.I2cManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.request.BulldogRequestUtils;

public final class I2cReadRequest extends AbstractReadRequest {

    private final I2cManager i2cManager;
    private static final Logger LOGGER
            = LoggerFactory.getLogger(I2cReadRequest.class);
    private final int len;
    private final int slaveAddr;

    public I2cReadRequest(I2cManager i2cManager, int slaveAddress, int len) {
        Objects.requireNonNull(i2cManager, "i2cManager");
        if (len <= 0) {
            throw new IllegalArgumentException("len must be a positive number");
        }
        this.len = len;
        this.slaveAddr = slaveAddress;
        this.i2cManager = i2cManager;
    }

    private String formattedResponse() {
        LOGGER.info(String.format("I2c read request from slave %d, length %d",
                slaveAddr,
                len));
        String readValue = BulldogRequestUtils
                .getFormattedByteArray(i2cManager.readFromI2c(slaveAddr, len));
        if (readValue == null) {
            return StringConstants.ERROR_RESPONSE;
        } else {
            return String.format(
                    StringConstants.I2C_READ_RESPONSE_FORMAT, readValue);
        }
    }

    @Override
    public String getFormattedResponse() {
        return formattedResponse();
    }

}
