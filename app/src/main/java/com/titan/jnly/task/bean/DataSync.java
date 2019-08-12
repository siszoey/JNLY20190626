package com.titan.jnly.task.bean;

import com.lib.bandaid.data.remote.entity.TTFileResult;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class DataSync implements Serializable {

    private String UserId;

    private String GSMM;

    private List<TTFileResult> images;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getGSMM() {
        return GSMM;
    }

    public void setGSMM(String GSMM) {
        this.GSMM = GSMM;
    }

    public List<TTFileResult> getImages() {
        return images;
    }

    public void setImages(List<TTFileResult> images) {
        this.images = images;
    }
}
