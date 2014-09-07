package com.cn.android.zhengxun.app.service;

import java.util.List;

import com.cn.android.zhengxun.app.data.SendInfoData;

public interface SendInfoService {

	List<SendInfoData> getAll();
	SendInfoData getDetail(String sendId);
	List<SendInfoData> getSynChonizedAll();
	boolean insertToDB(List<SendInfoData> sendInfos);
	
}
