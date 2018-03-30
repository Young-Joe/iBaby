package com.joe.ibaby.dao.beans;

/**
 * @author qiaojianfeng on 18/3/30.
 */

public class PushBean {

    /**
     * 默认为0
     * 0:日常推送拉起通知提醒服务
     * 1:普通通知推送
     */
    private int type = 0;

    private String title;

    private String content;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
