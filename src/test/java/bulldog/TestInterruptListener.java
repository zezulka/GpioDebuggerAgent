package bulldog;

import io.silverspoon.bulldog.core.event.InterruptEventArgs;
import protocol.request.interrupt.AbstractInterruptListenerRequest;

public class TestInterruptListener extends AbstractInterruptListenerRequest {

    public TestInterruptListener(InterruptEventArgs arg) {
        super(arg);
    }

    @Override
    protected void interruptRequestImpl(InterruptEventArgs iea) {
        System.out.println(iea.getEdge() + ", " + iea.getPin().getName());
    }

    @Override
    protected String getMessageFormatter() {
        return null;
    }

    @Override
    public void action() {
        // NO-OP
    }
}
