package com.mubin.archiver.decompressor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ExecutorService;

/**
 * @author mubin
 * @since 6/24/17
 */
public class DeCompressorFileVisitor extends SimpleFileVisitor<Path> {

    private Class<? extends AsyncFileDeCompressor> deCompressorClazz;
    private ExecutorService executorService;
    private Path outputPath;

    public DeCompressorFileVisitor(Class<? extends AsyncFileDeCompressor> deCompressorClazz,
                                   FileSystem zipFileSystem,
                                   ExecutorService executorService,
                                   String outputFilePath) {

        this.deCompressorClazz = deCompressorClazz;
        this.executorService = executorService;
        this.outputPath = zipFileSystem.getPath(outputFilePath);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        try {
            Path destOutputFile = Paths.get(outputPath.toString(), file.toString());

            AsyncFileDeCompressor asyncFileDeCompressor = deCompressorClazz
                    .getDeclaredConstructor(Path.class, Path.class)
                    .newInstance(file, destOutputFile);

            executorService.submit(asyncFileDeCompressor);
        } catch (InstantiationException | NoSuchMethodException
                | InvocationTargetException | IllegalAccessException e) {
            throw new Error("Can not instantiate deCompressorClazz");
        }

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
}
