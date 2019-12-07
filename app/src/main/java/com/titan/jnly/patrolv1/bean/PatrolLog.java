package com.titan.jnly.patrolv1.bean;

import com.lib.bandaid.data.local.sqlite.core.annotation.Column;
import com.lib.bandaid.data.local.sqlite.core.annotation.Table;

import java.io.Serializable;
import java.util.Date;

@Table(name = "TB_PatrolLog")
public class PatrolLog implements Serializable {

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

    @Column(alias = "生长势")
    private String SZS;

    @Column(alias = "生长势描述")
    private String SZSDesp;

    @Column(alias = "生长环境")
    private String SZHJ;

    @Column(alias = "生长环境描述")
    private String SZHJDesp;

    @Column(alias = "保护现状")
    private String BHXZ;

    @Column(alias = "养护现状")
    private String YHXZ;

    @Column(alias = "巡查问题")
    private String PatrolContent;

    @Column(alias = "巡查信息级别")
    private String PatrolLevel;

    @Column(alias = "巡查信息级别描述")
    private String PatrolLevelDesp;

    @Column(alias = "巡查单位")
    private String PatrolDep;

    @Column(alias = "巡查员")
    private String PatrolUser;

    @Column(alias = "巡查时间")
    private Date PatrolDate;

    @Column(alias = "事件上报人Id")
    private String UserId;

    @Column(alias = "填写时间")
    private Date TXSJ;

    @Column(alias = "数据状态")
    private Integer VALID = 1;

    @Column(alias = "巡查日志审核状态")
    private Integer PatrolLogStatus;

    @Column(alias = "巡查日志审核状态描述")
    private String PatrolLogStatusDesp;

    @Column(alias = "巡查日志审核结果")
    private Integer PatrolLogResult;

    @Column(alias = "巡查日志审核结果描述")
    private Integer PatrolLogResultDesp;

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

    public String getSZS() {
        return SZS;
    }

    public void setSZS(String SZS) {
        this.SZS = SZS;
    }

    public String getSZSDesp() {
        return SZSDesp;
    }

    public void setSZSDesp(String SZSDesp) {
        this.SZSDesp = SZSDesp;
    }

    public String getSZHJ() {
        return SZHJ;
    }

    public void setSZHJ(String SZHJ) {
        this.SZHJ = SZHJ;
    }

    public String getSZHJDesp() {
        return SZHJDesp;
    }

    public void setSZHJDesp(String SZHJDesp) {
        this.SZHJDesp = SZHJDesp;
    }

    public String getBHXZ() {
        return BHXZ;
    }

    public void setBHXZ(String BHXZ) {
        this.BHXZ = BHXZ;
    }

    public String getYHXZ() {
        return YHXZ;
    }

    public void setYHXZ(String YHXZ) {
        this.YHXZ = YHXZ;
    }

    public String getPatrolContent() {
        return PatrolContent;
    }

    public void setPatrolContent(String patrolContent) {
        PatrolContent = patrolContent;
    }

    public String getPatrolLevel() {
        return PatrolLevel;
    }

    public void setPatrolLevel(String patrolLevel) {
        PatrolLevel = patrolLevel;
    }

    public String getPatrolLevelDesp() {
        return PatrolLevelDesp;
    }

    public void setPatrolLevelDesp(String patrolLevelDesp) {
        PatrolLevelDesp = patrolLevelDesp;
    }

    public String getPatrolDep() {
        return PatrolDep;
    }

    public void setPatrolDep(String patrolDep) {
        PatrolDep = patrolDep;
    }

    public String getPatrolUser() {
        return PatrolUser;
    }

    public void setPatrolUser(String patrolUser) {
        PatrolUser = patrolUser;
    }

    public Date getPatrolDate() {
        return PatrolDate;
    }

    public void setPatrolDate(Date patrolDate) {
        PatrolDate = patrolDate;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
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

    public Integer getPatrolLogStatus() {
        return PatrolLogStatus;
    }

    public void setPatrolLogStatus(Integer patrolLogStatus) {
        PatrolLogStatus = patrolLogStatus;
    }

    public String getPatrolLogStatusDesp() {
        return PatrolLogStatusDesp;
    }

    public void setPatrolLogStatusDesp(String patrolLogStatusDesp) {
        PatrolLogStatusDesp = patrolLogStatusDesp;
    }

    public Integer getPatrolLogResult() {
        return PatrolLogResult;
    }

    public void setPatrolLogResult(Integer patrolLogResult) {
        PatrolLogResult = patrolLogResult;
    }

    public Integer getPatrolLogResultDesp() {
        return PatrolLogResultDesp;
    }

    public void setPatrolLogResultDesp(Integer patrolLogResultDesp) {
        PatrolLogResultDesp = patrolLogResultDesp;
    }

    @Override
    public String toString() {
        return "PatrolLog{" +
                "Id='" + Id + '\'' +
                ", XCRWID='" + XCRWID + '\'' +
                ", DZBQH='" + DZBQH + '\'' +
                ", SZK='" + SZK + '\'' +
                ", GSBH='" + GSBH + '\'' +
                ", RWH='" + RWH + '\'' +
                ", XIAN='" + XIAN + '\'' +
                ", SZS='" + SZS + '\'' +
                ", SZSDesp='" + SZSDesp + '\'' +
                ", SZHJ='" + SZHJ + '\'' +
                ", SZHJDesp='" + SZHJDesp + '\'' +
                ", BHXZ='" + BHXZ + '\'' +
                ", YHXZ='" + YHXZ + '\'' +
                ", PatrolContent='" + PatrolContent + '\'' +
                ", PatrolLevel='" + PatrolLevel + '\'' +
                ", PatrolLevelDesp='" + PatrolLevelDesp + '\'' +
                ", PatrolDep='" + PatrolDep + '\'' +
                ", PatrolUser='" + PatrolUser + '\'' +
                ", PatrolDate=" + PatrolDate +
                ", UserId='" + UserId + '\'' +
                ", TXSJ=" + TXSJ +
                ", VALID=" + VALID +
                ", PatrolLogStatus=" + PatrolLogStatus +
                ", PatrolLogStatusDesp='" + PatrolLogStatusDesp + '\'' +
                ", PatrolLogResult=" + PatrolLogResult +
                ", PatrolLogResultDesp=" + PatrolLogResultDesp +
                '}';
    }
}
