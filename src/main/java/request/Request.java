package request;

import java.io.IOException;

/**
 * This represents a Request on the agent's side. Please note that client does
 * not work with this request representation in any way.
 *
 * @author Miloslav Zezulka, 2017
 */
public interface Request {

    /**
     * After the request has been processed, information is sent back to client
     * (via {@code OutputStream} of the opened socket).
     *
     * @throws java.io.IOException connection to client has been lost
     */
    void giveFeedbackToClient() throws IOException;
}
