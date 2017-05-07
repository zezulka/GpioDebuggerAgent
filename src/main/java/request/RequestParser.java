package request;
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
     * <li>GPIO:READ:{PIN_NAME}</li>
     * <li>GPIO:WRITE:{PIN_NAME}{:{0,1}?}</li>
     * <li>I2C:READ:{SLAVE_ADDRESS_HEX}:{REGISTER_ADDRESS_HEX}:{INTERFACE_NAME}?</li>
     * <li>I2C:READRANGE:{SLAVE_ADDRESS_HEX}:{REGISTER_ADDRESS_LO_HEX}:{REGISTER_ADDRESS_HI_HEX}{:INTERFACE_NAME}?</li>
     * <li>I2C:WRITE:{SLAVE_ADDRESS_HEX}:{REGISTER_ADDRESS_HEX}:{CONTENT}{:INTERFACE_NAME}?</li>
     * <li>I2C:WRITERANGE:{SLAVE_ADDRESS_HEX}
                  :{REGISTER_ADDRESS_LO_HEX}:{REGISTER_ADDRESS_HI_HEX}
                  :{CONTENT[]}{:INTERFACE_NAME}?</li>
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
        String[] request = clientInput.split(SEPARATOR, 7);
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
                if(request.length < 3) {
                    throw new IllegalRequestException("too few arguments");
                } else if(request.length == 3) {
                   return ReadRequestFactory.of(interfc, request[2]);
                } else if(request.length == 4) {
                   return ReadRequestFactory.of(interfc, request[2], request[3]);
                }
            }
            case READRANGE: {
              if(request.length < 5) {
                  throw new IllegalRequestException("too few arguments");
              } else if(request.length == 5) {
                  return ReadRequestFactory.ofRange(interfc, request[2], request[3], request[4]);
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
            case WRITERANGE: {
                if(request.length == 5){
                    return WriteRequestFactory.of(interfc, request[2], request[3], request[4]);
                }
            }
            default:
                throw new IllegalRequestException("parse failed: " + clientInput);
        }
    }
}
