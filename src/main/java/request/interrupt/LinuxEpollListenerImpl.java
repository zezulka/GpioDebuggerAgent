package request.interrupt;

public class LinuxEpollListenerImpl extends AbstractEpollInterruptListenerRequest {

    private static final String PREFIX = "INTR_FEEDBACK";

    protected String getMessagePrefix() {
        return PREFIX;
    }

    public LinuxEpollListenerImpl(InterruptListenerArgs arg) {
        super(arg);
    }
    
    @Override
    public void handleRequest() {
    }
}
