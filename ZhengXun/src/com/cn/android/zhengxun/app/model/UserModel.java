package com.cn.android.zhengxun.app.model;

import java.io.Serializable;

public class UserModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7025181795080580929L;
	public static final String SELLER_CODE="SellerCode";
	public static final String USER_PSW="user_password";
	public static final String USER_ID="user_id";
	public static final String TOKEN="user_token";
	public static final String USER_NAME="user_name";
	public static final String ROLE="Role";
	public static final String AREA_NAME="Area_Name";
	public static final String DEPT_NAME="Dept_Name";
	public static final String MANGER_ID="Manager_Id";
	public static final String MANGER_NAME="Manager_Name";
	public static final String MANGER_ROLE="Manager_Role";
	public static final String CONTACT_PHONE="contact_phone";
	public static final String IS_SYN="isSynchronized";
	private String userName;
	private String password;
	private String usetToken;
	private String name;
	private String sellerCode;
	private String role;
	private String area_name;
	private String dept_name;
	private String managerId;
	private String manager_name;
	private String manager_role;
	private String t_phone;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsetToken() {
		return usetToken;
	}
	public void setUsetToken(String usetToken) {
		this.usetToken = usetToken;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSellerCode() {
		return sellerCode;
	}
	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	public String getManager_name() {
		return manager_name;
	}
	public void setManager_name(String manager_name) {
		this.manager_name = manager_name;
	}
	public String getManager_role() {
		return manager_role;
	}
	public void setManager_role(String manager_role) {
		this.manager_role = manager_role;
	}
	public String getT_phone() {
		return t_phone;
	}
	public void setT_phone(String t_phone) {
		this.t_phone = t_phone;
	}

}
