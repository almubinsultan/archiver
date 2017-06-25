package com.mubin.archiver.decompressor;

import com.mubin.archiver.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author mubin
 * @since 6/24/17
 */
public class ZipDeCompressorOld extends DeCompressor {

    public ZipDeCompressorOld(String inputFilePath, String outputFilePath) {
        super(inputFilePath, outputFilePath);
    }

    @Override
    protected void decompress() {
        byte[] buffer = new byte[1024 * 16];

        /*
         * Create output dir if not exists, if exists exit and print a message
         */

        File folder = new File(outputFilePath);

        if (folder.exists()) {
            System.out.println(outputFilePath + " already exists!");
            return;
        } else {
            folder.mkdir();
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(mergedInputFile))) {

            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {

                if (!zipEntry.isDirectory()) {

                    String fileName = zipEntry.getName();
                    File newFile = new File(outputFilePath + File.separator + fileName);

                    System.out.println("inflating: " + fileName);

                    new File(newFile.getParent()).mkdirs();

                    try (FileOutputStream fileOutputStream = new FileOutputStream(newFile)) {

                        int byteCount;
                        while (IOUtils.EOF != (byteCount = zipInputStream.read(buffer))) {
                            fileOutputStream.write(buffer, 0, byteCount);
                        }
                    }

                    zipInputStream.closeEntry();
                }

                zipEntry = zipInputStream.getNextEntry();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    @Override
    protected FileSystem createFileSystem(String outputFilePath) throws IOException {
        throw new UnsupportedOperationException();
    }
}

