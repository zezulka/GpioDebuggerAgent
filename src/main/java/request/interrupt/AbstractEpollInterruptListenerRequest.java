package request.interrupt;

import java.io.IOException;
import java.time.LocalTime;
import net.AgentConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class roofing all classes dealing with interrupt listeners.
 * @author Miloslav Zezulka
 */
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

   /**
    * Sends back client message in special format (i.e. must be parsed on client side).
    * Contains pin on which the interrupt was registered. In its prefix, information
    * about whether the interrupt listener has been (de)registered or generated, is contained.
    * @throws IOException 
    */
   @Override
   public void giveFeedbackToClient() throws IOException {
       String response = getMessagePrefix()
                                + ':'
                                + arg.getDigitalIoFeature().getPin().getName()
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
