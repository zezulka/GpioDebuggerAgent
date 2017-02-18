package net;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public enum ProtocolMessages {
    START("Server is running..."), 
    OTHER_APPROPRIATE_MESSAGES("some text here");
    
    private final String msg;
    
    ProtocolMessages(String msg) {
        this.msg = msg;
    }
    
    public String getMessage() {
        return this.msg;
    }
}
