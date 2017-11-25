package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ApplicationProperties {

    private static final Properties PROPS = new Properties();
    private static final Logger LOGGER
            = LoggerFactory.getLogger(ApplicationProperties.class);

    static {
        try (InputStream is = new FileInputStream("default.properties")) {
            PROPS.load(is);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("could not find properties file", ex);
        } catch (IOException ex) {
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

    public static Integer serverSocketPort() {
        return parseNumber("net.server.socket.port");
    }

    public static Integer socketPort() {
        return parseNumber("net.socket.port");
    }

    public static Integer timeout() {
        return parseNumber("net.timeout");
    }
}
