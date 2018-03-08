/**
 * ============================================================
 * 版权		：北京嘉和美康信息技术有限公司 版权所有 (c) 2013
 * 创建人		：刘佳宽  liujiakuan@bjgoodwill.com 研发中心
 * 创建日期 	：2013-12-2 10:12:08
 * ============================================================
 */
package com.bjgoodwill.ibaby.dao.beans;

import java.io.Serializable;

public class BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int UPLOAD_NO = 1;// default
	public static final int UPLOAD_ED = 0;
	public static final int DELETE_NO = 0; // default
	public static final int DELETE_ED = 1;
	public static final int UPLOAD_NUM_LIMIT = 60;

	public static final String TEXT_EMPTY = "";
}
