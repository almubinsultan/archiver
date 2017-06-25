package com.mubin.archiver.util;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mubin
 * @since 6/25/17
 */
public class ZipUtils {

    public static FileSystem createFileSystem(String filePath) throws IOException {
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        URI zipURI = URI.create(String.format("jar:file:%s", filePath));
        return FileSystems.newFileSystem(zipURI, env);
    }

}
