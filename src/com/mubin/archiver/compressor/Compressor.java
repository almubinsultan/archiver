package com.mubin.archiver.compressor;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.getProperty;

/**
 * @author mubin
 * @since 6/24/17
 */
public abstract class Compressor {

    private String inputFilePath;
    private String outputFilePath;
    private String compressedRawOutputFilePath;

    private int maxCompressedSizeInBytes;

    private ExecutorService executorService;

    public Compressor(String inputFilePath, String outputFilePath, int maxThreads, int maxCompressedSizeInBytes) {

        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
        this.maxCompressedSizeInBytes = maxCompressedSizeInBytes;

        executorService = Executors.newFixedThreadPool(maxThreads);
        compressedRawOutputFilePath = getProperty("java.io.tmpdir") + "/archiver-zip-compression" + currentTimeMillis() + ".tmp";
    }

    protected abstract Class<? extends AsyncFileCompressor> asyncFileCompressor();

    protected abstract FileSystem createFileSystem(String outputFilePath) throws IOException;

    public final void process() {
        System.out.println("Starting compression");

        compress();

        if (maxCompressedSizeInBytes > 0) {
            System.out.println("-----------------------------------");
            System.out.println("Starting file split process");

            splitFile();
        }

        System.out.println("Finished!");
    }

    private void compress() {

        try (FileSystem compressorFileSystem = createFileSystem(compressedRawOutputFilePath)) {

            CompressorFileVisitor compressorVisitor = new CompressorFileVisitor(asyncFileCompressor(),
                    compressorFileSystem, executorService, inputFilePath);

            Files.walkFileTree(FileSystems.getDefault().getPath(inputFilePath), compressorVisitor);

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

    private void splitFile() {
        byte[] buffer = new byte[maxCompressedSizeInBytes];

        File rawOutputFile = new File(compressedRawOutputFilePath);
        String fileName = rawOutputFile.getName();

        File outputFile = new File(outputFilePath);
        outputFile.mkdir();

        //try-with-resources to ensure closing stream
        try (FileInputStream fis = new FileInputStream(rawOutputFile);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            int bytesAmount = 0;
            while ((bytesAmount = bis.read(buffer)) > 0) {
                //write each chunk of data into separate file with different number in name
                String filePartName = String.format("%s.%d", fileName, System.nanoTime());
                File newFile = new File(outputFile, filePartName);
                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, bytesAmount);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        rawOutputFile.delete();
    }

}
