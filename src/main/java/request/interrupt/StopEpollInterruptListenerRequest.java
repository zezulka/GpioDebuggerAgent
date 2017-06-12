package request.interrupt;

public final class StopEpollInterruptListenerRequest extends AbstractEpollInterruptListenerRequest {

    private static final String PREFIX = "INTR_STOPPED";

    private static final InterruptListenerManager MANAGER = EpollInterruptListenerManager.getInstance();

    public StopEpollInterruptListenerRequest(InterruptListenerArgs arg) {
        super(arg);
    }

    @Override
    protected String getMessagePrefix() {
        return PREFIX;
    }

    @Override
    public void handleRequest() {
        MANAGER.deregisterInput(super.getArg());
    }
}
