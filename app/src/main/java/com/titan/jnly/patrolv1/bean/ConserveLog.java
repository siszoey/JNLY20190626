package com.titan.jnly.patrolv1.bean;

import android.content.Intent;

import com.lib.bandaid.data.local.sqlite.core.annotation.Column;

import java.io.Serializable;
import java.util.Date;

public class ConserveLog implements Serializable {

    @Column(isPKey = true, type = "varchar(32)", alias = "UUID主键")
    private String Id;

    @Column(alias = "任务ID")
    private String RWID;

    @Column(alias = "古树编号")
    private String GSBH;

    @Column(alias = "电子标签号")
    private String DZBQH;

    @Column(alias = "日志号")
    private String RZH;

    @Column(alias = "树种科")
    private String SZK;

    @Column(alias = "县（市、区）")
    private String XIAN;

    @Column(alias = "补水与排水")
    private String BSYPS;

    @Column(alias = "有害生物防治")
    private String YHSWFZ;

    @Column(alias = "土壤管理")
    private String TRGL;

    @Column(alias = "树体保护")
    private String STBH;

    @Column(alias = "环境改良")
    private String HJGL;

    @Column(alias = "施肥")
    private String SF;

    @Column(alias = "其他施工措施")
    private String QTCS;

    @Column(alias = "养护员")
    private String CONSERVE_USER;

    @Column(alias = "巡查时间")
    private Date CONSERVE_DATE;

    @Column(alias = "事件上报人Id")
    private String USER_ID;

    @Column(alias = "填写时间")
    private Date TXSJ;

    @Column(alias = "数据状态")
    private Intent VALID;

    @Column(alias = "养护日志审核状态")
    private Integer Status;

    @Column(alias = "养护日志审核状态描述")
    private String StatusDesp;

    @Column(alias = "养护日志审核结果")
    private Integer Result;

    @Column(alias = "养护日志审核结果描述")
    private String ResultDesp;

    @Column(alias = "养护单位")
    private String ConserveDEP;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getRWID() {
        return RWID;
    }

    public void setRWID(String RWID) {
        this.RWID = RWID;
    }

    public String getGSBH() {
        return GSBH;
    }

    public void setGSBH(String GSBH) {
        this.GSBH = GSBH;
    }

    public String getDZBQH() {
        return DZBQH;
    }

    public void setDZBQH(String DZBQH) {
        this.DZBQH = DZBQH;
    }

    public String getRZH() {
        return RZH;
    }

    public void setRZH(String RZH) {
        this.RZH = RZH;
    }

    public String getSZK() {
        return SZK;
    }

    public void setSZK(String SZK) {
        this.SZK = SZK;
    }

    public String getXIAN() {
        return XIAN;
    }

    public void setXIAN(String XIAN) {
        this.XIAN = XIAN;
    }

    public String getBSYPS() {
        return BSYPS;
    }

    public void setBSYPS(String BSYPS) {
        this.BSYPS = BSYPS;
    }

    public String getYHSWFZ() {
        return YHSWFZ;
    }

    public void setYHSWFZ(String YHSWFZ) {
        this.YHSWFZ = YHSWFZ;
    }

    public String getTRGL() {
        return TRGL;
    }

    public void setTRGL(String TRGL) {
        this.TRGL = TRGL;
    }

    public String getSTBH() {
        return STBH;
    }

    public void setSTBH(String STBH) {
        this.STBH = STBH;
    }

    public String getHJGL() {
        return HJGL;
    }

    public void setHJGL(String HJGL) {
        this.HJGL = HJGL;
    }

    public String getSF() {
        return SF;
    }

    public void setSF(String SF) {
        this.SF = SF;
    }

    public String getQTCS() {
        return QTCS;
    }

    public void setQTCS(String QTCS) {
        this.QTCS = QTCS;
    }

    public String getCONSERVE_USER() {
        return CONSERVE_USER;
    }

    public void setCONSERVE_USER(String CONSERVE_USER) {
        this.CONSERVE_USER = CONSERVE_USER;
    }

    public Date getCONSERVE_DATE() {
        return CONSERVE_DATE;
    }

    public void setCONSERVE_DATE(Date CONSERVE_DATE) {
        this.CONSERVE_DATE = CONSERVE_DATE;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public Date getTXSJ() {
        return TXSJ;
    }

    public void setTXSJ(Date TXSJ) {
        this.TXSJ = TXSJ;
    }

    public Intent getVALID() {
        return VALID;
    }

    public void setVALID(Intent VALID) {
        this.VALID = VALID;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public String getStatusDesp() {
        return StatusDesp;
    }

    public void setStatusDesp(String statusDesp) {
        StatusDesp = statusDesp;
    }

    public Integer getResult() {
        return Result;
    }

    public void setResult(Integer result) {
        Result = result;
    }

    public String getResultDesp() {
        return ResultDesp;
    }

    public void setResultDesp(String resultDesp) {
        ResultDesp = resultDesp;
    }

    public String getConserveDEP() {
        return ConserveDEP;
    }

    public void setConserveDEP(String conserveDEP) {
        ConserveDEP = conserveDEP;
    }

    @Override
    public String toString() {
        return "ConserveLog{" +
                "Id='" + Id + '\'' +
                ", RWID='" + RWID + '\'' +
                ", GSBH='" + GSBH + '\'' +
                ", DZBQH='" + DZBQH + '\'' +
                ", RZH='" + RZH + '\'' +
                ", SZK='" + SZK + '\'' +
                ", XIAN='" + XIAN + '\'' +
                ", BSYPS='" + BSYPS + '\'' +
                ", YHSWFZ='" + YHSWFZ + '\'' +
                ", TRGL='" + TRGL + '\'' +
                ", STBH='" + STBH + '\'' +
                ", HJGL='" + HJGL + '\'' +
                ", SF='" + SF + '\'' +
                ", QTCS='" + QTCS + '\'' +
                ", CONSERVE_USER='" + CONSERVE_USER + '\'' +
                ", CONSERVE_DATE=" + CONSERVE_DATE +
                ", USER_ID='" + USER_ID + '\'' +
                ", TXSJ=" + TXSJ +
                ", VALID=" + VALID +
                ", Status=" + Status +
                ", StatusDesp='" + StatusDesp + '\'' +
                ", Result=" + Result +
                ", ResultDesp='" + ResultDesp + '\'' +
                ", ConserveDEP='" + ConserveDEP + '\'' +
                '}';
    }
}
