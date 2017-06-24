package com.mubin.archiver.decompressor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import static java.lang.System.*;

/**
 * @author mubin
 * @since 6/24/17
 */
public abstract class DeCompressor {

    private String inputFilePath;

    protected File mergedInputFile;

    protected String outputFilePath;

    public DeCompressor(String inputFilePath, String outputFilePath) {

        this.inputFilePath = inputFilePath;

        this.outputFilePath = outputFilePath;

        String pathname = getProperty("java.io.tmpdir") + "/archiver-zip-decompression" + currentTimeMillis() + ".tmp";
        mergedInputFile = new File(pathname);
    }

    public final void process() {
        mergeFiles();

        decompress();

        mergedInputFile.delete();
    }

    protected abstract void decompress();

    private void mergeFiles() {
        File inputFileDirectory = new File(inputFilePath);
        File[] files = inputFileDirectory.listFiles();

        if (files == null) {
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(mergedInputFile);
             BufferedOutputStream mergingStream = new BufferedOutputStream(fos)) {

            for (File file : files) {
                Files.copy(file.toPath(), mergingStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
