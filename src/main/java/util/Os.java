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
public final class Os {

    private static final String OS_NAME = "os.name";
    private static final Path ETC_GROUP = Paths.get("/etc/group");
    private static final String USERNAME = System.getProperty("user.name");
    private static final String ROOT = "root";

    private static final List<Feature> FEATURES = new ArrayList<>();

    static {
        // We consider Windows to be only a testing OS only (=nonARM) as Bulldog
        // currently does not support it.
        if (isUserRoot()) {
            FEATURES.addAll(Arrays.asList(Feature.values()));
        } else if (isWindows() || isUserInGpioGroup()) {
            FEATURES.add(Feature.GPIO);
            FEATURES.add(Feature.INTERRUPTS);
        }
    }

    private Os() {
        /* do not instantiate this class  */
    }

    /**
     * For testing purposes only.
     */
    public static List<Feature> getAppFeatures() {
        return new ArrayList<>(FEATURES);
    }

    private static boolean checkOsNameProperty(String substring) {
        return System.getProperty(OS_NAME).toLowerCase().contains(substring);
    }

    private static boolean isUserInGpioGroup() {
        if (!isLinux()) {
            return false;
        }
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

    private static boolean isLinux() {
        return checkOsNameProperty("linux");
    }
}
