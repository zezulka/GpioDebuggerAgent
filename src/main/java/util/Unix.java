package util;

import protocol.Feature;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for determining which OS agent runs on.
 */
public final class Unix {

    private static final String OS_NAME = "os.name";
    private static final Path ETC_GROUP = Paths.get("/etc/group");
    private static final String USERNAME = System.getProperty("user.name");
    private static final String ROOT = "root";

    private static final List<Feature> features = new ArrayList<>();

    static {
        if (Unix.isUserRoot()) {
            features.addAll(Arrays.asList(Feature.values()));
        } else if (Unix.isUserInGpioGroup()) {
            features.add(Feature.GPIO);
            features.add(Feature.INTERRUPTS);
        }
    }

    private Unix() {
        /* do not instantiate this class  */
    }

    /**
     * For testing purposes only.
     */
    public static List<Feature> getAppFeatures() {
        return features;
    }

    private static boolean checkOsNameProperty(String substring) {
        return System.getProperty(OS_NAME).toLowerCase().contains(substring);
    }

    private static boolean isUserInGpioGroup() {
        try (BufferedReader br
                     = new BufferedReader(new FileReader(ETC_GROUP.toFile()))) {
            String nextLine;
            while ((nextLine = br.readLine()) != null) {
                if (nextLine.contains(USERNAME)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("/etc/group not found");
        }
        return false;
    }

    private static boolean isUserRoot() {
        return USERNAME.equals(ROOT);
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
