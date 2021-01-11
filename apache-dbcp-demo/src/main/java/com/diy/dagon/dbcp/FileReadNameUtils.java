package com.diy.dagon.dbcp;

import java.io.File;

/**
 * @author dagon
 * @description: TODO
 * @date 2021/1/9-20:55
 */
public class FileReadNameUtils {
    public static void main(String[] args) {
        String filepath = "C:\\Users\\Admin\\Desktop\\babyPhotos";
        File file = new File(filepath);//File类型可以是文件也可以是文件夹
        File[] files = file.listFiles();//将该目录下的所有文件放置在一个File类型的数组中
        for (int i = 0; i < files.length; i++) {
//            // 如果还是文件夹 递归获取里面的文件 文件夹
//            if (files[i].isDirectory()) {
//                System.out.println("目录：" + files[i].getPath());
//                //getFiles(files[i].getPath());
//
//            } else {
//                System.out.println("全目录："+files[i]);
//                //System.out.println("文件：" + files[i].getName()); // files[i].getPath());
//            }
            String name = file.getName();
            System.err.println(name);

        }
    }
}
