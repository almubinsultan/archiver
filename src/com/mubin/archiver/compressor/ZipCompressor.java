package com.mubin.archiver.compressor;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mubin
 * @since 6/24/17
 */
public class ZipCompressor extends Compressor {

    public ZipCompressor(String inputFilePath, String outputFilePath,
                         int maxNoOfThreadsForCompression, int maxCompressedFileSize) {
        super(inputFilePath, outputFilePath, maxNoOfThreadsForCompression, maxCompressedFileSize);
    }

    @Override
    protected Class<? extends AsyncFileCompressor> asyncFileCompressor() {
        return ZipFileCompressor.class;
    }

    @Override
    protected FileSystem createFileSystem(String outputFilePath) throws IOException {
        // setup ZipFileSystem
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        URI zipURI = URI.create(String.format("jar:file:%s", outputFilePath));
        return FileSystems.newFileSystem(zipURI, env);
    }


}
