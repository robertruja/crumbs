package org.crumbs.core.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {
    private IOUtils() {
    }

    public static byte[] readInputStream(InputStream is) {
        try {
            int max_read = 1024;
            int totalRead = 0;

            byte[] buffer = new byte[2 * max_read];
            int offset = 0;

            while (true) {
                int read = is.read(buffer, offset, max_read);
                totalRead += read;
                offset = offset + max_read;
                if (read == max_read) {
                    if (offset == buffer.length) {
                        // ensure capacity
                        byte[] doubleBuffer = new byte[buffer.length * 2];
                        for (int i = 0; i < buffer.length; i++) {
                            doubleBuffer[i] = buffer[i];
                        }
                        buffer = doubleBuffer;
                    }
                } else {
                    byte[] result = new byte[totalRead];
                    for (int i = 0; i < totalRead; i++) {
                        result[i] = buffer[i];
                    }
                    return result;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeToOutputStream(byte[] input, OutputStream out) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        writeIO(bais, out);
    }

    public static void writeIO(InputStream payloadInput, OutputStream out) throws IOException {
        byte[] buf = new byte[8192];
        int length;
        while ((length = payloadInput.read(buf)) > 0) {
            out.write(buf, 0, length);
        }
        out.close();
    }
}
