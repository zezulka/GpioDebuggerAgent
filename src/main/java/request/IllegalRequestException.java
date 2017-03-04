/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class IllegalRequestException extends Exception {
    public IllegalRequestException() {
        super();
    }
    
    public IllegalRequestException(String msg) {
        super(msg);
    }
    
    public IllegalRequestException(Throwable cause) {
        super(cause);
    }
    
    public IllegalRequestException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
