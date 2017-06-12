package request.interrupt;

public interface InterruptListenerManager {

    boolean registerInputs(InterruptListenerArgs input);
    boolean deregisterInputs(InterruptListenerArgs input);
}
