package protocol.request;

import board.test.BoardManager;
import protocol.request.interrupt.StartInterruptRequestFactory;
import protocol.request.interrupt.StopInterruptRequestFactory;
import protocol.request.manager.InterfaceManager;
import protocol.request.read.ReadRequestFactory;
import protocol.request.write.WriteRequestFactory;
import protocol.request.writeread.WriteReadRequestFactory;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;

public final class RequestUtils {

    private RequestUtils() {
    }

    /**
     * Parses client request given by String read from agent's
     * {@code InputStream}. The format of the request is one of the following:
     * GPIO:READ:{PIN_NAME} GPIO:WRITE:{PIN_NAME}{:{0,1}?}
     * I2C:READ:{SLAVE_ADDRESS_HEX}:{LEN} I2C:WRITE:{SLAVE_ADDRESS_HEX}:{DATA}+
     * I2C:WRITE_READ:{SLAVE_ADDRESS_HEX}:{DATA}+
     * SPI:READ:{CHIP_INDEX}:{DATA}* SPI:WRITE:{CHIP_INDEX}:{DATA}*
     * SPI:WRITE_READ:{CHIP_INDEX}:{DATA}*
     * GPIO:INTR_{INTR_STOP|INTR_START}:{PIN_NAME + ' ' + INTERRUPT_TYPE} ,
     * where ':' is the delimiter symbol.
     * For available interfaces and operations, please consult documentation of
     * {@code request.Interface} and {@code request.Operation} classes.
     *
     * @throws IllegalRequestException in case illegal String request has been
     *                                 provided, including null parameter
     */
    public static Request parse(BoardManager boardManager, String clientInput)
            throws IllegalRequestException {
        if (clientInput == null) {
            throw new IllegalRequestException("cannot be null");
        }
        Deque<String> requestDeque = new ArrayDeque<>();
        Collections.addAll(requestDeque,
                clientInput.split(StringConstants.REQ_SEPARATOR));
        if (requestDeque.isEmpty()) {
            throw new IllegalRequestException("cannot be empty");
        }
        Operation op;
        InterfaceManager manager;
        try {
            RequestType rt = RequestType.valueOf(requestDeque.removeFirst().
                    trim().toUpperCase());
            if (rt.equals(RequestType.INIT)) {
                return new InitMessage(boardManager);
            } else {
                manager = boardManager.deviceToInterfaceMapper().apply(rt);
                op = Operation.valueOf(requestDeque.removeFirst().
                        toUpperCase());
                return parserHelper(op, manager, requestDeque.
                        toArray(new String[0]));
            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalRequestException(ex);
        }

    }

    private static Request parserHelper(Operation op, InterfaceManager manager,
                                        String[] rest)
            throws IllegalRequestException {
        switch (op) {
            case READ: {
                return ReadRequestFactory.of(manager, rest);
            }
            case WRITE: {
                return WriteRequestFactory.of(manager, rest);
            }
            case WRITE_READ: {
                return WriteReadRequestFactory.of(manager, rest);
            }
            case INTR_STOP: {
                return StopInterruptRequestFactory.of(manager, rest[0]);
            }
            case INTR_START: {
                return StartInterruptRequestFactory.of(manager, rest[0]);
            }
            default:
                throw new IllegalRequestException("invalid operation");
        }
    }
}
