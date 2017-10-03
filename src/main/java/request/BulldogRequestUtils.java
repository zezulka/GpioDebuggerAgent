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
     * Bytes as such are in hexadecimal format.
     */
    public static String getFormattedByteArray(byte[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder()
                .append(getHexStringFromByte(array[0]));
        for (int i = 1; i < array.length; i++) {
            //byte is always interpreted as signed, we
            //dont want that in this case
            result = result.append(' ')
                    .append(getHexStringFromByte(array[i]));
        }
        return result.toString();
    }

    private static String getHexStringFromByte(short b) {
        String str = Integer.toHexString((short) (b & MASK));
        if (str.length() == 2) {
            return str;
        }
        return "0" + str;
    }
}
