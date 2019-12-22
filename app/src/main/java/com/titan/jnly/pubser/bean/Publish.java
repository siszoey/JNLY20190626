package com.titan.jnly.pubser.bean;

import com.zy.foxui.util.ObjectUtil;

import java.io.Serializable;
import java.util.Date;

public class Publish implements Serializable {

    /**
     * id
     */
    private String Id;

    /**
     * 主题
     */
    private String Title;

    /**
     * 文本内容
     */
    private String PublishContent;

    /**
     * 发布人
     */
    private String Publisher;

    /**
     * 发布时间
     */
    private Date Fbsj;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPublishContent() {
        return PublishContent;
    }

    public void setPublishContent(String publishContent) {
        PublishContent = publishContent;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public Date getFbsj() {
        return Fbsj;
    }

    public String getDate() {
        return ObjectUtil.Time.dateTimeToStr(Fbsj);
    }

    public void setFbsj(Date fbsj) {
        Fbsj = fbsj;
    }

    @Override
    public String toString() {
        return "Publish{" +
                "Id='" + Id + '\'' +
                ", Title='" + Title + '\'' +
                ", PublishContent='" + PublishContent + '\'' +
                ", Publisher='" + Publisher + '\'' +
                ", Fbsj=" + Fbsj +
                '}';
    }
}
