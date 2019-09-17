package com.titan.jnly.patrol.bean;

import com.lib.bandaid.data.remote.entity.TTFileResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 养护信息上传
 */
public class CureModel implements Serializable {

    private String UserId;

    private Map MaintainRecord;

    private List<TTFileResult> MaintainImgs;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public Map getMaintainRecord() {
        return MaintainRecord;
    }

    public void setMaintainRecord(Map maintainRecord) {
        MaintainRecord = maintainRecord;
    }

    public List<TTFileResult> getMaintainImgs() {
        return MaintainImgs;
    }

    public void setMaintainImgs(List<TTFileResult> maintainImgs) {
        MaintainImgs = maintainImgs;
    }
}
