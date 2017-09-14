package request;

import java.time.format.DateTimeFormatter;

public final class BulldogRequestUtils {

    private static final int MASK = 0xFF;
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("HH.mm.ss.S");

    private BulldogRequestUtils() {
    }

    /**
     * Converts byte array input into formatted String such that it conforms
     * with the defined protocol between agent and client. This usually means
     * that the bytes contained in the array are separated by a special char.
     */
    public static String getFormattedByteArray(byte[] array) {
        if (array == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (byte b : array) {
            //byte is always interpreted as signed, we
            //dont want that in this case
            result = result
                    .append(' ')
                    .append((short) (b & MASK));
        }
        return result.toString();
    }
}
