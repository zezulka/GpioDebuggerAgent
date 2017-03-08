/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request;

import request.read.GpioReadRequest;
import request.read.I2cReadRequest;
import request.read.SpiReadRequest;
import request.read.UartReadRequest;
import request.write.GpioWriteRequest;
import request.write.I2cWriteRequest;
import request.write.SpiWriteRequest;
import request.write.UartWriteRequest;

/**
 * @author Miloslav Zezulka, 2017
 */
public class RequestParser {
    
    public static final String SEPARATOR = ":";
    public static final String OP_SEPARATOR = " ";
    public static final int REQ_ARGS = 2;
    
    private RequestParser() {}   
    
    /**
     * Parses client request given by String read from agent's {@code InputStream}.
     * The format of the request is as follows: 
     * 
     * <br/><br/>
     * 
     * 
     * <ul>&lt;INTERFACE&gt;&lt;DELIMITER&gt;&lt;OPERATION&gt;[&lt;DELIMITER&gt;&lt;PIN_NAME&gt;],</ul>
     * where delimiter is the ':' char symbol.
     * 
     * <br/><br/>
     * 
     * For available interfaces and operations, please consult documentation of 
     * {@code request.Interface} and {@code request.Operation} classes.
     * @param clientInput
     * @return
     * @throws IllegalRequestException in case illegal String request has been
     * provided
     * @throws IllegalArgumentException null as input was provided
     */
    public static Request parse(String clientInput) throws IllegalRequestException {
        if(clientInput == null) {
            throw new IllegalArgumentException("request cannot be null");
        }
        String[] request = clientInput.split(SEPARATOR, REQ_ARGS + 1);
        if(request.length < REQ_ARGS) {
            throw new IllegalRequestException(
                String.format("request must have exactly %d arguments, "
                        + "supplied: %d", REQ_ARGS, request.length)
            );
        }
        Interface interfc;
        Operation op;
        try {
            interfc = Interface.valueOf(request[0].trim().toUpperCase());
            op = Operation.valueOf(request[1].trim().toUpperCase());
        } catch(IllegalArgumentException ex) {
            throw new IllegalRequestException(ex);
        }
        
        switch(op) {
            case READ: {
                switch(interfc) {
                    case GPIO: return GpioReadRequest.getInstance(request[2]);
                    case I2C: return I2cReadRequest.getInstance();
                    case SPI : return SpiReadRequest.getInstance();
                    case UART: return UartReadRequest.getInstance();
                }
            }
            case WRITE: {
                switch(interfc) {
                    case GPIO: return GpioWriteRequest.getInstance(request[2]);
                    case I2C: return I2cWriteRequest.getInstance();
                    case SPI : return SpiWriteRequest.getInstance();
                    case UART: return UartWriteRequest.getInstance();
                }
            }
            default : throw new IllegalRequestException("parse failed: " + request[1]);
        }
    }
}
