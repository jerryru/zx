package com.cn.android.zhengxun.app.model;

import java.util.Date;

public class AttendenceInfoModel {
	public static final String ATTENDENCE_TOKEN="Attendance_Token";
	public static final String ATTENDENCE_ID="Attendance_Id";
	public static final String SELLER_CODE="SellerCode";
	public static final String BCOMPANY_ID="BcompanyID";
	public static final String ATTENDENCE_TYPE="Attendance_Type";
	public static final String ATTENDENCE_DATE="Attendance_datetime";
	public static final String ATTENDENCE_LAT="Attendance_lat";
	public static final String ATTENDENCE_LOT="Attendance_lot";
	public static final String ATTENDENCE_LOCATION="Attendance_loaction";
	public static final String ABNOMAL_POSITIONING_REMARK="Abnormal_Positioning_Remark";
	public static final String ABNORMAL_POSITONING_PHOTO="Abnormal_Positioning_Photo";
	public static final String ABNORMAL_POSITONING_AUDIO="Abnormal_Positioning_Audio";
	public static final String ABNORMAL_POSITONING_AUDIO_FILE="Abnormal_Positioning_Audio_File";
	public static final String IS_SYN="isSynchronized";
	public static final String DEPLOYMENT = "Attendance_Info";
	
	private String attendceId;
	private String sellerCode;
	private int attendenceType;
	private String bcompanyId;
	private String lat;
	private String lot;
	private int attendanceToken;
	private String attendanceLoaction;
	private String abnormalPositioningRemark;
	private byte[] abnorma_PositioningPhoto;
	private byte[] abnormalPositioningAudio;
	private String abnormalPositioningAudioFile;
	private Date attendenceTime;
	private String attendence_address;
	private String abnormal_Positioning_Remark;
	public int isSynchronized;
	public String getAttendceId() {
		return attendceId;
	}
	public void setAttendceId(String attendceId) {
		this.attendceId = attendceId;
	}
	public String getSellerCode() {
		return sellerCode;
	}
	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}
	public int getAttendenceType() {
		return attendenceType;
	}
	public void setAttendenceType(int attendenceType) {
		this.attendenceType = attendenceType;
	}
	public String getBcompanyId() {
		return bcompanyId;
	}
	public void setBcompanyId(String bcompanyId) {
		this.bcompanyId = bcompanyId;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLot() {
		return lot;
	}
	public void setLot(String lot) {
		this.lot = lot;
	}
	public int getAttendanceToken() {
		return attendanceToken;
	}
	public void setAttendanceToken(int attendanceToken) {
		this.attendanceToken = attendanceToken;
	}
	public String getAttendanceLoaction() {
		return attendanceLoaction;
	}
	public void setAttendanceLoaction(String attendanceLoaction) {
		this.attendanceLoaction = attendanceLoaction;
	}
	public String getAbnormalPositioningRemark() {
		return abnormalPositioningRemark;
	}
	public void setAbnormalPositioningRemark(String abnormalPositioningRemark) {
		this.abnormalPositioningRemark = abnormalPositioningRemark;
	}
	public byte[] getAbnorma_PositioningPhoto() {
		return abnorma_PositioningPhoto;
	}
	public void setAbnorma_PositioningPhoto(byte[] abnorma_PositioningPhoto) {
		this.abnorma_PositioningPhoto = abnorma_PositioningPhoto;
	}
	public byte[] getAbnormalPositioningAudio() {
		return abnormalPositioningAudio;
	}
	public void setAbnormalPositioningAudio(byte[] abnormalPositioningAudio) {
		this.abnormalPositioningAudio = abnormalPositioningAudio;
	}
	public String getAbnormalPositioningAudioFile() {
		return abnormalPositioningAudioFile;
	}
	public void setAbnormalPositioningAudioFile(
			String abnormalPositioningAudioFile) {
		this.abnormalPositioningAudioFile = abnormalPositioningAudioFile;
	}
	public Date getAttendenceTime() {
		return attendenceTime;
	}
	public void setAttendenceTime(Date attendenceTime) {
		this.attendenceTime = attendenceTime;
	}
	public String getAttendence_address() {
		return attendence_address;
	}
	public void setAttendence_address(String attendence_address) {
		this.attendence_address = attendence_address;
	}
	public String getAbnormal_Positioning_Remark() {
		return abnormal_Positioning_Remark;
	}
	public void setAbnormal_Positioning_Remark(String abnormal_Positioning_Remark) {
		this.abnormal_Positioning_Remark = abnormal_Positioning_Remark;
	}
	public int getIsSynchronized() {
		return isSynchronized;
	}
	public void setIsSynchronized(int isSynchronized) {
		this.isSynchronized = isSynchronized;
	}
	
}
