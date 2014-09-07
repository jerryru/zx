package com.cn.android.zhengxun.app.data;


public class UserData {

	private String user_id="";
	private String sellerCode="";
	
	private static final UserData userData=new UserData();
	public synchronized static UserData getInstance(){
//		if(userData==null){
//			userData=new UserData();
//		}
		return userData;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getSellerCode() {
		return sellerCode;
	}
	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}
}
