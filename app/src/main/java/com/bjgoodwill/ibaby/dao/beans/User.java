package com.bjgoodwill.ibaby.dao.beans;

import cn.bmob.v3.BmobObject;

/**
 * @author qiaojianfeng on 18/3/8.
 */

public class User extends BmobObject {

    private String userId;
    private String password;
    private String nickName;
    private String babyName;
    private String babyBirth;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    public String getBabyBirth() {
        return babyBirth;
    }

    public void setBabyBirth(String babyBirth) {
        this.babyBirth = babyBirth;
    }
}
