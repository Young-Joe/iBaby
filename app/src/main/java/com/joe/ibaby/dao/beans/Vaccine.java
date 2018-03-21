package com.joe.ibaby.dao.beans;

/**
 * @author qiaojianfeng on 18/3/16.
 */

public class Vaccine extends BaseBean{

    /**
     * 疫苗名称
     */
    private String vaccine;
    /**
     * 接种年龄
     */
    private String age;
    /**
     * 接种天数
     */
    private int age2day;
    /**
     * 接种次数
     * @default 1
     */
    private String times;
    /**
     * 疫苗信息
     */
    private String info;
    /**
     * 禁忌
     */
    private String warn;
    /**
     * 注意事项
     */
    private String attention;

    /**
     * 价格
     */
    private String price;

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getAge2day() {
        return age2day;
    }

    public void setAge2day(int age2day) {
        this.age2day = age2day;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getWarn() {
        return warn;
    }

    public void setWarn(String warn) {
        this.warn = warn;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
