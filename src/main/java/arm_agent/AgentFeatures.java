package arm_agent;

import request.Request;
import request.RequestFactory;

/**
 * This represents all the features the agent is capable of.
 * @author Miloslav Zezulka, 2017
 */
public class AgentFeatures {
    
    
    public static void respondToRequest(Request req) {
        if(RequestFactory.parse(req)) {
            sendAppropriateMessageToClient();
        } else {
            sendErrorMessageToClient();
        }
        
    }

    private static void sendAppropriateMessageToClient() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void sendErrorMessageToClient() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
