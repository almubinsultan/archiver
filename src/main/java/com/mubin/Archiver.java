package com.mubin;

import com.mubin.archiver.compressor.ZipCompressor;
import com.mubin.archiver.decompressor.ZipDeCompressor;

/**
 * @author mubin
 * @since 6/24/17
 */
public class Archiver {

    public static void main(String[] args) {

        if (args == null || args.length < 2) {
            System.out.println("For compression please provide inputPath/outputPath/maxFileSize");
            System.out.println("For decompression please provide inputPath/outputPath");

            return;
        }

        String inputPath = args[0];
        String outputPath = args[1];
        Integer maxFileSize = null;

        if (args.length == 3) {
            try {
                maxFileSize = Integer.parseInt(args[2]);
                maxFileSize = maxFileSize * 1024 * 1024;
            } catch (NumberFormatException e) {
                System.out.printf("Invalid maxFileSize provided");

                return;
            }
        }

        boolean compression = maxFileSize != null;
        int noOfThreads = Runtime.getRuntime().availableProcessors();

        if (compression) {
            new ZipCompressor(inputPath, outputPath, noOfThreads, maxFileSize)
                    .process();
        } else {
            new ZipDeCompressor(inputPath, outputPath, noOfThreads)
                    .process();
        }
    }
}
