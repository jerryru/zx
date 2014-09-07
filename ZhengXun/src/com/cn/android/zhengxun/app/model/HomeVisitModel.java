package com.cn.android.zhengxun.app.model;

import java.util.Date;

public class HomeVisitModel {

	public static final String Appraise_State="Appraise_State";
	public static final String SellerCode="sellerCode";
	public static final String User_Id="User_Id";
	public static final String Family_Visit_Id="Family_Visit_Id";
	public static final String Bcompany_Id="Bcompany_Id";
	public static final String Clerk_Id="Clerk_Id";
	public static final String Visit_in_Time="Visit_in_Time";
	public static final String Door_Photo="Door_Photo";
	public static final String Gift_Photo="Gift_Photo";
	public static final String Visit_Remark="Visit_Remark";
	public static final String Visit_in_Lat="Visit_in_Lat";
	public static final String Visit_in_Lon="Visit_in_Lon";
	public static final String Visit_out_Time="Visit_out_Time";
	public static final String Visit_audio="Visit_audio";
	public static final String Visit_audio_length="Visit_audio_length";
	public static final String Visit_audio_File="Visit_audio_File";
	public static final String Abnormal_Positioning_Remark="Abnormal_Positioning_Remark";
	public static final String Abnormal_Positioning_Photo="Abnormal_Positioning_Photo";
	public static final String Abnormal_Positioning_Audio="Abnormal_Positioning_Audio";
	public static final String Abnormal_Positioning_Audio_length="Abnormal_Positioning_Audio_length";
	public static final String Abnormal_Positioning_Audio_File="Abnormal_Positioning_Audio_File";
	public static final String Visit_out_Location="Visit_out_Location";
	public static final String Visit_out_Lon="Visit_out_Lon";
	public static final String Visit_out_Lat="Visit_out_Lat";
	public static final String Visit_in_Location="Visit_in_Location";
	public static final String IsSynchronized="isSynchronized";
	public static final String DEPLOYMENT = "Family_Visit_Info";
	public static final String Abnormal_in_Remark = "Abnormal_In_Remark";
	public static final String ABNORMAL_OUT_REMARK = "Abnormal_Out_Remark";

