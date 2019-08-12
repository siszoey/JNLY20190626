package com.titan.jnly.task.bean;

import com.lib.bandaid.data.remote.entity.TTFileResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class DataSync implements Serializable {

    private String UserId;

    private Map GSMM;

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

    public List<TTFileResult> getImages() {
        return images;
    }

    public void setImages(List<TTFileResult> images) {
        this.images = images;
    }
}
