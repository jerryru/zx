package com.cn.android.zhengxun.app.model;

import java.util.List;

public class PrincipleModel {

	private UserModel user;
	private List<ClerkModel> clerk;
	private List<PharmacyInfoModel> bcompany;
	public List<ClerkModel> getClerk() {
		return clerk;
	}
	public void setClerk(List<ClerkModel> clerk) {
		this.clerk = clerk;
	}
	public List<PharmacyInfoModel> getBcompany() {
		return bcompany;
	}
	public void setBcompany(List<PharmacyInfoModel> bcompany) {
		this.bcompany = bcompany;
	}
	public UserModel getUser() {
		return user;
	}
	public void setUser(UserModel user) {
		this.user = user;
	}

}
