package com.joe.ibaby.dao.beans;

import cn.bmob.v3.BmobObject;

public class BaseBean extends BmobObject {

	private static final long serialVersionUID = 1L;

	public static final int ERR_TYPE = -11;
	public static final int SERVER_EXCEPTION = 0;
	public static final int CALL_TRUE = 1;
	public static final int NET_EXCEPTION = 2;
	public static final int DATA_NULL = 3;
	//表示还有数据未上传
	public static final int DATA_UNUPLOAD = 4;
	public static final String TEXT_EMPTY = "";

	//默认性别为1 男
	public static final int DEFAULT_GENDER = 1;

	private int MsgId = ERR_TYPE;

	public boolean isDataRight() {
		return MsgId == CALL_TRUE;
	}

	public boolean isNetExcption() {
		return MsgId == NET_EXCEPTION;
	}

	public boolean isSeverExcption() {
		return MsgId == SERVER_EXCEPTION;
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

	public int getMsgId() {
		return MsgId;
	}

	public void setMsgId(int msgId) {
		MsgId = msgId;
	}

}
