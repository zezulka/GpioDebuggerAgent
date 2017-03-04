/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request.read;

import java.util.List;
import request.Interface;

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
