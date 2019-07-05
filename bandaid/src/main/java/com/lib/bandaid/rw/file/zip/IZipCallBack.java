package com.lib.bandaid.rw.file.zip;

import java.util.List;

/**
 * Created by zy on 2017/8/2.
 */
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
