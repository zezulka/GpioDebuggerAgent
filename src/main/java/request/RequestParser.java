package request;

import java.util.Arrays;
/**
 * @author Miloslav Zezulka, 2017
 */
public class RequestParser {

    private RequestParser() {
    }

    /**
     * Parses client request given by String read from agent's
     * {@code InputStream}. The format of the request is one of the following:
     *
     * <br/>
     *
     * <ul>
       * <li>GPIO:READ:{PIN_NAME}</li>
       * <li>GPIO:WRITE:{PIN_NAME}{:{0,1}?}</li>
       * <li>I2C:READ:{SLAVE_ADDRESS_HEX}:{START_REGISTER_ADDRESS_HEX}:{LEN}(:{INTERFACE_NAME})?</li>
       * <li>I2C:WRITE:{SLAVE_ADDRESS_HEX}:{REGISTER_ADDRESS_HEX}:{CONTENT}({' ' + CONTENT})?{:INTERFACE_NAME}?</li>
       * <li>SPI:READ:{CHIP_INDEX}:{VAL + ' '}+{:INTERFACE_NAME}?<li>
       * <li>SPI:WRITE:{CHIP_INDEX}:{VAL + ' '}+{:INTERFACE_NAME}?<li>
       *
       * <li>GPIO:INTR_{STOP|START}:{PIN_NAME + ' ' + INTERRUPT_TYPE}</li>
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
        String[] request = clientInput.split(StringConstants.REQ_WORD_SEPARATOR.toString());
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
                if(request.length == 3) {
                   return ReadRequestFactory.of(interfc, request[2]);
                } else if(request.length == 4) {
                   return ReadRequestFactory.of(interfc, request[2], request[3]);
                } else if(request.length == 5) {
                    return ReadRequestFactory.of(interfc, request[2], request[3], request[4]);
                }
            }
            case WRITE: {
                if(request.length == 3) {
                    return WriteRequestFactory.of(interfc, request[2]);
                } else if(request.length == 4){
                    return WriteRequestFactory.of(interfc, request[2], request[3]);
                } else if(request.length == 5){
                    return WriteRequestFactory.of(interfc, request[2], request[3], request[4]);
                }
            }
            case INTR_STOP: {
                return StopInterruptRequestFactory.of(request[2]);
            }
            case INTR_START: {
                return StartInterruptRequestFactory.of(request[2]);
            }
        }
        throw new IllegalRequestException("invalid number of arguments");
    }
}
