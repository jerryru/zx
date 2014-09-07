package com.cn.android.zhengxun.app.service;

import java.util.Date;
import java.util.List;

import com.cn.android.zhengxun.app.model.AttendenceInfoModel;

public interface DailyWorkService {

	boolean sigin(AttendenceInfoModel data);
	boolean sigout(AttendenceInfoModel data);
	boolean addAttendenceToServer(AttendenceInfoModel data);
	void update(AttendenceInfoModel dailyWorkData);
	void updateLocal(AttendenceInfoModel dailyWorkData);
	List<AttendenceInfoModel> getUnsynAttendenceInfos();
	List<AttendenceInfoModel> getUnsynLocalAttendenceInfos();
	void updateSynchronize(AttendenceInfoModel dailyWorkData);
	List<AttendenceInfoModel> getAttendenceInfo(Date start,Date end);
	boolean isExist(AttendenceInfoModel dailyWorkData);
}
