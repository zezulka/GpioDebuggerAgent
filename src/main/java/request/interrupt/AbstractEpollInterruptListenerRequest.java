package request.interrupt;

import java.io.IOException;
import java.time.LocalTime;
import net.AgentConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEpollInterruptListenerRequest implements InterruptListenerRequest {

   private final InterruptListenerArgs arg;
   private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEpollInterruptListenerRequest.class);

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
       String response = getMessagePrefix()
                                + ':'
                                + arg.getPin().getName()
                                + ':' + arg.getEdge()
                                + ':'
                                + LocalTime.now().getNano()
                                + '\n';
       LOGGER.info(String.format("[Interrupt listeners] sent to client: %s", response));
       AgentConnectionManager.setMessageToSend(response);
   }

   protected InterruptListenerArgs getArg() {
       return this.arg;
   }

   protected abstract String getMessagePrefix();

}
