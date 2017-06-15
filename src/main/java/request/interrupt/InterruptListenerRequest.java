package request.interrupt;

import request.Request;

public interface InterruptListenerRequest extends Request {
    /**
      * Performs requested operation denoted by the client.
      * Operations available: start/stop interrupt listener for the given
      * {@code Pin} and {@code InterruptType} combination given in the constructor of subtypes.
      * There can be no more than one such listener with the mentioned combination.
      */
    void handleInterruptRequest();
}
