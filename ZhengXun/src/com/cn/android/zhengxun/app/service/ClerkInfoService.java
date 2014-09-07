package com.cn.android.zhengxun.app.service;

import java.util.List;

import com.cn.android.zhengxun.app.model.ClerkModel;

public interface ClerkInfoService {

	List<ClerkModel> getClerks(String bCompanyId);

	ClerkModel getClerk(String clerk_Id);
}
