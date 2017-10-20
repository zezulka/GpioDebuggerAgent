package protocol.request;

/**
 * This represents a Request on the agent's side. Please note that client does
 * not work with this request representation in any way.
 *
 */
public interface Request {

    String getFormattedResponse();

    void performRequest();
}
