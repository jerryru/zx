package com.cn.android.zhengxun.app.data;

import java.util.Date;

public class AttendenceData {

	private String attendceId;
	private String sellerCode;
	private int attendenceType;
	private String bcompanyId;
	private double lat;
	private double lot;
	private Date attendenceTime;
	private String attendence_address;
	private String abnormal_Positioning_Remark;
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
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLot() {
		return lot;
	}
	public void setLot(double lot) {
		this.lot = lot;
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
	
}
