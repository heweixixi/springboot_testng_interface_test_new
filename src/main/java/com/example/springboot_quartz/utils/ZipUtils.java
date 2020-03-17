package com.example.springboot_quartz.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    public static void main(String[] args) {
        File source = new File("D:\\test\\");
        File target = new File("D:\\test2\\new.zip");
        try {
            ZipOutputStream zo = new ZipOutputStream(new FileOutputStream(target));
            compress(source,zo);
            //切记，最后的ZipOutputStream流一定要关闭，要不然导出的压缩文件会报错
            zo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //生成zip压缩文件
    public static void compress(File sourceFile, ZipOutputStream zo)throws IOException{
        if (sourceFile.isDirectory()){
            //按目录压缩
            compressDir(sourceFile,zo);
        }else {
            //压缩文件
            compressFile(sourceFile,zo);
        }
    }

    //压缩目录
    private static void compressDir(File dir,ZipOutputStream zo)throws IOException{
        //列出目录下所有的文件或者目录
        File[] files = dir.listFiles();

        //如果目录下为空，则直接压缩空目录
        if (files.length<1){
            ZipEntry zipEntry = new ZipEntry(dir.getName());
            zo.putNextEntry(zipEntry);
            zo.closeEntry();
        }
        for (File file : files) {
            //压缩目录下的文件或者目录
            compress(file,zo);
        }
    }

    //压缩文件
    public static void compressFile(File sourceFile,ZipOutputStream zo)throws IOException{
        ZipEntry zipEntry = new ZipEntry(sourceFile.getName());
        zo.putNextEntry(zipEntry);
        FileInputStream fi = new FileInputStream(sourceFile);
        byte[] data = new byte[1024];
        while (fi.read(data)!=-1){
            zo.write(data);
        }
        zo.closeEntry();
    }
}
