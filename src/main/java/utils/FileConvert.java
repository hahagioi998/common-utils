package utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author EalenXie Created on 2019/10/11 11:33.
 */
public class FileConvert {


    private FileConvert() {
    }

    private static final Logger log = LoggerFactory.getLogger(FileConvert.class);

    private static DecimalFormat df;


    static {
        // 设置数字格式，保留一位有效小数
        df = new DecimalFormat("#0.00");
        //四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        //设置数字的分数部分中允许的最小位数。
        df.setMinimumFractionDigits(2);
        //设置数字的分数部分中允许的最大位数。
        df.setMaximumFractionDigits(2);
    }


    /**
     * 根据文件长度得到文件大小(比如 : 1.81GB,1.83MB)
     *
     * @param length 文件长度
     * @return 返回一个表示文件大小的字符串 比如 1.51G、1.82MB
     */
    public static String getFormatFileSize(long length) {
        double size = ((double) length) / (1 << 30);
        if (size >= 1) return df.format(size) + "GB";
        size = ((double) length) / (1 << 20);
        if (size >= 1) return df.format(size) + "MB";
        size = ((double) length) / (1 << 10);
        if (size >= 1) return df.format(size) + "KB";
        return length + "B";
    }

    /**
     * 根据文件的全路径，获取文件后缀名,如果是文件夹类型则返回空
     *
     * @param filePath 文件路径
     * @return 文件后缀
     */
    public static String getSuffixByFilePath(String filePath) {
        if (filePath.contains(".")) {
            return filePath.substring(filePath.lastIndexOf('.'));
        } else {
            return "";
        }
    }

    /**
     * 根据文件的全路径，获取文件前缀名
     */
    public static String getPrefixByFilePath(String filePath) {
        if (filePath.contains(".")) {
            return filePath.substring(0, filePath.lastIndexOf('.'));
        } else {
            return filePath;
        }
    }


    /**
     * http 请求多文件下载
     *
     * @param files    文件内容
     * @param response http Resp
     */
    public static void filesDownLoad(List<File> files, HttpServletResponse response) throws IOException {
        if (!files.isEmpty()) {
            // 设置编码
            response.setCharacterEncoding("UTF-8");
            // 设置文件格式
            response.setContentType("application/octet-stream");
            if (files.size() == 1) {
                File file = files.get(0);
                // 判断文件是否存在
                if (file.exists()) {
                    //如果文件是一个文件夹 则也先进行压缩 ,然后再进行下载
                    if (file.isDirectory()) {
                        // 设置文件名，解决乱码
                        try (OutputStream os = response.getOutputStream()) {
                            //打包下载
                            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName() + ".zip", "UTF-8"));
                            FileConvert.toZip(file.getPath(), os);
                            response.flushBuffer();
                        }
                    } else {
                        //单文件下载
                        try (FileInputStream fileInputStream = new FileInputStream(file)) {
                            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
                            // 设置缓冲区大小
                            int bufferSize = 4096;
                            int readSize;
                            int writeSize;
                            try (FileChannel fileChannel = fileInputStream.getChannel()) {
                                // allocateDirect速度更快
                                ByteBuffer buff = ByteBuffer.allocateDirect(bufferSize);
                                while ((readSize = fileChannel.read(buff)) != -1) {
                                    if (readSize == 0) {
                                        continue;
                                    }
                                    buff.position(0);
                                    buff.limit(readSize);
                                    while (buff.hasRemaining()) {
                                        writeSize = Math.min(buff.remaining(), bufferSize);
                                        byte[] byteArr = new byte[writeSize];
                                        buff.get(byteArr, 0, writeSize);
                                        response.getOutputStream().write(byteArr);
                                    }
                                    buff.clear();
                                }
                            } catch (IOException e) {
                                log.error("printStackTrace", e);
                            }
                        }
                    }
                }
            }
            //多文件时 先压缩成一个,然后再进行下载
            if (files.size() > 1) {
                try (ZipOutputStream out = new ZipOutputStream(response.getOutputStream())) {
                    String fileNames = String.valueOf(files);
                    response.setHeader("Content-Disposition", "attachment;filename=" + fileNames);
                    FileConvert.toZip2(files, out);
                    response.flushBuffer();
                }
            }
        }
    }


    public static void toZip(String srcDir, OutputStream out) {
        toZip2(Collections.singletonList(new File(srcDir)), out);
    }


    /**
     * 压缩成ZIP 方法2
     *
     * @param srcFiles 需要压缩的文件列表
     * @param out      压缩文件输出流
     */

    public static void toZip2(List<File> srcFiles, OutputStream out) {
        long start = System.currentTimeMillis();
        try (ZipOutputStream zos = new ZipOutputStream(out)) {
            for (File srcFile : srcFiles) {
                compress(srcFile, zos, srcFile.getName());
            }
            log.info("压缩完成，耗时：{}ms", System.currentTimeMillis() - start);
        } catch (IOException e) {
            log.error("printStackTrace", e);
        }
    }


    /**
     * 递归压缩方法
     *
     * @param sourceFile 源文件
     * @param zos        zip输出流
     * @param name       压缩后的名称
     *                   <p>
     *                   false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     */

    private static void compress(File sourceFile, ZipOutputStream zos, String name) {
        byte[] buf = new byte[5 * 1024 * 1024];
        try {
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

        } catch (Exception e) {
            log.error("printStackTrace", e);
        }
    }


    /**
     * 文件流合并
     *
     * @param target 最终流合并出来的文件
     * @param source 合并文件列表(按默认排序开始合并)
     */
    public static void mergeFileIO(File target, File[] source) throws IOException {
        try (FileChannel outChannel = new FileOutputStream(target).getChannel()) {
            if (source != null && source.length != 0) {
                for (File f : source) {
                    try (FileChannel fc = new FileInputStream(f).getChannel()) {
                        ByteBuffer bb = ByteBuffer.allocate(1024 * 8);
                        while (fc.read(bb) != -1) {
                            bb.flip();
                            outChannel.write(bb);
                            bb.clear();
                        }
                    }
                }
            }
        }
    }


}
