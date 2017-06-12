package request.interrupt;

public final class StopEpollInterruptListenerRequest extends AbstractEpollInterruptListenerRequest {

<<<<<<< HEAD
    private static final String PREFIX = "INTR_STOPPED";
=======
    private static final String PREFIX = "INTR_STOP";
>>>>>>> 56aaf63c4dfb58906460112874bda94f4b30cddd
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
<<<<<<< HEAD
        MANAGER.deregisterInput(super.getArg());
=======
        MANAGER.deregisterInputs(super.getArg());
>>>>>>> 56aaf63c4dfb58906460112874bda94f4b30cddd
    }
}
