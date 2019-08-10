package com.titan.jnly.login.bean;

import com.lib.bandaid.data.local.sqlite.core.annotation.Column;
import com.lib.bandaid.data.local.sqlite.core.annotation.Table;

import java.io.Serializable;

@Table(name = "TB_UserInfo")
public class UserInfo implements Serializable {

    @Column(isPKey = true)
    private String Id;

    @Column
    private String UserName;

    @Column
    private String Name;

    @Column
    private String UserJurs;

    @Column
    private String UserRoles;

    @Column
    private String UserGroup;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserJurs() {
        return UserJurs;
    }

    public void setUserJurs(String userJurs) {
        UserJurs = userJurs;
    }

    public String getUserRoles() {
        return UserRoles;
    }

    public void setUserRoles(String userRoles) {
        UserRoles = userRoles;
    }

    public String getUserGroup() {
        return UserGroup;
    }

    public void setUserGroup(String userGroup) {
        UserGroup = userGroup;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "Id='" + Id + '\'' +
                ", UserName='" + UserName + '\'' +
                ", Name='" + Name + '\'' +
                ", UserJurs='" + UserJurs + '\'' +
                ", UserRoles='" + UserRoles + '\'' +
                ", UserGroup='" + UserGroup + '\'' +
                '}';
    }
}
