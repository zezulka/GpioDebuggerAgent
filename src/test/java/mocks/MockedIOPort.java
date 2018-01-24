package mocks;

import io.silverspoon.bulldog.core.io.IOPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MockedIOPort implements IOPort {

    private String name;

    MockedIOPort(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAlias() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAlias(String alias) {
        // TODO Auto-generated method stub

    }

    @Override
    public void open() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isOpen() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeByte(int b) {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeBytes(byte[] bytes) {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeString(String string) {
        // TODO Auto-generated method stub

    }

    @Override
    public byte readByte() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int readBytes(byte[] buffer) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String readString() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OutputStream getOutputStream() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getInputStream() {
        // TODO Auto-generated method stub
        return null;
    }

}
