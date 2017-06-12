package request.interrupt;

import java.io.IOException;
import java.time.LocalTime;
import net.ProtocolManager;

public abstract class AbstractEpollInterruptListenerRequest implements InterruptListenerRequest {

   private final InterruptListenerArgs arg;
    
  /**
   * @throws IllegalArgumentException args is null
   */
   public AbstractEpollInterruptListenerRequest(InterruptListenerArgs args) {
       if(args == null) {
           throw new IllegalArgumentException("Args cannot be null");
       }
       this.arg = args;
   }

   @Override
   public void giveFeedbackToClient() throws IOException {
       ProtocolManager.getInstance().setMessageToSend(getMessagePrefix()
                                                      + ':'
                                                      + LocalTime.now().getNano()
                                                      + ':'
                                                      + arg.getPin().getName()
                                                      + ':' + arg.getEdge()
                                                      + '\n');
   }
   
   protected InterruptListenerArgs getArg() {
       return this.arg;
   }

   protected abstract String getMessagePrefix();

}
