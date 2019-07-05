package com.lib.bandaid.rw.file.zip;


import com.lib.bandaid.rw.file.utils.FileUtil;
import com.lib.bandaid.thread.ThreadPoolUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by zy on 2017/8/2.
 */
public class CommonZip {

    private IZipCallBack iZipCallBack;
    private boolean isFirst = true;
    private long sumSize = 0;
    private long size = 0;

    public void setIZipCallBack(IZipCallBack iZipCallBack) {
        this.iZipCallBack = iZipCallBack;
    }

    /**
     * @param zip      压缩后的文件
     * @param srcFiles 要压缩的zip文件
     */
    public void zipFiles(final File zip, final File[] srcFiles) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {

                ZipOutputStream out = null;
                try {
                    System.out.println("开始压缩");
                    out = new ZipOutputStream(new FileOutputStream(zip));
                    ZipFiles(out, "112233/11", srcFiles);
                    System.out.println("压缩完毕");
                } catch (Exception e) {
                    System.out.println("压缩出错");
                    e.printStackTrace();
                    if (iZipCallBack != null) {
                        iZipCallBack.error(e);
                    }
                } finally {
                    sumSize = 0;
                    size = 0;
                    isFirst = false;
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
        });
    }


    /**
     * @param zip  压缩后的文件
     * @param maps 要压缩的zip文件
     */
    public void zipFilesV1(final File zip, final Map<File, String> maps) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {

                ZipOutputStream out = null;
                try {
                    System.out.println("开始压缩");
                    out = new ZipOutputStream(new FileOutputStream(zip));
                    ZipFilesV1(out, maps);
                    System.out.println("压缩完毕");
                    if (iZipCallBack != null) {
                        iZipCallBack.success(null);
                    }
                } catch (Exception e) {
                    System.out.println("压缩出错");
                    e.printStackTrace();
                    if (iZipCallBack != null) {
                        iZipCallBack.error(e);
                    }
                } finally {
                    sumSize = 0;
                    size = 0;
                    isFirst = false;
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
        });
    }

    /**
     * @param zip  压缩后的文件
     * @param maps 要压缩的zip文件
     */
    public void zipFiles(File zip, Map<File, String> maps, String parentPath) {

        ZipOutputStream out = null;
        try {
            System.out.println("开始压缩");
            out = new ZipOutputStream(new FileOutputStream(zip));
            ZipFiles(out, maps, parentPath);
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    System.out.println("关闭流出错");
                }
            }
            if (iZipCallBack != null) {
                iZipCallBack.success(null);
            }
            System.out.println("压缩完毕");
        } catch (Exception e) {
            try {
                Thread.sleep(1000);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            System.out.println("压缩出错");
            if (iZipCallBack != null) {
                iZipCallBack.error(e);
            }
        } finally {
            sumSize = 0;
            size = 0;
            isFirst = false;
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
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
    private void ZipFiles(ZipOutputStream out, String path, File[] srcFiles) {
        path = path.replaceAll("\\*", "/");
        if (!path.endsWith("/")) {
            path += "/";
        }
        byte[] buf = new byte[1024];
        List<String> paths = new ArrayList<>();
        try {
            if (isFirst) {
                if (iZipCallBack != null) {
                    for (int i = 0; i < srcFiles.length; i++) {
                        File file = srcFiles[i];
                        if (file != null && file.exists()) {
                            paths.add(file.getPath());
                        }
                        if (file != null) {
                            sumSize += FileUtil.getFileSize(file);
                        }
                    }
                    isFirst = false;
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
                    ZipFiles(out, path + srcPath, files);
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
                        iZipCallBack.success(paths);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (iZipCallBack != null) {
                iZipCallBack.error(e);
            }
        }
    }

    /**
     * @param out
     * @param maps
     */
    private void ZipFiles(ZipOutputStream out, Map<File, String> maps, String parentPath) {
        byte[] buf = new byte[1024];
        List<String> _paths = new ArrayList<>();
        try {
            if (isFirst) {
                if (iZipCallBack != null) {
                    for (File file : maps.keySet()) {
                        if (file != null && file.exists()) {
                            _paths.add(file.getPath());
                        }
                        if (file != null) {
                            sumSize += FileUtil.getFileSize(file);
                        }
                    }
                    isFirst = false;
                }
            }

            for (File file : maps.keySet()) {
                String path;
                if (parentPath != null && !parentPath.trim().equals("")) {
                    path = parentPath + "/" + maps.get(file);
                } else {
                    path = maps.get(file);
                }

                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    String srcPath = file.getName();
                    srcPath = srcPath.replaceAll("\\*", "/");
                    if (!srcPath.endsWith("/")) {
                        srcPath += "/";
                    }
                    out.putNextEntry(new ZipEntry(path + srcPath));
                    ZipFiles(out, path + srcPath, files);
                } else {
                    FileInputStream in = new FileInputStream(file);
                    String temp = path + file.getName();
                    System.out.println(temp);
                    out.putNextEntry(new ZipEntry(path + file.getName()));
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
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (iZipCallBack != null) {
                iZipCallBack.error(e);
            }
        }
    }

    /**
     * @param out
     * @param maps
     */
    private void ZipFilesV1(ZipOutputStream out, Map<File, String> maps) {
        byte[] buf = new byte[1024];
        List<String> _paths = new ArrayList<>();
        try {
            if (isFirst) {
                if (iZipCallBack != null) {
                    for (File file : maps.keySet()) {
                        if (file != null && file.exists()) {
                            _paths.add(file.getPath());
                        }
                        if (file != null) {
                            sumSize += FileUtil.getFileSize(file);
                        }
                    }
                    isFirst = false;
                }
            }

            for (File file : maps.keySet()) {
                String path = maps.get(file);
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    String srcPath = file.getName();
                    srcPath = srcPath.replaceAll("\\*", "/");
                    if (!srcPath.endsWith("/")) {
                        srcPath += "/";
                    }
                    out.putNextEntry(new ZipEntry(path + srcPath));
                    ZipFiles(out, path + srcPath, files);
                } else {
                    FileInputStream in = new FileInputStream(file);
                    String temp = path + file.getName();
                    System.out.println(temp);
                    out.putNextEntry(new ZipEntry(path + file.getName()));
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
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (iZipCallBack != null) {
                iZipCallBack.error(e);
            }
        }
    }

    //==================================================================================================================

    /**
     * 解压到指定目录
     *
     * @param zipPath 要解压的zip文件路径
     * @param descDir 解压后的文件路径
     * @author isea533
     */
    public void unZipFiles(String zipPath, String descDir, IZipCallBack iZipCallBack) {
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
    private void unZipFiles(File zipFile, String descDir, IZipCallBack iZipCallBack) throws IOException {
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


    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     *
     * @param sourceFilePath :待压缩的文件路径
     * @param zipFilePath    :压缩后存放路径
     * @param fileName       :压缩后文件的名称
     * @return
     */
    public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if (sourceFile.exists() == false) {
            System.out.println("待压缩的文件目录：" + sourceFilePath + "不存在.");
        } else {
            try {
                File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
                if (zipFile.exists()) {
                    System.out.println(zipFilePath + "目录下存在名字为:" + fileName + ".zip" + "打包文件.");
                } else {
                    File[] sourceFiles = sourceFile.listFiles();
                    if (null == sourceFiles || sourceFiles.length < 1) {
                        System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
                        return false;
                    } else {
                        fos = new FileOutputStream(zipFile);
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));
                        byte[] bufs = new byte[1024 * 10];
                        for (int i = 0; i < sourceFiles.length; i++) {
                            //创建ZIP实体，并添加进压缩包
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                            zos.putNextEntry(zipEntry);
                            //读取待压缩的文件并写进压缩包里
                            fis = new FileInputStream(sourceFiles[i]);
                            bis = new BufferedInputStream(fis, 1024 * 10);
                            int read = 0;
                            while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                                zos.write(bufs, 0, read);
                            }
                        }
                        flag = true;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                //关闭流
                try {
                    if (null != bis) bis.close();
                    if (null != zos) zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("压缩完毕！");
        return flag;
    }

}
