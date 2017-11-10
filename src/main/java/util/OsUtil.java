package util;

/**
 *
 * Utility class for determining which OS agent runs on.
 */
public final class OsUtil {

    private static final String OS_NAME = "os.name";

    private OsUtil() {
        /* do not instantiate this class  */
    }

    private static boolean checkOsNameProperty(String substring) {
        return System.getProperty(OS_NAME).toLowerCase().contains(substring);
    }

    public static boolean isWindows() {
        return checkOsNameProperty("windows");
    }

    public static boolean isUnix() {
        return isLinux() || isMac();
    }

    public static boolean isLinux() {
        return checkOsNameProperty("linux");
    }

    public static boolean isMac() {
        return checkOsNameProperty("mac");
    }
}
