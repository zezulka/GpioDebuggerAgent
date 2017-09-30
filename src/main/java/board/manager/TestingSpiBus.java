package board.manager;

import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.io.bus.spi.SpiBus;
import io.silverspoon.bulldog.core.io.bus.spi.SpiConnection;
import io.silverspoon.bulldog.core.io.bus.spi.SpiMessage;
import io.silverspoon.bulldog.core.io.bus.spi.SpiMode;
import io.silverspoon.bulldog.core.pin.Pin;
import java.util.List;
import java.util.Random;

public class TestingSpiBus extends TestingBus implements SpiBus {

    public TestingSpiBus(String name) {
        super(name);
    }

    @Override
    public Pin getMISO() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Pin getMOSI() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Pin getSCLK() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public List<Integer> getslaveSelectIndexes() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getSpeedInHz() {
        return 0;
    }

    @Override
    public void setSpeedInHz(int hz) {
    }

    @Override
    public void setBitsPerWord(int bpw) {
    }

    @Override
    public int getBitsPerWord() {
        return 0;
    }

    @Override
    public void setDelayMicroseconds(int delay) {
    }

    @Override
    public int getDelayMicroseconds() {
        return 0;
    }

    @Override
    public void setMode(SpiMode mode) {
    }

    @Override
    public SpiMode getMode() {
        return SpiMode.Mode0;
    }

    @Override
    public void useLeastSignificantBitFirst() {
    }

    @Override
    public void useMostSignificantBitFirst() {
    }

    @Override
    public boolean isLSBUsed() {
        return false;
    }

    @Override
    public boolean isMSBUsed() {
        return false;
    }

    @Override
    public void selectSlave(DigitalOutput chipSelect) {
    }

    @Override
    public void selectSlaves(DigitalOutput... chipSelects) {
    }

    @Override
    public void selectSlaves(Integer... chipSelectAddresses) {
    }

    @Override
    public SpiConnection createSpiConnection() {
        return new SpiConnection(this);
    }

    @Override
    public SpiConnection createSpiConnection(int chipSelectAddress) {
        return new SpiConnection(this, chipSelectAddress);
    }

    @Override
    public SpiConnection createSpiConnection(DigitalOutput chipSelect) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public SpiConnection createSpiConnection(DigitalOutput... chipSelects) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public SpiConnection createSpiConnection(int... chipSelectAddress) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void broadcast(byte[] bytes, Integer... chipSelects) {
    }

    @Override
    public SpiMessage transfer(byte[] buffer) {
        SpiMessage msg = new SpiMessage();
        byte[] randBuff = new byte[buffer.length];
        new Random().nextBytes(randBuff);
        msg.setReceivedBytes(randBuff);
        new Random().nextBytes(randBuff);
        msg.setSentBytes(randBuff);
        return msg;
    }

}
