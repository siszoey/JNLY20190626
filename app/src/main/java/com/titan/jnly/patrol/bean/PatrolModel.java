package com.titan.jnly.patrol.bean;

import com.lib.bandaid.data.remote.entity.TTFileResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 巡查信息上传
 */
public class PatrolModel implements Serializable {
    private String UserId;

    private Map PatrolRecord;

    private List<TTFileResult> PatrolImgs;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public Map getPatrolRecord() {
        return PatrolRecord;
    }

    public void setPatrolRecord(Map patrolRecord) {
        PatrolRecord = patrolRecord;
    }

    public List<TTFileResult> getPatrolImgs() {
        return PatrolImgs;
    }

    public void setPatrolImgs(List<TTFileResult> patrolImgs) {
        PatrolImgs = patrolImgs;
    }
}
