package com.mubin.archiver.compressor;

import com.mubin.archiver.util.IOUtils;
import com.sun.nio.zipfs.ZipFileSystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author mubin
 * @since 6/24/17
 */
public class ZipFileCompressor extends AsyncFileCompressor {

    public ZipFileCompressor(Path inputFile, FileSystem fileSystem, String rootInputFilePath) {
        super(inputFile, fileSystem, rootInputFilePath);

        if (!(fileSystem instanceof ZipFileSystem)) {
            throw new IllegalArgumentException("Not a ZipFileSystem!");
        }
    }

    @Override
    protected Long compress() throws IOException {
        String fileWithDirectory = inputFile.toString().substring(rootInputFilePath.length());
        Path path = fileSystem.getPath(fileWithDirectory);

        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        try (FileInputStream in = new FileInputStream(inputFile.toFile());
             OutputStream out = Files.newOutputStream(path)) {

            System.out.printf("deflating: %s \n", path.toString());

            return IOUtils.copy(in, out);
        }

    }

}
