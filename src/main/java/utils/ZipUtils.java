package utils;


import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author EalenXie Created on 2020/1/17 13:48.
 */
public class ZipUtils {

    private ZipUtils() {
    }


    /**
     * 压缩成 Zip
     *
     * @param src 需要压缩的文件
     * @param out 压缩文件输出流
     */
    public static void toZip(File src, OutputStream out) throws IOException {
        toZip(Collections.singletonList(src), out);
    }

    /**
     * 压缩成ZIP
     *
     * @param srcFiles 需要压缩的文件列表
     * @param file     输出文件
     */
    public static void toZip(List<File> srcFiles, File file) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file); ZipOutputStream zos = new ZipOutputStream(out)) {
            for (File srcFile : srcFiles) {
                compress(srcFile, zos, srcFile.getName());
            }
        }
    }

    /**
     * 压缩成ZIP
     *
     * @param srcFiles 需要压缩的文件列表
     * @param out      压缩文件输出流
     */

    public static void toZip(List<File> srcFiles, OutputStream out) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(out)) {
            for (File srcFile : srcFiles) {
                compress(srcFile, zos, srcFile.getName());
            }
        }
    }


    /**
     * 递归压缩方法
     *
     * @param sourceFile 源文件
     * @param zos        zip输出流
     * @param name       压缩后的名称
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name) throws IOException {
        byte[] buf = new byte[5 * 1024 * 1024];
        if (sourceFile.isFile()) {
            zos.putNextEntry(new ZipEntry(name));
            int len;
            try (FileInputStream in = new FileInputStream(sourceFile)) {
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
            }
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                zos.putNextEntry(new ZipEntry(name + "/"));
                zos.closeEntry();
            } else {
                for (File file : listFiles) {
                    compress(file, zos, name + "/" + file.getName());
                }
            }
        }
    }

}
