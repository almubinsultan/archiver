package com.mubin;

import com.mubin.archiver.compressor.ZipCompressor;
import com.mubin.archiver.decompressor.ZipDeCompressor;

public class ArchiverTestMain {

    public static void main(String[] args) {
        testZip();
    }

    private static void testZip() {
        try {

            String inputFilePath = "/Users/mubin/testzip/inputSource";
            String outputFilePath = "/Users/mubin/testzip/outputSource.zip";
            ZipCompressor zipCompressor = new ZipCompressor(inputFilePath,
                    outputFilePath,
                    Runtime.getRuntime().availableProcessors(),
                    1024 * 10); //1024 * 1024 * 50 (50mb)

//            zipCompressor.process();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String inputFilePath = "/Users/mubin/testzip/outputSource.zip";
            String outputFilePath = "/Users/mubin/testzip/inputSource-extracted";

            ZipDeCompressor zipDeCompressor = new ZipDeCompressor(inputFilePath,
                    outputFilePath,
                    Runtime.getRuntime().availableProcessors());

            zipDeCompressor.process();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
