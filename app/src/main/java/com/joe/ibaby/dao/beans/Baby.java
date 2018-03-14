package com.joe.ibaby.dao.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import cn.bmob.v3.datatype.BmobFile;

/**
 * @author qiaojianfeng on 18/3/12.
 */
@Entity
public class Baby extends BaseBean {

    @Id(autoincrement = true)
    private Long id;

    //TODO 删除主键
    @Unique
    private String userId;
    private String babyName;
    private String babyBirth;
    // bmob主键
    private String bmobObjId;
    //性别 1 男 2 女
    private int gender = DEFAULT_GENDER;
    private String age = TEXT_EMPTY;

    @Transient
    private BmobFile babyPic;


    @Generated(hash = 150777722)
    public Baby(Long id, String userId, String babyName, String babyBirth,
            String bmobObjId, int gender, String age) {
        this.id = id;
        this.userId = userId;
        this.babyName = babyName;
        this.babyBirth = babyBirth;
        this.bmobObjId = bmobObjId;
        this.gender = gender;
        this.age = age;
    }
    @Generated(hash = 1845915441)
    public Baby() {
    }


    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getBabyName() {
        return this.babyName;
    }
    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }
    public String getBabyBirth() {
        return this.babyBirth;
    }
    public void setBabyBirth(String babyBirth) {
        this.babyBirth = babyBirth;
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
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public BmobFile getBabyPic() {
        return babyPic;
    }

    public void setBabyPic(BmobFile babyPic) {
        this.babyPic = babyPic;
    }
}
