package request.write;

import java.io.IOException;
import request.Request;

/**
 * Marker interface symbolizing that this is a read request.
 * @author Miloslav Zezulka, 2017
 */
public interface WriteRequest extends Request {
    void write();
}
