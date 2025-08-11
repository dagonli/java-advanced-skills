package com.diy.dagon.smart.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentFileRenameAndPackageReplace {

    private static final int THREAD_COUNT = 100;

    public static void main(String[] args) {
        String sourceFolderPath = "D:\\IdeaProject\\work\\tob\\target\\target-api\\src\\main\\java\\com\\qihoo\\finance";
        String destinationFolderPath = "D:\\IdeaProject\\work\\tob\\target\\target-api\\src\\main\\java\\tech\\qifu\\jinke\\targets\\api";
        String oldPackage = "com.qihoo.finance";
        String newPackage = "tech.qifu.jinke.targets.api";

        try {
            concurrentRenameAndReplacePackages(sourceFolderPath, destinationFolderPath, oldPackage, newPackage);
            System.out.println("Concurrent file rename and package replace completed.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void concurrentRenameAndReplacePackages(String sourceFolderPath, String destinationFolderPath,
                                                           String oldPackage, String newPackage)
            throws IOException, InterruptedException {
        File sourceFolder = new File(sourceFolderPath);
        File destinationFolder = new File(destinationFolderPath);

        // Create the destination folder if it doesn't exist
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }

        List<File> javaFiles = listJavaFiles(sourceFolder);
        int filesPerThread = (int) Math.ceil((double) javaFiles.size() / THREAD_COUNT);

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            int startIndex = i * filesPerThread;
            int endIndex = Math.min((i + 1) * filesPerThread, javaFiles.size());
            List<File> filesToProcess = javaFiles.subList(startIndex, endIndex);

            executorService.execute(() -> {
                try {
                    processFiles(filesToProcess, destinationFolder, oldPackage, newPackage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    private static void processFiles(List<File> files, File destinationFolder, String oldPackage, String newPackage)
            throws IOException {
        for (File javaFile : files) {
            String newFileName = javaFile.getName().replaceFirst("^\\w+(?=\\.java$)", "");
            Path sourcePath = javaFile.toPath();
            Path destinationPath = new File(destinationFolder, newFileName).toPath();

            // Copy the file to the destination folder with the new name
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

            // Replace the package name in the destination file
            replacePackageName(destinationPath, oldPackage, newPackage);

            System.err.println("newFileName"+"改造完成");
        }
    }

    private static List<File> listJavaFiles(File directory) {
        List<File> javaFiles = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    javaFiles.addAll(listJavaFiles(file));
                } else if (file.getName().endsWith(".java")) {
                    javaFiles.add(file);
                }
            }
        }
        return javaFiles;
    }

    private static void replacePackageName(Path filePath, String oldPackage, String newPackage) throws IOException {
        File tempFile = new File(filePath.toString() + ".tmp");
        try (BufferedReader reader = Files.newBufferedReader(filePath);
             BufferedWriter writer = Files.newBufferedWriter(tempFile.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replaceFirst("^package\\s+" + oldPackage + "\\s*;", "package " + newPackage + ";");
                writer.write(line);
                writer.newLine();
            }
        }

        // Replace the original file with the modified temp file
        Files.delete(filePath);
        Files.move(tempFile.toPath(), filePath);
    }
}
