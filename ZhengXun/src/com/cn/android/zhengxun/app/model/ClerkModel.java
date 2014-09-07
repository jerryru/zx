package com.cn.android.zhengxun.app.model;

import java.io.Serializable;
import java.util.Date;

public class ClerkModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5185016993990106088L;
	public static final String BCOMPANYID = "BcompanyID";
	public static final String SELLER_CODE = "SellerCode";
	public static final String ID = "Id";
	public static final String NAME = "Name";
	public static final String SEX = "Sex";
	public static final String CLERK_TYPE = "ClerkType";
	public static final String BIRTHDAY = "Birthday";
	public static final String ADDRESS = "Address";
	public static final String TEL = "Tel";
	public static final String QQ = "QQ";
	public static final String EMAIL = "Email";
	public static final String WEIXIN = "Weixin";
	public static final String BUST_PHOTO = "BustPhoto";
	public static final String FULL_PHOTO = "FullPhoto";
	public static final String CARD_ID = "cardID";
	public static final String IS_SYNCHONIZED = "isSynchronized";
	public static final String DEPLOYMENT = "Clerk_Info";

	private String id;
	private String sex;
	private String clerkType;
	private String bcompanyID;
	private Date birthday;
	private String telphoneNum;
	private String email;
	private String qq;
	private String weiXin;
	private String cardId;
	private byte[] bustPhoto;
	private String sellerCode;
	private byte[] fullPhoto;
	private String address;
	private String name;
	private String userId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getClerkType() {
		return clerkType;
	}

	public void setClerkType(String clerkType) {
		this.clerkType = clerkType;
	}

	public String getBcompanyID() {
		return bcompanyID;
	}

	public void setBcompanyID(String bcompanyID) {
		this.bcompanyID = bcompanyID;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getTelphoneNum() {
		return telphoneNum;
	}

	public void setTelphoneNum(String telphoneNum) {
		this.telphoneNum = telphoneNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWeiXin() {
		return weiXin;
	}

	public void setWeiXin(String weiXin) {
		this.weiXin = weiXin;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public byte[] getBustPhoto() {
		return bustPhoto;
	}

	public void setBustPhoto(byte[] bustPhoto) {
		this.bustPhoto = bustPhoto;
	}

	public String getSellerCode() {
		return sellerCode;
	}

	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}

	public byte[] getFullPhoto() {
		return fullPhoto;
	}

	public void setFullPhoto(byte[] fullPhoto) {
		this.fullPhoto = fullPhoto;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
