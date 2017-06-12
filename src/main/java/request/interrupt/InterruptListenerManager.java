package request.interrupt;

public interface InterruptListenerManager {
    boolean registerInput(InterruptListenerArgs input);
    boolean deregisterInput(InterruptListenerArgs input);
}
