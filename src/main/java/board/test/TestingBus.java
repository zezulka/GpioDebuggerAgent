package board.test;

import io.silverspoon.bulldog.core.io.bus.Bus;
import io.silverspoon.bulldog.core.io.bus.BusConnection;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestingBus implements Bus {

    private String name;
    private String alias;
    private boolean isOpen = false;
    private int selectedAddress = 0;

    TestingBus(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public void open() {
        isOpen = true;
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void close() {
        isOpen = false;
    }

    public void writeByte(byte b) {
    }

    @Override
    public byte readByte() {
        return 0;
    }

    @Override
    public void selectSlave(int address) {
        selectedAddress = address;
    }

    @Override
    public boolean isSlaveSelected(int address) {
        return selectedAddress == address;
    }

    @Override
    public BusConnection createConnection(int address) {
        return new BusConnection(this, address);
    }

    @Override
    public FileOutputStream getOutputStream() {
        return null;
    }

    @Override
    public FileInputStream getInputStream() {
        return null;
    }

    @Override
    public void writeBytes(byte[] bytes) {
    }

    @Override
    public int readBytes(byte[] buffer) {
        return 0;
    }

    @Override
    public void writeString(String string) {
    }

    @Override
    public String readString() {
        return null;
    }

    @Override
    public void writeByte(int b) {
    }

}
