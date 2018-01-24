package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public final class ApplicationProperties {

    private static final Properties PROPS = new Properties();
    private static final Logger LOGGER
            = LoggerFactory.getLogger(ApplicationProperties.class);

    static {


        try {
            PROPS.load(ApplicationProperties.class
                    .getClassLoader()
                    .getResourceAsStream("default.properties"));
        } catch (IOException ex) {
            LOGGER.error("Fatal error: default properties file is missing in resources.");
            throw new RuntimeException(ex);
        }
    }

    private ApplicationProperties() {
    }

    /**
     *
     * @return Null if the property found is malformed and not a number, or
     * boxed int representing value of the property
     */
    private static Integer parseNumber(String propertyName) {
        String w = PROPS.getProperty(propertyName);
        if (w == null) {
            return null;
        }
        try {
            return Integer.valueOf(w);
        } catch (NumberFormatException e) {
            LOGGER.error(propertyName
                    + " property is not numeric in the properties file");
            return null;
        }
    }

    public static Integer lockPort() {
        return parseNumber("lock.port");
    }

    public static Integer socketPort() {
        return parseNumber("server.socket.port");
    }

    public static Integer timeout() {
        return parseNumber("net.timeout");
    }
}
