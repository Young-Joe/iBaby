package com.joe.ibaby.dao.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import cn.bmob.v3.datatype.BmobFile;

/**
 * @author qiaojianfeng on 18/3/8.
 */
@Entity
public class User extends BaseBean {

    @Id(autoincrement = true)
    private Long id;

    @Unique
    private String userId;
    private String password;
    private String nickName;
    //性别 1 男 2 女
    private int gender = 1;
    // bmob主键
    private String bmobObjId;

    @Transient
    private Baby baby;

    @Transient
    private BmobFile userHead;

    @Generated(hash = 534687770)
    public User(Long id, String userId, String password, String nickName,
            int gender, String bmobObjId) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.nickName = nickName;
        this.gender = gender;
        this.bmobObjId = bmobObjId;
    }

    @Generated(hash = 586692638)
    public User() {
    }

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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BmobFile getUserHead() {
        return userHead;
    }

    public void setUserHead(BmobFile userHead) {
        this.userHead = userHead;
    }

    public String getBmobObjId() {
        return this.bmobObjId;
    }

    public void setBmobObjId(String bmobObjId) {
        this.bmobObjId = bmobObjId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        if (gender != -1)  {
            this.gender = gender;
        }
    }

    public Baby getBaby() {
        return baby;
    }

    public void setBaby(Baby baby) {
        this.baby = baby;
    }
}
