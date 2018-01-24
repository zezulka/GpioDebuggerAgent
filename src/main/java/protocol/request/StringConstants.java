package protocol.request;

public final class StringConstants {

    private StringConstants() {
    }

    public static final String ERROR_RESPONSE = "ERR";
    public static final String REQ_SEPARATOR = ":";
    public static final String VAL_SEPARATOR = " ";
    public static final String INIT_PREFIX = "INIT:";
    public static final String GPIO_RESPONSE_FORMAT = "GPIO:%s:%s";
    public static final String I2C_READ_RESPONSE_FORMAT = "I2C:%s";
    public static final String I2C_WRITE_RESPONSE = "I2C:Write OK";
    public static final String SPI_READ_RESPONSE_FORMAT = "SPI:%s";
    public static final String SPI_WRITE_RESPONSE = "SPI:Write OK";

    private static final String INTRS_APPENDIX = ":%s:%s:%s";
    public static final String START_INTRS_RESPONSE_FORMAT
            = "INTR_STARTED" + INTRS_APPENDIX;

    public static final String STOP_INTRS_RESPONSE_FORMAT
            = "INTR_STOPPED" + INTRS_APPENDIX;

    public static final String GENERATED_INTR_RESPONSE_FORMAT
            = "INTR_GENERATED" + INTRS_APPENDIX;
}
