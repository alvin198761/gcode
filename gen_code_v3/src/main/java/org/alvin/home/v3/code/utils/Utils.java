package org.alvin.home.v3.code.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utils {


    public static void createZip(String sourcePath, String zipPath, boolean delete) {
        try {
            FileOutputStream fos = new FileOutputStream(zipPath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            writeZip(new File(sourcePath), "", zos, delete);
            zos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void delDir(File file) {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                delDir(subFile);
            }
        }
        file.delete();
    }

    private static void writeZip(File file, String parentPath, ZipOutputStream zos, boolean delete) {
        try {
            if (file.exists()) {
                if (file.isDirectory()) {
                    parentPath = parentPath + file.getName() + File.separator;
                    File[] files = file.listFiles();
                    if (files.length != 0) {
                        for (File f : files) {
                            writeZip(f, parentPath, zos, delete);
                        }
                    } else {
                        zos.putNextEntry(new ZipEntry(parentPath));
                    }
                } else {
                    FileInputStream fis = null;
                    fis = new FileInputStream(file);
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte[] content = new byte[1024];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        zos.write(content, 0, len);
                        zos.flush();
                    }
                    fis.close();
                }
                //删除选项
                if (delete) {
                    System.gc();
                    if (!file.delete()) {
                        file.deleteOnExit();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
