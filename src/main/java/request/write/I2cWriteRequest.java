/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request.write;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class I2cWriteRequest implements WriteRequest {

    private static final I2cWriteRequest INSTANCE = new I2cWriteRequest();
    
    private I2cWriteRequest() {
    }
    
    public static I2cWriteRequest getInstance() {
        return INSTANCE;
    }

    @Override
    public void write() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void giveFeedbackToClient() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
