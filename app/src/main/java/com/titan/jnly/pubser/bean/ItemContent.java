package com.titan.jnly.pubser.bean;

import java.io.Serializable;
import java.util.List;

public class ItemContent implements Serializable {

    private List<String> FJ;

    private Regulation PoliciesRegulation;


    public List<String> getFJ() {
        return FJ;
    }

    public void setFJ(List<String> FJ) {
        this.FJ = FJ;
    }

    public Regulation getPoliciesRegulation() {
        return PoliciesRegulation;
    }

    public void setPoliciesRegulation(Regulation policiesRegulation) {
        PoliciesRegulation = policiesRegulation;
    }

    public class Regulation implements Serializable {

        private String Id;

        private String Title;

        private String PublishContent;

        private String UserId;

        private String Fbsj;

        private Integer Category;

        private Integer VALID;

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

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String userId) {
            UserId = userId;
        }

        public String getFbsj() {
            return Fbsj;
        }

        public void setFbsj(String fbsj) {
            Fbsj = fbsj;
        }

        public Integer getCategory() {
            return Category;
        }

        public void setCategory(Integer category) {
            Category = category;
        }

        public Integer getVALID() {
            return VALID;
        }

        public void setVALID(Integer VALID) {
            this.VALID = VALID;
        }
    }

}
