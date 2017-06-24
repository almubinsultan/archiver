package com.mubin.archiver.compressor;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * @author mubin
 * @since 6/24/17
 */
public abstract class AsyncFileCompressor implements Callable<Long> {

    protected Path inputFile;
    protected FileSystem fileSystem;

    protected String rootInputFilePath;

    public AsyncFileCompressor(Path inputFile, FileSystem fileSystem,
                               String rootInputFilePath) {
        this.inputFile = inputFile;
        this.fileSystem = fileSystem;
        this.rootInputFilePath = rootInputFilePath;
    }

    @Override
    public final Long call() throws Exception {
        try {
            return compress();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }


    protected abstract Long compress() throws IOException;
}
