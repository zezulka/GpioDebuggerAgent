package mocks;

import board.test.BoardManager;
import protocol.request.InitMessage;

public class MockedInitMessage extends InitMessage {

    public MockedInitMessage(BoardManager manager) {
        super(manager);
    }

    @Override
    public void performRequest() {
    }
}
