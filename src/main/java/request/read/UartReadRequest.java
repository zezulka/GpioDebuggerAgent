package request.read;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class UartReadRequest implements ReadRequest {
    private static final UartReadRequest INSTANCE = new UartReadRequest();

    private UartReadRequest() {
    }

    public static UartReadRequest getInstance() {
        return INSTANCE;
    }

    @Override
    public String read() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void giveFeedbackToClient() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
