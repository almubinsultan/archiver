package com.mubin.archiver.compressor;

import com.mubin.archiver.util.ZipUtils;

import java.io.IOException;
import java.nio.file.FileSystem;

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
        return ZipUtils.createFileSystem(outputFilePath);
    }


}
