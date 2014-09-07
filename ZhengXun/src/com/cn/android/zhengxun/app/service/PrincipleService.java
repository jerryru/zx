package com.cn.android.zhengxun.app.service;


import com.cn.android.zhengxun.app.model.PrincipleModel;
import com.cn.android.zhengxun.app.model.UserModel;

public interface PrincipleService {

	PrincipleModel getPrincipleByUser(UserModel user);
	boolean insertToDB(PrincipleModel model);
	PrincipleModel getPrinciple();
}
