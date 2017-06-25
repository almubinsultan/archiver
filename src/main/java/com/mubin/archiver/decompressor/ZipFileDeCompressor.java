package com.mubin.archiver.decompressor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author mubin
 * @since 6/26/17
 */
public class ZipFileDeCompressor extends AsyncFileDeCompressor {


    public ZipFileDeCompressor(Path inputFile, Path outputFile) {
        super(inputFile, outputFile);
    }

    @Override
    protected Integer decompress() throws IOException {
        System.out.printf("inflating: %s \n", inputFile);

        Files.copy(inputFile, outputFile);

        return 0;
    }
}
