package com.mubin.archiver.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author mubin
 * @since 6/24/17
 */
public class IOUtils {

    public static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 8;

    public static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        long count = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

        int byteCount;
        while (EOF != (byteCount = inputStream.read(buffer))) {
            outputStream.write(buffer, 0, byteCount);
            count += byteCount;
        }

        return count;
    }
}
