package request.manager;

import io.silverspoon.bulldog.core.io.bus.i2c.I2cConnection;

import java.io.IOException;

import net.ProtocolMessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class I2cManagerBulldogImpl implements I2cManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(I2cManager.class);
    private static I2cConnection i2cConnection;
    private final BoardManager boardManager;

    private I2cManagerBulldogImpl(BoardManager boardManager) {
        this.boardManager = boardManager;
    }
    
    public static I2cManagerBulldogImpl getInstance(BoardManager boardManager) {
        return new I2cManagerBulldogImpl(boardManager);
    }

    /**
     * Reads bytes from i2c interface.
     *
     * @return I2c contents.
     */
    @Override
    public String readFromI2c(int slave, int address, int len) {
        LOGGER.info(String.format("I2c read request from position %x and of length %d", address, len));
        createConnectionIfNecessary(slave);
        try {
            if(len == 1) {
              byte b = i2cConnection.readByteFromRegister(address);
              return b + " 0x" + Integer.toString((byte)b, 16);
            } else if(len > 1){
              byte[] buff = new byte[len];
              i2cConnection.readBytesFromRegister(address, buff);
              StringBuilder builder = new StringBuilder();
              for(byte b : buff) {
                  builder = builder.append(b).append("\t0x").append(Integer.toString((byte)b, 16)).append('\n');
              }
              return builder.toString();
            }
        } catch(IOException ex) {
            LOGGER.error(null, ex);
        }
        return "";
    }

    /*
     * Reads bytes from the given address. I2c connection must have
     * been established before calling this method.
     */
    @Override
    public void writeIntoI2c(int slave, int address, byte[] message) {
        createConnectionIfNecessary(address);
        if(message == null || message.length == 0) {
            LOGGER.error("Client attempted to write no data, ignoring...");
            return;
        }
        try {
            LOGGER.info(String.format("I2c write request from position %x and of length %d", address, message.length));
            if(message.length == 1) {
                i2cConnection.writeByteToRegister(address, message[0]);
            } else {
                i2cConnection.writeBytesToRegister(address, message);
            }
        } catch (IOException ex) {
            LOGGER.error(ProtocolMessages.S_IO_EXCEPTION.toString(), ex);
        }
    }
    
    private void createConnectionIfNecessary(int address) {
        if(i2cConnection == null) {
            i2cConnection = this.boardManager.getI2c().createI2cConnection(address);
            return;
        }
        if(i2cConnection.getAddress() != address) {
            try {
                i2cConnection.getBus().close();
            } catch (IOException ex) {
                LOGGER.error(null, ex);
            } finally {
                i2cConnection = this.boardManager.getI2c().createI2cConnection(address);
            }
        }
    }
}