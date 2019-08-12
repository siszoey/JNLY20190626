package com.titan.jnly.task.bean;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class DataSync implements Serializable {

    private String UserId;

    private String GSMM;

    private List images;

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

    public List getImages() {
        return images;
    }

    public void setImages(List images) {
        this.images = images;
    }
}
