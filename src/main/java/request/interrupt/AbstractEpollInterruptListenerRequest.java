package request.interrupt;

import java.io.IOException;
import java.time.LocalTime;
import net.ProtocolManager;

public abstract class AbstractEpollInterruptListenerRequest implements InterruptListenerRequest {

   private final InterruptListenerArgs arg;

  /**
   * @throws IllegalArgumentException args is null
   */
   public AbstractEpollInterruptListenerRequest(InterruptListenerArgs arg) {
       if(arg == null) {
           throw new IllegalArgumentException("Args cannot be null");
       }
       this.arg = arg;
   }

   @Override
   public void giveFeedbackToClient() throws IOException {
       ProtocolManager.getInstance().setMessageToSend(getMessagePrefix()
                                                      + ':'
                                                      + arg.getPin().getName()
                                                      + ':' + arg.getEdge()
                                                      + ':'
                                                      + LocalTime.now().getNano()
                                                      + '\n');
   }

   protected InterruptListenerArgs getArg() {
       return this.arg;
   }

   protected abstract String getMessagePrefix();

}
