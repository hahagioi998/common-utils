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
import java.util.List;
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
     * 根据长度得到格式大小(比如 : 1.81GB,1.83MB)
     *
     * @param length 文件长度
     * @return 返回一个表示文件大小的字符串 比如 1.51G、1.82MB
     */
    public static String getFormatSize(long length) {
        //换算单位
        double kb = 1024;
        double mb = kb * kb;
        double gb = kb * mb;
        double tb = kb * gb;
        if (length < kb) return length + "B";
        if (length < mb) return df.format(length / kb) + "KB";
        if (length < gb) return df.format(length / mb) + "MB";
        if (length < tb) return df.format(length / gb) + "GB";
        return df.format(length / tb) + "TB";
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
                            ZipUtils.toZip(file, os);
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
                    ZipUtils.toZip(files, out);
                    response.flushBuffer();
                }
            }
        }
    }


}
