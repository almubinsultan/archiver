package com.mubin.archiver.decompressor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * @author mubin
 * @since 6/26/17
 */
public abstract class AsyncFileDeCompressor implements Callable<Integer> {

    protected Path inputFile;
    protected Path outputFile;

    public AsyncFileDeCompressor(Path inputFile, Path outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    @Override
    public Integer call() throws Exception {
        try {

            return decompress();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    protected abstract Integer decompress() throws IOException;
}
