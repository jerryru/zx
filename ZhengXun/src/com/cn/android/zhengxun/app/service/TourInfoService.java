package com.cn.android.zhengxun.app.service;

import java.util.Date;
import java.util.List;

import com.cn.android.zhengxun.app.model.TourModel;

public interface TourInfoService {

	boolean synChronizeWithServer(TourModel tourModel);
	boolean updateInDB(TourModel model);
	boolean insertDB(TourModel model);
	List<TourModel> getTourInfos(Date start,Date end);
	TourModel getTourById(String tourId);
	List<TourModel> getAsynTourInfos();
	List<TourModel> getAsynLocalTourInfos();
	boolean updateSynchronize(TourModel tourModel);
}
