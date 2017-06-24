package com.mubin.archiver.compressor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ExecutorService;

/**
 * @author mubin
 * @since 6/24/17
 */
public class CompressorFileVisitor extends SimpleFileVisitor<Path> {

    private Class<? extends AsyncFileCompressor> compressorClazz;
    private FileSystem fileSystem;
    private ExecutorService executorService;

    private String rootInputFilePath;

    public CompressorFileVisitor(Class<? extends AsyncFileCompressor> compressorClazz,
                                 FileSystem fileSystem, ExecutorService executorService,
                                 String rootInputFilePath) {

        this.compressorClazz = compressorClazz;
        this.fileSystem = fileSystem;
        this.executorService = executorService;
        this.rootInputFilePath = rootInputFilePath;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        // processing only regular files
        if (attrs.isRegularFile()) {
            try {
                AsyncFileCompressor asyncFileCompressor = compressorClazz
                        .getDeclaredConstructor(Path.class, FileSystem.class, String.class)
                        .newInstance(file, fileSystem, rootInputFilePath);
                executorService.submit(asyncFileCompressor);
            } catch (InstantiationException | NoSuchMethodException
                    | InvocationTargetException | IllegalAccessException e) {
                throw new Error("Can not instantiate compressorClazz");
            }
        }

        return FileVisitResult.CONTINUE;
    }
}
