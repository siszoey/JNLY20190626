package com.titan.jnly.patrolv1.bean;

import com.lib.bandaid.data.local.sqlite.core.annotation.Column;

import java.io.Serializable;
import java.util.Date;

public class PatrolMessage implements Serializable {

    @Column(isPKey = true, type = "varchar(32)", alias = "UUID主键")
    private String Id;

    @Column(alias = "巡查任务ID")
    private String XCRWID;

    @Column(alias = "古树电子编号")
    private String DZBQH;

    @Column(alias = "树种")
    private String SZK;

    @Column(alias = "古树编号")
    private String GSBH;

    @Column(alias = "任务号")
    private String RWH;

    @Column(alias = "县（市、区）")
    private String XIAN;

    @Column(alias = "消息内容")
    private String MessageContent;

    @Column(alias = "信息级别")
    private Integer MessageLevel;

    @Column(alias = "信息级别描述")
    private Integer PatrolLevelDesp;

    @Column(alias = "填写时间")
    private Date TXSJ;

    @Column(alias = "数据状态")
    private Integer VALID = 1;

    @Column(alias = "附件Id")
    private String FJ;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getXCRWID() {
        return XCRWID;
    }

    public void setXCRWID(String XCRWID) {
        this.XCRWID = XCRWID;
    }

    public String getDZBQH() {
        return DZBQH;
    }

    public void setDZBQH(String DZBQH) {
        this.DZBQH = DZBQH;
    }

    public String getSZK() {
        return SZK;
    }

    public void setSZK(String SZK) {
        this.SZK = SZK;
    }

    public String getGSBH() {
        return GSBH;
    }

    public void setGSBH(String GSBH) {
        this.GSBH = GSBH;
    }

    public String getRWH() {
        return RWH;
    }

    public void setRWH(String RWH) {
        this.RWH = RWH;
    }

    public String getXIAN() {
        return XIAN;
    }

    public void setXIAN(String XIAN) {
        this.XIAN = XIAN;
    }

    public String getMessageContent() {
        return MessageContent;
    }

    public void setMessageContent(String messageContent) {
        MessageContent = messageContent;
    }

    public Integer getMessageLevel() {
        return MessageLevel;
    }

    public void setMessageLevel(Integer messageLevel) {
        MessageLevel = messageLevel;
    }

    public Integer getPatrolLevelDesp() {
        return PatrolLevelDesp;
    }

    public void setPatrolLevelDesp(Integer patrolLevelDesp) {
        PatrolLevelDesp = patrolLevelDesp;
    }

    public Date getTXSJ() {
        return TXSJ;
    }

    public void setTXSJ(Date TXSJ) {
        this.TXSJ = TXSJ;
    }

    public Integer getVALID() {
        return VALID;
    }

    public void setVALID(Integer VALID) {
        this.VALID = VALID;
    }

    public String getFJ() {
        return FJ;
    }

    public void setFJ(String FJ) {
        this.FJ = FJ;
    }

    @Override
    public String toString() {
        return "PatrolMessage{" +
                "Id='" + Id + '\'' +
                ", XCRWID='" + XCRWID + '\'' +
                ", DZBQH='" + DZBQH + '\'' +
                ", SZK='" + SZK + '\'' +
                ", GSBH='" + GSBH + '\'' +
                ", RWH='" + RWH + '\'' +
                ", XIAN='" + XIAN + '\'' +
                ", MessageContent='" + MessageContent + '\'' +
                ", MessageLevel=" + MessageLevel +
                ", PatrolLevelDesp=" + PatrolLevelDesp +
                ", TXSJ=" + TXSJ +
                ", VALID=" + VALID +
                ", FJ='" + FJ + '\'' +
                '}';
    }
}
