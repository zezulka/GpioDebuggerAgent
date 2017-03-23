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

    private RequestParser() {
    }

    /**
     * Parses client request given by String read from agent's
     * {@code InputStream}. The format of the request is as follows:
     *
     * <br/>
     *
     * <ul>
     * <li>GPIO:READ:{PIN_NO}</li>
     * <li>GPIO:WRITE:{PIN_NO}:{0,1}?</li>
     * <li>I2C:READ:{INTERFACE_NAME}?</li>
     * <li>I2C:WRITE{:{INTERFACE_NAME}}?:{CONTENT}</li>
     * </ul>
     * ,':' being the delimiter symbol.
     *
     *
     * For available interfaces and operations, please consult documentation of
     * {@code request.Interface} and {@code request.Operation} classes.
     *
     * @param clientInput
     * @return
     * @throws IllegalRequestException in case illegal String request has been
     * provided, including null parameter
     */
    public static Request parse(String clientInput) throws IllegalRequestException {
        if (clientInput == null) {
            throw new IllegalRequestException("request cannot be null");
        }
        String[] request = clientInput.split(SEPARATOR, 4);
        Interface interfc;
        Operation op;
        try {
            interfc = Interface.valueOf(request[0].trim().toUpperCase());
            op = Operation.valueOf(request[1].trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalRequestException(ex);
        }

        switch (op) {
            case READ: {
                switch (interfc) {
                    case GPIO:
                        return GpioReadRequest.getInstance(request[2]);
                    case I2C:
                        return I2cReadRequest.getInstance();
                    case SPI:
                        return SpiReadRequest.getInstance();
                    case UART:
                        return UartReadRequest.getInstance();
                }
            }
            case WRITE: {
                switch (interfc) {
                    case GPIO:
                        if(request.length == 3) {
                            return GpioWriteRequest.getInstanceImplicitVoltage(request[2]);
                        } else if(request.length == 4) {
                            boolean desiredVoltage;
                            if("0".equals(request[3])) {
                                desiredVoltage = false;
                            } else if("1".equals(request[3])) {
                                desiredVoltage = true;
                            } else {
                                throw new IllegalRequestException("specified voltage must be either \"0\" or \"1\".");
                            }
                            return GpioWriteRequest.getInstanceExplicitVoltage(request[2], desiredVoltage);
                        }  
                    case I2C:
                        if(request.length == 3) {
                            return I2cWriteRequest.getInstance(request[2]);
                        }
                        throw new UnsupportedOperationException("write request with interface names not supported yet");
                    case SPI:
                        return SpiWriteRequest.getInstance();
                    case UART:
                        return UartWriteRequest.getInstance();
                }
            }
            default:
                throw new IllegalRequestException("parse failed: " + clientInput);
        }
    }
}
