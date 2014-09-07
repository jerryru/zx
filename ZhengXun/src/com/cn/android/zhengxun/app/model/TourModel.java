package com.cn.android.zhengxun.app.model;

import java.util.Date;

public class TourModel {

	public static final String Stock_State = "Stock_State";
	public static final String Appraise_State = "Appraise_State";
	public static final String Sellercode = "Sellercode";
	public static final String User_Id = "User_Id";
	public static final String Tour_Id = "Tour_Id";
	public static final String Bcompany_Id = "Bcompany_Id";
	public static final String Tour_in_time = "Tour_in_time";
	public static final String Door_Photo = "Door_Photo";
	public static final String Display_Photo = "Display_Photo";
	public static final String Tour_Remark = "Tour_Remark";
	public static final String Tour_in_lat = "Tour_in_lat";
	public static final String Tour_in_lot = "Tour_in_lot";
	public static final String Tour_in_Location = "Tour_in_Location";
	public static final String Tour_out_Time = "Tour_out_Time";
	public static final String tour_out_lat = "tour_out_lat";
	public static final String tour_out_lon = "tour_out_lon";
	public static final String Tour_out_Location = "Tour_out_Location";
	public static final String Tour_audio = "Tour_audio";
	public static final String Tour_audio_length = "Tour_audio_length";
	public static final String Tour_audio_File = "Tour_audio_File";
	public static final String Abnormal_Positioning_Remark = "Abnormal_Positioning_Remark";
	public static final String Abnormal_Positioning_Photo = "Abnormal_Positioning_Photo";
	public static final String Abnormal_Positioning_Audio = "Abnormal_Positioning_Audio";
	public static final String Abnormal_Positioning_Audio_length = "Abnormal_Positioning_Audio_length";
	public static final String Abnormal_Positioning_Audio_File = "Abnormal_Positioning_Audio_File";
	public static final String IsSynchronized = "isSynchronized";
	public static final String IS_NORMAL="isNormal";
	public static final String DEPLOYMENT = "Tour_Info";

	private int stockState;
	private int appraiseState;
	private String sellercode;
	private String userId;
	private String tourId;
	private String bcompanyId;
	private Date tourInTime;
	private byte[] door_Photo;
	private byte[] display_Photo;
	private String tour_Remark;
	private String tour_in_lat;
	private String tour_in_lot;
	private String tour_in_Location;
	private Date tour_out_Time;
	private String tourOutLat;
	private String tourOutLon;
	private String tour_out_Location;
	private byte[] tour_audio;
	private String tour_audio_file;
	private int tour_audio_length;
	private String abnormal_Positioning_Remark;

	private byte[] abnormal_Positioning_Photo;
	private byte[] abnormal_Positioning_Audio;
	private String abnormal_Positioning_Audio_File;
	private int abnormal_Positioning_Audio_length;
	private int isSynchronized;
	private int isNormal;
	public String getTour_audio_file() {
		return tour_audio_file;
	}
	public void setTour_audio_file(String tour_audio_file) {
		this.tour_audio_file = tour_audio_file;
	}
	public String getAbnormal_Positioning_Audio_File() {
		return abnormal_Positioning_Audio_File;
	}
	public void setAbnormal_Positioning_Audio_File(
			String abnormal_Positioning_Audio_File) {
		this.abnormal_Positioning_Audio_File = abnormal_Positioning_Audio_File;
	}
	public int getStockState() {
		return stockState;
	}
	public void setStockState(int stockState) {
		this.stockState = stockState;
	}
	public int getAppraiseState() {
		return appraiseState;
	}
	public void setAppraiseState(int appraiseState) {
		this.appraiseState = appraiseState;
	}
	public String getSellercode() {
		return sellercode;
	}
	public void setSellercode(String sellercode) {
		this.sellercode = sellercode;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTourId() {
		return tourId;
	}
	public void setTourId(String tourId) {
		this.tourId = tourId;
	}
	public String getBcompanyId() {
		return bcompanyId;
	}
	public void setBcompanyId(String bcompanyId) {
		this.bcompanyId = bcompanyId;
	}
	public Date getTourInTime() {
		return tourInTime;
	}
	public void setTourInTime(Date tourInTime) {
		this.tourInTime = tourInTime;
	}
	public byte[] getDoor_Photo() {
		return door_Photo;
	}
	public void setDoor_Photo(byte[] door_Photo) {
		this.door_Photo = door_Photo;
	}
	public byte[] getDisplay_Photo() {
		return display_Photo;
	}
	public void setDisplay_Photo(byte[] display_Photo) {
		this.display_Photo = display_Photo;
	}
	public String getTour_Remark() {
		return tour_Remark;
	}
	public void setTour_Remark(String tour_Remark) {
		this.tour_Remark = tour_Remark;
	}
	public String getTour_in_lat() {
		return tour_in_lat;
	}
	public void setTour_in_lat(String tour_in_lat) {
		this.tour_in_lat = tour_in_lat;
	}
	public String getTour_in_lot() {
		return tour_in_lot;
	}
	public void setTour_in_lot(String tour_in_lot) {
		this.tour_in_lot = tour_in_lot;
	}
	public String getTour_in_Location() {
		return tour_in_Location;
	}
	public void setTour_in_Location(String tour_in_Location) {
		this.tour_in_Location = tour_in_Location;
	}
	public Date getTour_out_Time() {
		return tour_out_Time;
	}
	public void setTour_out_Time(Date tour_out_Time) {
		this.tour_out_Time = tour_out_Time;
	}
	public String getTourOutLat() {
		return tourOutLat;
	}
	public void setTourOutLat(String tourOutLat) {
		this.tourOutLat = tourOutLat;
	}
	public String getTourOutLon() {
		return tourOutLon;
	}
	public void setTourOutLon(String tourOutLon) {
		this.tourOutLon = tourOutLon;
	}
	public String getTour_out_Location() {
		return tour_out_Location;
	}
	public void setTour_out_Location(String tour_out_Location) {
		this.tour_out_Location = tour_out_Location;
	}
	public byte[] getTour_audio() {
		return tour_audio;
	}
	public void setTour_audio(byte[] tour_audio) {
		this.tour_audio = tour_audio;
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
	public int getIsSynchronized() {
		return isSynchronized;
	}
	public void setIsSynchronized(int isSynchronized) {
		this.isSynchronized = isSynchronized;
	}
	public int getIsNormal() {
		return isNormal;
	}
	public void setIsNormal(int isNormal) {
		this.isNormal = isNormal;
	}
	public int getTour_audio_length() {
		return tour_audio_length;
	}
	public void setTour_audio_length(int tour_audio_length) {
		this.tour_audio_length = tour_audio_length;
	}
	public int getAbnormal_Positioning_Audio_length() {
		return abnormal_Positioning_Audio_length;
	}
	public void setAbnormal_Positioning_Audio_length(
			int abnormal_Positioning_Audio_length) {
		this.abnormal_Positioning_Audio_length = abnormal_Positioning_Audio_length;
	}
}
