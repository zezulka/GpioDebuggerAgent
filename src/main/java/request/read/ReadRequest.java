package request.read;

import request.Request;

/**
 * Read request marker interface.
 *
 * @author Miloslav Zezulka, 2017
 */
public interface ReadRequest extends Request {

    String read();
}
