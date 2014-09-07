package com.cn.android.zhengxun.app.service;

import java.util.Date;
import java.util.List;

import com.cn.android.zhengxun.app.model.HomeVisitModel;

public interface FamilyVisitService {

	boolean synchronziedWithServer(HomeVisitModel model);
	boolean insertDB(HomeVisitModel model);
	boolean updateInDB(HomeVisitModel model);
	List<HomeVisitModel> getVisits(Date start,Date end);
	HomeVisitModel getVisitById(String visitId);
	List<HomeVisitModel> getUnsynVisits();
	List<HomeVisitModel> getUnsynLocalVisits();
	boolean updateSyn(HomeVisitModel model);
}
