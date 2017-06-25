package com.mubin.archiver.decompressor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.getProperty;

/**
 * @author mubin
 * @since 6/24/17
 */
public abstract class DeCompressor {

    private String inputFilePath;

    protected File mergedInputFile;

    protected String outputFilePath;

    private ExecutorService executorService;

    public DeCompressor(String inputFilePath, String outputFilePath, int maxThreads) {

        this.inputFilePath = inputFilePath;

        this.outputFilePath = outputFilePath;

        String pathname = getProperty("java.io.tmpdir") + "/archiver-zip-decompression" + currentTimeMillis() + ".tmp";
        mergedInputFile = new File(pathname);

        executorService = Executors.newFixedThreadPool(maxThreads);
    }

    protected abstract Class<? extends AsyncFileDeCompressor> asyncFileDeCompressor();

    protected abstract FileSystem createFileSystem(String inputFilePath) throws IOException;

    public final void process() {
        System.out.println("Starting decompression");

        mergeFiles();

        try {
            decompress();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mergedInputFile.delete();

        System.out.println("Finished!");
    }

    private void decompress() throws IOException {
        final Path outputPath = Paths.get(outputFilePath);

        if (Files.exists(outputPath)) {
            System.out.println(outputFilePath + " already exists!");
            return;
        }

        Files.createDirectories(outputPath);

        try (FileSystem deCompressorFileSystem = createFileSystem(mergedInputFile.getPath())) {

            DeCompressorFileVisitor deCompressorVisitor = new DeCompressorFileVisitor(asyncFileDeCompressor(),
                    deCompressorFileSystem, executorService, outputFilePath);

            Files.walkFileTree(deCompressorFileSystem.getPath("/"), deCompressorVisitor);

            // shutdown ExecutorService and block till tasks are complete
            executorService.shutdown();

            try {
                executorService.awaitTermination(10, TimeUnit.MINUTES); //waits 10 minutes before giving timeout
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
