package com.mubin.archiver.decompressor;

import com.mubin.archiver.util.ZipUtils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author mubin
 * @since 6/25/17
 */
public class ZipDeCompressor extends DeCompressor {

    public ZipDeCompressor(String inputFilePath, String outputFilePath) {
        super(inputFilePath, outputFilePath);
    }

    @Override
    protected void decompress() throws IOException {
        final Path outputPath = Paths.get(outputFilePath);

        if (Files.exists(outputPath)) {
            System.out.println(outputFilePath + " already exists!");
            return;
        }

        Files.createDirectories(outputPath);

        try (FileSystem zipFileSystem = ZipUtils.createFileSystem(mergedInputFile.getPath())) {
            final Path root = zipFileSystem.getPath("/");

            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                    final Path destFile = Paths.get(outputPath.toString(), file.toString());

                    System.out.printf("inflating: %s ", file);

                    Files.copy(file, destFile);

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir,
                                                         BasicFileAttributes attrs) throws IOException {
                    final Path dirToCreate = Paths.get(outputPath.toString(), dir.toString());

                    if (Files.notExists(dirToCreate)) {
                        Files.createDirectory(dirToCreate);
                    }

                    return FileVisitResult.CONTINUE;
                }
            });
        }

    }
}
