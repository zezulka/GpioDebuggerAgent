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
    public static final int REQ_ARGS = 3;
    
    private RequestParser() {}
    
    public static Request parse(String clientInput) throws IllegalRequestException {
        if(clientInput == null) {
            throw new IllegalArgumentException("request cannot be null");
        }
        String[] request = clientInput.split(SEPARATOR, REQ_ARGS);
        if(request.length != REQ_ARGS) {
            throw new IllegalRequestException(
                String.format("request must have exactly %d arguments, "
                        + "supplied: %d", REQ_ARGS, request.length)
            );
        }
        switch(Operation.valueOf(request[1].toUpperCase())) {
            case READ: {
                switch(Interface.valueOf(request[0].toUpperCase())) {
                    case GPIO: return GpioReadRequest.getInstance(request[2]);
                    case I2C: return I2cReadRequest.getInstance();
                    case SPI : return SpiReadRequest.getInstance();
                    case UART: return UartReadRequest.getInstance();
                }
            }
            case WRITE: {
                switch(Interface.valueOf(request[0].toUpperCase())) {
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
