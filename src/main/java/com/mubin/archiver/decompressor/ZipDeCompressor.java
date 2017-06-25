package com.mubin.archiver.decompressor;

import com.mubin.archiver.util.ZipUtils;

import java.io.IOException;
import java.nio.file.FileSystem;

/**
 * @author mubin
 * @since 6/25/17
 */
public class ZipDeCompressor extends DeCompressor {

    public ZipDeCompressor(String inputFilePath, String outputFilePath, int maxThreads) {
        super(inputFilePath, outputFilePath, maxThreads);
    }

    @Override
    protected Class<? extends AsyncFileDeCompressor> asyncFileDeCompressor() {
        return ZipFileDeCompressor.class;
    }

    @Override
    protected FileSystem createFileSystem(String inputFilePath) throws IOException {
        return ZipUtils.createFileSystem(inputFilePath);
    }
}
