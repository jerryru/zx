package com.cn.android.zhengxun.app.model;

public class PharmacyInfoModel {

	public static final String SELLER_CODE="SellerCode";
	public static final String BREG_ADDRESS="Bregaddress";
	public static final String CUSTOMER_LAT="customer_lat";
	public static final String CUSTOMER_LOT="customer_lon";
	public static final String BCLASS="Bclass";
	public static final String BTYPE="Btype";
	public static final String CITY_NAME="CityName";
	public static final String PROVINCE_NAME="ProvinceName";
	public static final String BCOMPANY_NAME="BcompanyName";
	public static final String BCOMPANYID="BcompanyID";
	public static final String IS_SYN="isSynchronized";
	public static final String DEPLOYMENT = "Pharmacy_Info";
	public static final String BCOMPANY_NAME_AREA="BcompanyName_area";
	
	private int isSynchronized;
	private String bcompanyId;
	private String bcompnayName;
	private String provinceName;
	private String cityName;
	private String btype;
	private String bclass;	
	private String customerLat;
	private String customerLot;
	private String bregaddress;
	private String sellerCode;
	private String BcompanyName_area;
	
	public int getIsSynchronized() {
		return isSynchronized;
	}
	public void setIsSynchronized(int isSynchronized) {
		this.isSynchronized = isSynchronized;
	}
	public String getBcompanyId() {
		return bcompanyId;
	}
	public void setBcompanyId(String bcompanyId) {
		this.bcompanyId = bcompanyId;
	}
	public String getBcompnayName() {
		return bcompnayName;
	}
	public void setBcompnayName(String bcompnayName) {
		this.bcompnayName = bcompnayName;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getBtype() {
		return btype;
	}
	public void setBtype(String btype) {
		this.btype = btype;
	}
	public String getBclass() {
		return bclass;
	}
	public void setBclass(String bclass) {
		this.bclass = bclass;
	}
	public String getCustomerLat() {
		return customerLat;
	}
	public void setCustomerLat(String customerLat) {
		this.customerLat = customerLat;
	}
	public String getCustomerLot() {
		return customerLot;
	}
	public void setCustomerLot(String customerLot) {
		this.customerLot = customerLot;
	}
	public String getBregaddress() {
		return bregaddress;
	}
	public void setBregaddress(String bregaddress) {
		this.bregaddress = bregaddress;
	}
	public String getSellerCode() {
		return sellerCode;
	}
	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}
	public String getBcompanyName_area() {
		return BcompanyName_area;
	}
	public void setBcompanyName_area(String bcompanyName_area) {
		BcompanyName_area = bcompanyName_area;
	}
	
}
