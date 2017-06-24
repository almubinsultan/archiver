package com.mubin;

import com.mubin.archiver.compressor.ZipCompressor;
import com.mubin.archiver.decompressor.ZipDeCompressor;

public class Main {

    public static void main(String[] args) {
        testZip();
    }

    private static void testZip() {
        try {

            String inputFilePath = "/Users/mubin/testzip/inputSource";
            String outputFilePath = "/Users/mubin/testzip/outputSource.zip";
            ZipCompressor zipCompressor = new ZipCompressor(inputFilePath,
                    outputFilePath,
                    5,
                    1024 * 5);

//            zipCompressor.process();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String inputFilePath = "/Users/mubin/testzip/outputSource.zip";
            String outputFilePath = "/Users/mubin/testzip/inputSource-extracted";

            ZipDeCompressor zipDeCompressor = new ZipDeCompressor(inputFilePath, outputFilePath);

            zipDeCompressor.process();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
