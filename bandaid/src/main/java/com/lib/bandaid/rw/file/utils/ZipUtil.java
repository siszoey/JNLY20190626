package com.lib.bandaid.rw.file.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by zy on 2017/4/18.
 */

public final class ZipUtil {

    /**
     * @param zip      压缩后的文件
     * @param srcFiles 要压缩的zip文件
     */
    public static void ZipFiles(File zip, File[] srcFiles, IZipCallBack iZipCallBack) {
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zip));
            //ZipFiles(out, "", srcFiles, srcFiles, iZipCallBack);
            ZipFilesProcess(out, "", srcFiles, -1, -1, iZipCallBack);
            System.out.println("压缩完毕");
        } catch (Exception e) {
            System.out.println("解压出错");
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("关闭流出错");
                }
            }
        }
    }


    /**
     * @param out      压缩后的流文件
     * @param path
     * @param srcFiles 要压缩成zip的文件
     */
    private static long ZipFilesProcess(ZipOutputStream out, String path, File[] srcFiles, long _sumSize, long _size, IZipCallBack iZipCallBack) {
        path = path.replaceAll("\\*", "/");
        if (!path.endsWith("/")) {
            path += "/";
        }
        byte[] buf = new byte[1024];
        try {
            long sumSize = _sumSize;
            long size = _size;
            if (_sumSize == -1) {
                sumSize = 0;
            }
            if (_size == -1) {
                size = -1;
            }
            /**
             * 获取总大小
             */
            if (iZipCallBack != null) {
                for (int i = 0; i < srcFiles.length; i++) {
                    File file = srcFiles[i];
                    if (file != null) {
                        sumSize += FileUtil.getFileSize(file.getPath());
                    }
                }
            }

            for (int i = 0; i < srcFiles.length; i++) {
                if (srcFiles[i].isDirectory()) {
                    File[] files = srcFiles[i].listFiles();
                    String srcPath = srcFiles[i].getName();
                    srcPath = srcPath.replaceAll("\\*", "/");
                    if (!srcPath.endsWith("/")) {
                        srcPath += "/";
                    }
                    out.putNextEntry(new ZipEntry(path + srcPath));
                    size = ZipFilesProcess(out, path + srcPath, files, sumSize, size, iZipCallBack);
                } else {
                    FileInputStream in = new FileInputStream(srcFiles[i]);
                    String temp = path + srcFiles[i].getName();
                    System.out.println(temp);
                    out.putNextEntry(new ZipEntry(path + srcFiles[i].getName()));
                    int len;
                    if (iZipCallBack != null) {
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                            size += len;
                            iZipCallBack.processing((float) size * 100 / sumSize);
                        }
                        System.out.println(size);
                    } else {
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                    }
                    out.closeEntry();
                    in.close();
                }
                /***
                 * 解决计算瑕疵
                 */
                if (iZipCallBack != null) {
                    if (sumSize == size) {
                        iZipCallBack.processing(100.00f);
                        iZipCallBack.success(null);
                    }
                    if (sumSize == size + 1) {
                        size = size + 1;
                    }

                }
            }
            return size;
        } catch (Exception e) {
            e.printStackTrace();
            if (iZipCallBack != null) {
                iZipCallBack.error(e);
            }
        }
        return 0;
    }


    /**
     * @param out      压缩后的流文件
     * @param path
     * @param srcFiles 要压缩成zip的文件
     */
    private static long ZipFiles(ZipOutputStream out, String path, File[] original, File[] srcFiles, IZipCallBack iZipCallBack) {
        path = path.replaceAll("\\*", "/");
        if (!path.endsWith("/")) {
            path += "/";
        }
        byte[] buf = new byte[1024];
        try {
            long sumSize = 0;
            long size = 0;
            if (iZipCallBack != null) {
                for (int i = 0; i < original.length; i++) {
                    File file = original[i];
                    if (file != null) {
                        sumSize += FileUtil.getFileSize(file.getPath());
                    }
                }
            }

            for (int i = 0; i < srcFiles.length; i++) {
                if (srcFiles[i].isDirectory()) {
                    File[] files = srcFiles[i].listFiles();
                    String srcPath = srcFiles[i].getName();
                    srcPath = srcPath.replaceAll("\\*", "/");
                    if (!srcPath.endsWith("/")) {
                        srcPath += "/";
                    }
                    out.putNextEntry(new ZipEntry(path + srcPath));
                    size += ZipFiles(out, path + srcPath, original, files, iZipCallBack);
                    System.out.println(size);
                } else {
                    FileInputStream in = new FileInputStream(srcFiles[i]);
                    String temp = path + srcFiles[i].getName();
                    System.out.println(temp);
                    out.putNextEntry(new ZipEntry(path + srcFiles[i].getName()));
                    int len;
                    if (iZipCallBack != null) {
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                            size += len;
                            iZipCallBack.processing((float) size * 100 / sumSize);
                        }
                    } else {
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                    }
                    out.closeEntry();
                    in.close();
                }
                /***
                 * 解决计算瑕疵
                 */
                if (iZipCallBack != null) {
                    if (sumSize == size) {
                        iZipCallBack.processing(100.00f);
                        iZipCallBack.success(null);
                    }
                }
            }
            return size;
        } catch (Exception e) {
            e.printStackTrace();
            if (iZipCallBack != null) {
                iZipCallBack.error(e);
            }
        }
        return 0;
    }


    //========================================================解压======================================================

    /**
     * 解压到指定目录
     *
     * @param zipPath 要解压的zip文件路径
     * @param descDir 解压后的文件路径
     * @author isea533
     */
    public static void unZipFiles(String zipPath, String descDir, IZipCallBack iZipCallBack) {
        try {
            unZipFiles(new File(zipPath), descDir, iZipCallBack);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("解压失败");
            if (iZipCallBack != null) {
                iZipCallBack.error(e);
            }
        }
    }

    /**
     * 解压文件到指定目录
     *
     * @param zipFile
     * @param descDir
     * @author isea533
     */
    @SuppressWarnings("rawtypes")
    private static void unZipFiles(File zipFile, String descDir, IZipCallBack iZipCallBack) throws IOException {
        long size = 0;
        long unZipSize = 0;
        List<String> paths = null;
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);
        if (iZipCallBack != null) {
            paths = new ArrayList<>();
            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                size += entry.getSize();
            }
        }
        int i = 0;
        InputStream in = null;
        OutputStream out = null;
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            try {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                in = zip.getInputStream(entry);
                String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                //输出文件路径信息
                System.out.println(outPath);
                paths.add(outPath);
                out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                if (iZipCallBack != null) {
                    while ((len = in.read(buf1)) > 0) {
                        unZipSize = unZipSize + len;
                        iZipCallBack.processing((float) unZipSize * 100 / size);
                        out.write(buf1, 0, len);
                    }
                } else {
                    while ((len = in.read(buf1)) > 0) {
                        out.write(buf1, 0, len);
                    }
                }
                in.close();
                out.close();
            } catch (Exception e) {
                in.close();
                out.close();
            }
        }
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
        if (zip != null) {
            zip.close();
        }
        /***
         * 解决计算瑕疵
         */
        if (iZipCallBack != null) {
            iZipCallBack.processing(100.00f);
            iZipCallBack.success(paths);
        }
        System.out.println("解压完毕");
    }

    public interface IZipCallBack {
        /**
         * 解压进度回调
         *
         * @param process
         */
        public void processing(float process);

        /**
         * 解压后的文件路径
         */
        public void success(List<String> path);

        /**
         * 解压错误回掉
         *
         * @param e
         */
        public void error(Exception e);

    }
}
