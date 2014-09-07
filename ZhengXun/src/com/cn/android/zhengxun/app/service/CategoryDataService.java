package com.cn.android.zhengxun.app.service;

import java.util.List;

import com.cn.android.zhengxun.app.data.CategoryData;

public interface CategoryDataService {

	List<CategoryData> getData(String path);
	
}
