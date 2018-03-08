package com.bjgoodwill.ibaby.dao.beans;

/**
 * 封装消息类，初始值默认为"消极"
 */
public class JHMessage extends BaseBean {

	private static final long serialVersionUID = 1L;
	public static final int ERR_TYPE = -11;
	public static final int SERVER_EXCEPTION = 0;
	public static final int CALL_TRUE = 1;
	public static final int NET_EXCEPTION = 2;
	public static final int DATA_NULL = 3;
	//表示服务返回的数据为空 但依旧要做删除本地操作
	public static final int SERVER_DATA_NULL = -1;
	//表示还有数据未上传
	public static final int DATA_UNUPLOAD = 4;

	private int MsgId = ERR_TYPE;
	private String MsgContent = "执行失败";
	/** 接口返回的全部数据 */
	private String RetContent;
	private String MsgDateTime;
	private String Md5Value;
	private Object obj;
	private int extra;

	/**
	 * 有些现场可能会出现GOAL_SETTING_VLAUE 字段值缺失问题
	 * 用于提醒显示错误
     */
	private String SETTINGID;

	public boolean isDataRight() {
		return MsgId == CALL_TRUE;
	}

	public boolean isNetExcption() {
		return MsgId == NET_EXCEPTION;
	}

	public boolean isSeverExcption() {
		return MsgId == SERVER_EXCEPTION;
	}
	
	public boolean isSeverDataNull() {
		return MsgId == SERVER_DATA_NULL;
	}

	public boolean isDataUnUpload(){
		return MsgId == DATA_UNUPLOAD;
	}

	public void setNetException() {
		MsgId = NET_EXCEPTION;
	}

	public void setDataRight() {
		MsgId = CALL_TRUE;
	}

	public void setDataError() {
		MsgId = ERR_TYPE;
	}

	public void setDataUnUpload(){
		MsgId = DATA_UNUPLOAD;
	}

	public boolean isDataError() {
		return MsgId == ERR_TYPE;
	}

	public boolean isDataNull() {
		return MsgId == DATA_NULL;
	}

	public void setDataNull() {
		MsgId = DATA_NULL;
	}

	public int getExtra() {
		return extra;
	}

	public void setExtra(int extra) {
		this.extra = extra;
	}

	public JHMessage() {
		super();
	}

	public int getMsgId() {
		return MsgId;
	}

	public void setMsgId(int msgId) {
		MsgId = msgId;
	}

	public String getMsgContent() {
		return MsgContent;
	}

	public void setMsgContent(String msgContent) {
		MsgContent = msgContent;
	}

	public String getRetContent() {
		return RetContent;
	}

	public void setRetContent(String retContent) {
		RetContent = retContent;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getMsgDateTime() {
		return MsgDateTime;
	}

	public void setMsgDateTime(String msgDateTime) {
		MsgDateTime = msgDateTime;
	}

	public String getMd5Value() {
		return Md5Value;
	}

	public void setMd5Value(String md5Value) {
		Md5Value = md5Value;
	}

	public String getSETTINGID() {
		return SETTINGID;
	}

	public void setSETTINGID(String SETTINGID) {
		this.SETTINGID = SETTINGID;
	}
}
