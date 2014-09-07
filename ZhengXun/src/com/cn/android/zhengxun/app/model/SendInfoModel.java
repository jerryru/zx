package com.cn.android.zhengxun.app.model;

import java.util.Date;

public class SendInfoModel {
	public static final String DEPLOYMENT="Send_Info";
	public static final String SEND_ID="SendID";
	public static final String INFO_TITLE="InfoTitle";
	public static final String INFO_TYPE="InfoType";
	public static final String SENDER="Sender";
	public static final String SEND_DATE="SendDate";
	public static final String INFO_CONTENT="InfoContent";
	public static final String IS_SYN="isSynchronized";
	
	private String id;
	private String title;
	private String content;
	private Date createDate;
	private int is_synchonized;
	private String info_type;
	private String sender;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getIs_synchonized() {
		return is_synchonized;
	}
	public void setIs_synchonized(int is_synchonized) {
		this.is_synchonized = is_synchonized;
	}
	public String getInfo_type() {
		return info_type;
	}
	public void setInfo_type(String info_type) {
		this.info_type = info_type;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
}
