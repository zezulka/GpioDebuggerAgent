package request.manager;

import board.manager.BoardManager;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cConnection;

import java.io.IOException;

import net.ProtocolMessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BulldogI2cManager implements I2cManager {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(I2cManager.class);
    private static I2cConnection i2cConnection;
    private final BoardManager boardManager;

    public BulldogI2cManager(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    @Override
    public byte[] readFromI2c(int slave, int len) {
        createConnectionIfNecessary(slave);
        try {
            byte[] buff = new byte[len];
            i2cConnection.readBytes(buff);
            return buff;
        } catch (IOException ex) {
            LOGGER.error("reading from i2c failed", ex);
            return null;
        }
    }

    /*
     * Reads bytes from the given address. I2c connection must have
     * been established before calling this method.
     */
    @Override
    public void writeIntoI2c(int slaveAddress, byte[] message) {
        if (!createConnectionIfNecessary(slaveAddress)) {
            LOGGER.error("Could not write to i2c interface, "
                    + "connection creation failed.");
            return;
        }
        if (message == null || message.length == 0) {
            LOGGER.debug("Client attempted to write no data, ignoring...");
            return;
        }
        try {
            i2cConnection.writeBytes(message);
        } catch (IOException ex) {
            LOGGER.error(ProtocolMessages.IO_EXCEPTION.toString(), ex);
        }
    }

    /**
     *
     * @return Connection establishment was successful. This does not
     * necessarily imply that new connection must be created; if connection
     * already exists, this method is no-op and returns true as well.
     */
    private boolean createConnectionIfNecessary(int address) {
        if (i2cConnection == null) {
            i2cConnection = boardManager.getI2c().createI2cConnection(address);
            return true;
        }
        if (i2cConnection.getAddress() != address) {
            try {
                I2cBus i2cBus = boardManager.getI2c();
                if (i2cBus.isOpen()) {
                    // Would be nicer to end connection directly via
                    // i2cBus.closeConnection(i2cConnection.getAddress())
                    // but that was not available at the moment in bulldog
                    i2cBus.close();
                }
                i2cConnection = i2cBus.createI2cConnection(address);
                return true;
            } catch (IOException ex) {
                LOGGER.error(null, ex);
                return false;
            }
        }
        return true;
    }
}