	private String abnormal_Out_Remark;
	private String abnormal_In_Remark;
	private int appraise_State;
	private String sellerCode;
	private String user_Id;
	private String family_Visit_Id;
	private String bcompany_Id;
	private String clerk_Id;
	private Date visit_in_Time;
	private byte[] door_Photo;
	private byte[] gift_Photo;
	private String visit_Remark;
	private String visit_in_Lat;
	private String visit_in_Lon;
	private Date visit_out_Time;
	private byte[] visit_audio;
	private String visit_audio_File;
	private int visit_audio_length;
	private String abnormal_Positioning_Remark;
	private byte[] abnormal_Positioning_Photo;
	private byte[] abnormal_Positioning_Audio;
	private String abnormal_Positioning_Audio_File;
	private int abnormal_Positioning_Audio_length;
	private String visit_out_Location;
	private String visit_out_Lon;
	private String visit_out_Lat;
	private String visit_in_Location;
	private int isSynchronized;
	public String getAbnormal_Out_Remark() {
		return abnormal_Out_Remark;
	}
	public void setAbnormal_Out_Remark(String abnormal_Out_Remark) {
		this.abnormal_Out_Remark = abnormal_Out_Remark;
	}
	public String getAbnormal_In_Remark() {
		return abnormal_In_Remark;
	}
	public void setAbnormal_In_Remark(String abnormal_In_Remark) {
		this.abnormal_In_Remark = abnormal_In_Remark;
	}
	public int getAppraise_State() {
		return appraise_State;
	}
	public void setAppraise_State(int appraise_State) {
		this.appraise_State = appraise_State;
	}
	public String getSellerCode() {
		return sellerCode;
	}
	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}
	public String getUser_Id() {
		return user_Id;
	}
	public void setUser_Id(String user_Id) {
		this.user_Id = user_Id;
	}
	public String getFamily_Visit_Id() {
		return family_Visit_Id;
	}
	public void setFamily_Visit_Id(String family_Visit_Id) {
		this.family_Visit_Id = family_Visit_Id;
	}
	public String getBcompany_Id() {
		return bcompany_Id;
	}
	public void setBcompany_Id(String bcompany_Id) {
		this.bcompany_Id = bcompany_Id;
	}
	public String getClerk_Id() {
		return clerk_Id;
	}
	public void setClerk_Id(String clerk_Id) {
		this.clerk_Id = clerk_Id;
	}
	public Date getVisit_in_Time() {
		return visit_in_Time;
	}
	public void setVisit_in_Time(Date visit_in_Time) {
		this.visit_in_Time = visit_in_Time;
	}
	public byte[] getDoor_Photo() {
		return door_Photo;
	}
	public void setDoor_Photo(byte[] door_Photo) {
		this.door_Photo = door_Photo;
	}
	public byte[] getGift_Photo() {
		return gift_Photo;
	}
	public void setGift_Photo(byte[] gift_Photo) {
		this.gift_Photo = gift_Photo;
	}
	public String getVisit_Remark() {
		return visit_Remark;
	}
	public void setVisit_Remark(String visit_Remark) {
		this.visit_Remark = visit_Remark;
	}
	public String getVisit_in_Lat() {
		return visit_in_Lat;
	}
	public void setVisit_in_Lat(String visit_in_Lat) {
		this.visit_in_Lat = visit_in_Lat;
	}
	public String getVisit_in_Lon() {
		return visit_in_Lon;
	}
	public void setVisit_in_Lon(String visit_in_Lon) {
		this.visit_in_Lon = visit_in_Lon;
	}
	public String getVisit_audio_File() {
		return visit_audio_File;
	}
	public void setVisit_audio_File(String visit_audio_File) {
		this.visit_audio_File = visit_audio_File;
	}
	public String getAbnormal_Positioning_Audio_File() {
		return abnormal_Positioning_Audio_File;
	}
	public void setAbnormal_Positioning_Audio_File(
			String abnormal_Positioning_Audio_File) {
		this.abnormal_Positioning_Audio_File = abnormal_Positioning_Audio_File;
	}
	public Date getVisit_out_Time() {
		return visit_out_Time;
	}
	public void setVisit_out_Time(Date visit_out_Time) {
		this.visit_out_Time = visit_out_Time;
	}
	public byte[] getVisit_audio() {
		return visit_audio;
	}
	public void setVisit_audio(byte[] visit_audio) {
		this.visit_audio = visit_audio;
	}
	public String getAbnormal_Positioning_Remark() {
		return abnormal_Positioning_Remark;
	}
	public void setAbnormal_Positioning_Remark(String abnormal_Positioning_Remark) {
		this.abnormal_Positioning_Remark = abnormal_Positioning_Remark;
	}
	public byte[] getAbnormal_Positioning_Photo() {
		return abnormal_Positioning_Photo;
	}
	public void setAbnormal_Positioning_Photo(byte[] abnormal_Positioning_Photo) {
		this.abnormal_Positioning_Photo = abnormal_Positioning_Photo;
	}
	public byte[] getAbnormal_Positioning_Audio() {
		return abnormal_Positioning_Audio;
	}
	public void setAbnormal_Positioning_Audio(byte[] abnormal_Positioning_Audio) {
		this.abnormal_Positioning_Audio = abnormal_Positioning_Audio;
	}
	public String getVisit_out_Location() {
		return visit_out_Location;
	}
	public void setVisit_out_Location(String visit_out_Location) {
		this.visit_out_Location = visit_out_Location;
	}
	public String getVisit_out_Lon() {
		return visit_out_Lon;
	}
	public void setVisit_out_Lon(String visit_out_Lon) {
		this.visit_out_Lon = visit_out_Lon;
	}
	public String getVisit_out_Lat() {
		return visit_out_Lat;
	}
	public void setVisit_out_Lat(String visit_out_Lat) {
		this.visit_out_Lat = visit_out_Lat;
	}
	public String getVisit_in_Location() {
		return visit_in_Location;
	}
	public void setVisit_in_Location(String visit_in_Location) {
		this.visit_in_Location = visit_in_Location;
	}
	public int getIsSynchronized() {
		return isSynchronized;
	}
	public void setIsSynchronized(int isSynchronized) {
		this.isSynchronized = isSynchronized;
	}
	public int getVisit_audio_length() {
		return visit_audio_length;
	}
	public void setVisit_audio_length(int visit_audio_length) {
		this.visit_audio_length = visit_audio_length;
	}
	public int getAbnormal_Positioning_Audio_length() {
		return abnormal_Positioning_Audio_length;
	}
	public void setAbnormal_Positioning_Audio_length(
			int abnormal_Positioning_Audio_length) {
		this.abnormal_Positioning_Audio_length = abnormal_Positioning_Audio_length;
	}
	
}
