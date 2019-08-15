package com.titan.jnly.vector.bean;

import android.content.Intent;

import com.lib.bandaid.data.local.sqlite.core.annotation.Column;
import com.lib.bandaid.data.local.sqlite.core.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务序列表，用于生成调查序列号
 */
@Table(name = "TB_Work_Sequence")
public class WorkSequence implements Serializable {

    @Column(isPKey = true, type = "nvarchar(25)")
    private String sequence;

    @Column
    private String userName;

    @Column
    private Integer num;

    @Column
    private Date dateTime;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getNumFormat() {
        return String.format("%03d", num);
    }

    public Integer sequence2Num(String sequence) {
        String text = sequence.substring(10);
        return Integer.parseInt(text);
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "WorkSequence{" +
                "sequence='" + sequence + '\'' +
                ", userName='" + userName + '\'' +
                ", num=" + num +
                ", dateTime=" + dateTime +
                '}';
    }
}
