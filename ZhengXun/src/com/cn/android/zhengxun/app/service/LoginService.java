package com.cn.android.zhengxun.app.service;

import com.cn.android.zhengxun.app.model.UserModel;

public interface LoginService {

	UserModel login(UserModel user);
	UserModel getReturnUser();
	
	void setReturnUser(UserModel returnUser);
}
