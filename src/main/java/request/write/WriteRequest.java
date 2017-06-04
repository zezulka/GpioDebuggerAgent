package request.write;

import request.Request;

/**
 * Write request marker interface.
 * @author Miloslav Zezulka, 2017
 */
public interface WriteRequest extends Request {
    void write();
}
