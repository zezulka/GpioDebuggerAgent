package net;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Util {

    private Util() {
    }

    private static final Path ETC_GROUP = Paths.get("/etc/group");
    public static final String USERNAME = System.getProperty("user.name");
    public static final String ROOT = "root";

    public static boolean isUserInGpioGroup() {
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

    public static boolean isUserRoot() {
        return USERNAME.equals(ROOT);
    }

}
