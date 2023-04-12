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
            int buffLen = 1024;
            byte[] buff = new byte[buffLen];
            byte[] res = new byte[buffLen];

            int size = 0;
            int read = 0;
            int offset = 0;

            while(read > -1) {
                read = is.read(buff, 0, buff.length);
                size = size + read;
                if(size > res.length) {
                    byte[] tmp = new byte[size];
                    System.arraycopy(tmp, 0, res, 0, res.length);
                    res = tmp;
                }
                if (read >= 0) System.arraycopy(buff, 0, res, offset, read);
                offset = offset + read;
            }
            return res;
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
