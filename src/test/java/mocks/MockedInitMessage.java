package mocks;

import board.manager.BoardManager;
import request.InitMessage;

public class MockedInitMessage extends InitMessage {

    public MockedInitMessage(BoardManager manager) {
        super(manager);
    }

    @Override
    public void performRequest() {
        addFeatures(Feature.GPIO);
    }

}
