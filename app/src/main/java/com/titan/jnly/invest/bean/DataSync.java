package com.titan.jnly.invest.bean;

import com.lib.bandaid.data.remote.entity.TTFileResult;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class DataSync implements Serializable {

    private String UserId;

    private Map GSMM;

    private List<File> files;

    private List<TTFileResult> images;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public Map getGSMM() {
        return GSMM;
    }

    public void setGSMM(Map GSMM) {
        this.GSMM = GSMM;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public void addFile(File file) {
        if (file == null) return;
        if (files == null) files = new ArrayList<>();
        files.add(file);
    }

    public void addFile(List<File> file) {
        if (file == null || file.size() == 0) return;
        if (files == null) files = new ArrayList<>();
        files.addAll(file);
    }

    public List<TTFileResult> getImages() {
        return images;
    }

    public void setImages(List<TTFileResult> images) {
        this.images = images;
    }
}
