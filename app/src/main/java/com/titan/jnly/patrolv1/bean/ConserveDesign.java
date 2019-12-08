package com.titan.jnly.patrolv1.bean;

import com.lib.bandaid.data.local.sqlite.core.annotation.Column;
import com.lib.bandaid.data.local.sqlite.core.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/*养护任务表*/
@Table(name = "TB_ConserveTask")
public class ConserveDesign implements Serializable {

    @Column(isPKey = true, type = "varchar(32)", alias = "UUID主键")
    private String Id;

    @Column(alias = "古树编号")
    private String GSBH;

    @Column(alias = "电子标签号")
    private String DZBQH;

    @Column(alias = "任务号")
    private String RWH;

    @Column(alias = "县（市、区）")
    private String XIAN;

    @Column(alias = "县（市、区）")
    private String XIAN_N;

    @Column(alias = "乡镇（街道）")
    private String XIANG;

    @Column(alias = "乡镇（街道）")
    private String XIANG_N;

    @Column(alias = "村（居委会）")
    private String CUN;

    @Column(alias = "村（居委会）")
    private String CUN_N;

    @Column(alias = "小地名")
    private String XDM;

    @Column(alias = "树种中文名")
    private String SZZWM;

    @Column(alias = "树种拉丁名")
    private String SZLDM;

    @Column(alias = "树种俗名")
    private String SZSM;

    @Column(alias = "树种科")
    private String SZK;

    @Column(alias = "树种属")
    private String SZS;

    @Column(alias = "经度")
    private Double LON;

    @Column(alias = "纬度")
    private Double LAT;

    @Column(alias = "建设单位")
    private String JSDW;

    @Column(alias = "设计单位")
    private String SJDW;

    @Column(alias = "监理单位")
    private String JLDW;

    @Column(alias = "养护单位")
    private String YHDW;

    @Column(alias = "填写人")
    private String TXR;

    @Column(alias = "填写人ID")
    private String TXRID;

    @Column(alias = "填写时间")
    private Date TXSJ;

    @Column(alias = "数据状态")
    private Integer VALID = 1;

    @Column(alias = "巡查员")
    private String XCYID;

    @Column(alias = "古树标识")
    private String OBJECTID;

    @Column(alias = "养护任务标识 0,1,2,3")
    private Integer TASKMARK;

    @Column(alias = "养护任务标识描述")
    public String TaskMarkDesp;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public String getXIAN_N() {
        return XIAN_N;
    }

    public void setXIAN_N(String XIAN_N) {
        this.XIAN_N = XIAN_N;
    }

    public String getXIANG() {
        return XIANG;
    }

    public void setXIANG(String XIANG) {
        this.XIANG = XIANG;
    }

    public String getXIANG_N() {
        return XIANG_N;
    }

    public void setXIANG_N(String XIANG_N) {
        this.XIANG_N = XIANG_N;
    }

    public String getCUN() {
        return CUN;
    }

    public void setCUN(String CUN) {
        this.CUN = CUN;
    }

    public String getCUN_N() {
        return CUN_N;
    }

    public void setCUN_N(String CUN_N) {
        this.CUN_N = CUN_N;
    }

    public String getXDM() {
        return XDM;
    }

    public void setXDM(String XDM) {
        this.XDM = XDM;
    }

    public String getSZZWM() {
        return SZZWM;
    }

    public void setSZZWM(String SZZWM) {
        this.SZZWM = SZZWM;
    }

    public String getSZLDM() {
        return SZLDM;
    }

    public void setSZLDM(String SZLDM) {
        this.SZLDM = SZLDM;
    }

    public String getSZSM() {
        return SZSM;
    }

    public void setSZSM(String SZSM) {
        this.SZSM = SZSM;
    }

    public String getSZK() {
        return SZK;
    }

    public void setSZK(String SZK) {
        this.SZK = SZK;
    }

    public String getSZS() {
        return SZS;
    }

    public void setSZS(String SZS) {
        this.SZS = SZS;
    }

    public Double getLON() {
        return LON;
    }

    public void setLON(Double LON) {
        this.LON = LON;
    }

    public Double getLAT() {
        return LAT;
    }

    public void setLAT(Double LAT) {
        this.LAT = LAT;
    }

    public String getJSDW() {
        return JSDW;
    }

    public void setJSDW(String JSDW) {
        this.JSDW = JSDW;
    }

    public String getSJDW() {
        return SJDW;
    }

    public void setSJDW(String SJDW) {
        this.SJDW = SJDW;
    }

    public String getJLDW() {
        return JLDW;
    }

    public void setJLDW(String JLDW) {
        this.JLDW = JLDW;
    }

    public String getYHDW() {
        return YHDW;
    }

    public void setYHDW(String YHDW) {
        this.YHDW = YHDW;
    }

    public String getTXR() {
        return TXR;
    }

    public void setTXR(String TXR) {
        this.TXR = TXR;
    }

    public String getTXRID() {
        return TXRID;
    }

    public void setTXRID(String TXRID) {
        this.TXRID = TXRID;
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

    public String getXCYID() {
        return XCYID;
    }

    public void setXCYID(String XCYID) {
        this.XCYID = XCYID;
    }

    public String getOBJECTID() {
        return OBJECTID;
    }

    public void setOBJECTID(String OBJECTID) {
        this.OBJECTID = OBJECTID;
    }

    public Integer getTASKMARK() {
        return TASKMARK;
    }

    public void setTASKMARK(Integer TASKMARK) {
        this.TASKMARK = TASKMARK;
    }

    public String getTaskMarkDesp() {
        return TaskMarkDesp;
    }

    public void setTaskMarkDesp(String taskMarkDesp) {
        TaskMarkDesp = taskMarkDesp;
    }

    @Override
    public String toString() {
        return "ConserveTask{" +
                "Id='" + Id + '\'' +
                ", GSBH='" + GSBH + '\'' +
                ", DZBQH='" + DZBQH + '\'' +
                ", RWH='" + RWH + '\'' +
                ", XIAN='" + XIAN + '\'' +
                ", XIAN_N='" + XIAN_N + '\'' +
                ", XIANG='" + XIANG + '\'' +
                ", XIANG_N='" + XIANG_N + '\'' +
                ", CUN='" + CUN + '\'' +
                ", CUN_N='" + CUN_N + '\'' +
                ", XDM='" + XDM + '\'' +
                ", SZZWM='" + SZZWM + '\'' +
                ", SZLDM='" + SZLDM + '\'' +
                ", SZSM='" + SZSM + '\'' +
                ", SZK='" + SZK + '\'' +
                ", SZS='" + SZS + '\'' +
                ", LON=" + LON +
                ", LAT=" + LAT +
                ", JSDW='" + JSDW + '\'' +
                ", SJDW='" + SJDW + '\'' +
                ", JLDW='" + JLDW + '\'' +
                ", YHDW='" + YHDW + '\'' +
                ", TXR='" + TXR + '\'' +
                ", TXRID='" + TXRID + '\'' +
                ", TXSJ=" + TXSJ +
                ", VALID=" + VALID +
                ", XCYID='" + XCYID + '\'' +
                ", OBJECTID='" + OBJECTID + '\'' +
                ", TASKMARK=" + TASKMARK +
                ", TaskMarkDesp='" + TaskMarkDesp + '\'' +
                '}';
    }
}
