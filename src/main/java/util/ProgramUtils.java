  package util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/*
 * Copyright 2017 miloslav.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *
 * @author Miloslav Zezulka
 */
public class ProgramUtils {

    private static File f;
    private static FileChannel channel;
    private static FileLock lock;

    public static boolean checkIfAlreadyRunning() throws IOException {
        f = new File("RingOnRequest.lock");
        if (f.exists()) {
            return true;
        }
        channel = new RandomAccessFile(f, "rw").getChannel();
        lock = channel.tryLock();
        if (lock == null) {
            channel.close();
            return true;
        }
        ShutdownHook shutdownHook = new ShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        return false;
    }

    public static void unlockFile() {
        try {
            if (lock != null) {
                lock.release();
                channel.close();
                f.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    static class ShutdownHook extends Thread {

        @Override
        public void run() {
            unlockFile();
        }
    }
}
