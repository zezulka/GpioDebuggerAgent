package request.interrupt;

public final class StartEpollInterruptListenerRequest extends AbstractEpollInterruptListenerRequest {

    private static final String PREFIX = "INTR_STARTED";
    private static final InterruptListenerManager MANAGER = EpollInterruptListenerManager.getInstance();

    public StartEpollInterruptListenerRequest(InterruptListenerArgs arg) {
        super(arg);
    }

    protected String getMessagePrefix() {
        return PREFIX;
    }

    @Override
    public void handleRequest() {
        MANAGER.registerInput(super.getArg());
    }
}
