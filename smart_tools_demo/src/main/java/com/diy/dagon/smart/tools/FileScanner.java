package com.diy.dagon.smart.tools;

import java.io.File;

public class FileScanner {
    public static void main(String[] args) {
        //String folderPath = "D:\\IdeaProject\\work\\btl\\btl-app\\src\\main\\java\\com\\qihoo\\finance\\btl\\modules\\task"; // 替换为要扫描的文件夹路径
        String folderPath = "D:\\IdeaProject\\work\\tob\\target";

        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("指定路径不是一个有效的文件夹路径");
            return;
        }

        scanFiles(folder);
    }

    private static void scanFiles(File folder) {
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }

        int totalNums = 0;
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                String fileNameWithoutExtension = getFileNameWithoutExtension(fileName);
                if (fileNameWithoutExtension.endsWith("Task")) {
                    totalNums++;
                    System.out.println(fileNameWithoutExtension);
                }
            } else if (file.isDirectory()) {
                scanFiles(file); // 递归扫描子文件夹
            }
        }

        //System.err.println("找到满足条件的记录数:" + totalNums);
    }

    private static String getFileNameWithoutExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1) {
            return fileName.substring(0, dotIndex);
        }
        return fileName;
    }
}