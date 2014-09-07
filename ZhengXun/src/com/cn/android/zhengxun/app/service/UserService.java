package com.cn.android.zhengxun.app.service;

import com.cn.android.zhengxun.app.model.UserModel;

public interface UserService {

	int login(UserModel user);
	boolean insertUser(UserModel user);
	
}
