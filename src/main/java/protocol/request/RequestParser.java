package protocol.request;

import protocol.request.interrupt.StopInterruptRequestFactory;
import protocol.request.interrupt.StartInterruptRequestFactory;
import protocol.request.read.ReadRequestFactory;
import protocol.request.write.WriteRequestFactory;
import java.util.Arrays;

import java.util.function.Function;

import protocol.request.manager.InterfaceManager;
import protocol.request.writeread.WriteReadRequestFactory;

public final class RequestParser {

    private RequestParser() {
    }

    /**
     * Parses client request given by String read from agent's
     * {@code InputStream}. The format of the request is one of the following:
     *
     *
     * GPIO:READ:{PIN_NAME} GPIO:WRITE:{PIN_NAME}{:{0,1}?}
     *
     * I2C:READ:{SLAVE_ADDRESS_HEX}:{LEN} I2C:WRITE:{SLAVE_ADDRESS_HEX}:{DATA}+
     * I2C:WRITE_READ:{SLAVE_ADDRESS_HEX}:{DATA}+
     *
     * SPI:READ:{CHIP_INDEX}:{DATA}* SPI:WRITE:{CHIP_INDEX}:{DATA}*
     * SPI:WRITE_READ:{CHIP_INDEX}:{DATA}*
     *
     * GPIO:INTR_{INTR_STOP|INTR_START}:{PIN_NAME + ' ' + INTERRUPT_TYPE} ,':'
     * being the delimiter symbol.
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
    public static Request parse(
            Function<DeviceInterface, InterfaceManager> converter,
            String clientInput) throws IllegalRequestException {
        if (clientInput == null) {
            throw new IllegalRequestException("request cannot be null");
        }
        String[] request
                = clientInput.split(StringConstants.REQ_SEPARATOR);
        if (request.length < NumericConstants.MIN_NUM_ARGS) {
            throw new IllegalRequestException(String
                    .format("Request must have at least %d args.",
                            request.length));
        }
        DeviceInterface interfc;
        Operation op;
        try {
            interfc = DeviceInterface.valueOf(request[0].trim().toUpperCase());
            op = Operation.valueOf(request[1].trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalRequestException(ex);
        }
        switch (op) {

            case READ: {
                return ReadRequestFactory.of(converter.apply(interfc),
                        Arrays.copyOfRange(request, 2, request.length));
            }
            case WRITE: {
                return WriteRequestFactory.of(converter.apply(interfc),
                        Arrays.copyOfRange(request, 2, request.length));
            }
            case WRITE_READ: {
                return WriteReadRequestFactory.of(converter.apply(interfc),
                        Arrays.copyOfRange(request, 2, request.length));
            }
            case INTR_STOP: {
                return StopInterruptRequestFactory.of(converter.apply(interfc),
                        request[2]);
            }
            case INTR_START: {
                return StartInterruptRequestFactory.of(converter.apply(interfc),
                        request[2]);
            }
            default:
                throw new IllegalRequestException("invalid operation");
        }

    }
}