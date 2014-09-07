package com.cn.android.zhengxun.app.service.impl;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.model.AttendenceInfoModel;
import com.cn.android.zhengxun.app.model.HomeVisitModel;
import com.cn.android.zhengxun.app.model.TourModel;
import com.cn.android.zhengxun.app.service.DailyWorkService;
import com.cn.android.zhengxun.app.service.FamilyVisitService;
import com.cn.android.zhengxun.app.service.SynchronizationService;
import com.cn.android.zhengxun.app.service.TourInfoService;
import com.cn.android.zhengxun.app.util.AppHelper;

public class SynchronizationServiceImpl implements SynchronizationService {

	private DailyWorkService daillyWorkService;
	private TourInfoService tourService;
	private FamilyVisitService visitService;
	private Context context;

	public SynchronizationServiceImpl(Context context) {
		this.context = context;
	}

	@Override
	public boolean synchronizeData() {
		if (AppHelper.isNetworkConected(context)) {
			final List<AttendenceInfoModel> dailyWorkDatas;
			daillyWorkService = new DailyWorkServiceImpl(context);
			dailyWorkDatas = daillyWorkService.getUnsynAttendenceInfos();

			tourService = new TourInfoServiceImpl(context);
			final List<TourModel> tours = tourService.getAsynTourInfos();

			visitService = new FamilyVisitServiceImpl(context);
			final List<HomeVisitModel> visits = visitService.getUnsynVisits();

			if ((dailyWorkDatas == null || dailyWorkDatas.size() == 0)
					&& (tours.size() == 0 || tours == null)
					&& (visits == null || visits.size() == 0)) {
				Toast.makeText(context,
						context.getString(R.string.not_need_to_submit), 1000)
						.show();
			} else {
//				AppHelper.showExitDialog(context,
//						context.getString(R.string.confirm_submit),
//						new android.content.DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								proDialog = new ProgressDialog(context);								
//								proDialog.setMessage(context
//										.getString(R.string.begin_submit));
//								if (null != proDialog) {
//									proDialog.show();
//								}
								Toast.makeText(
										context,
										context.getString(R.string.begin_submit),
										1000).show();
								
								if (null != dailyWorkDatas
										&& dailyWorkDatas.size() != 0) {
									for (AttendenceInfoModel attendectInfo : dailyWorkDatas) {
										boolean isSucessful = daillyWorkService
												.addAttendenceToServer(attendectInfo);
										if (isSucessful) {
											daillyWorkService
													.updateSynchronize(attendectInfo);
										} else {

										}
									}
								}

								if (null != tours && tours.size() > 0) {
									for (TourModel tour : tours) {
										boolean isSucessful = tourService
												.synChronizeWithServer(tour);
										if (isSucessful) {
											tourService.updateSynchronize(tour);
										} else {

										}
									}
								}

								if (null != visits && visits.size() != 0) {
									for (HomeVisitModel visit : visits) {
										boolean isSucessful = visitService
												.synchronziedWithServer(visit);
										if (isSucessful) {
											visitService.updateSyn(visit);
										} else {

										}
									}

								}
							
								Toast.makeText(
										context,
										context.getString(R.string.finish_submit),
										1000).show();
							
//							}
//
//						});

			}
			return true;
		} else {
			AppHelper.showExitDialog(context,
					context.getString(R.string.no_network),
					new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent();
							intent.setAction(Settings.ACTION_SETTINGS);
							context.startActivity(intent);
						}
					});
			return false;
		}
	}

}
