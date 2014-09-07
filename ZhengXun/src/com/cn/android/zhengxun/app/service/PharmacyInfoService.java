package com.cn.android.zhengxun.app.service;

import java.util.List;

import com.cn.android.zhengxun.app.model.PharmacyInfoModel;

public interface PharmacyInfoService {

	List<PharmacyInfoModel> getPharmacyInfoBySellerCode(String sellerCode);
	
	List<PharmacyInfoModel> getPharmacyInfoBySellerCodeForTour(String sellerCode);

	PharmacyInfoModel getPharmacyInfoById(String bcompanyId);
	
	List<PharmacyInfoModel> getPharmacyInfoByName(String sellerCode,String search_area);
	
	List<PharmacyInfoModel> getPharmacyInfoByNameForTour(String sellerCode,String search_area);
}
