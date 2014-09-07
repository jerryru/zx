package com.cn.android.zhengxun.app.adapter;

import java.util.List;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.constants.TypeIds;
import com.cn.android.zhengxun.app.model.AttendenceInfoModel;
import com.cn.android.zhengxun.app.service.PharmacyInfoService;
import com.cn.android.zhengxun.app.service.impl.PharmacyInfoServiceImpl;
import com.cn.android.zhengxun.app.util.DateUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DailyWorkListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<AttendenceInfoModel> dailyDatas;
	private PharmacyInfoService pharmacyInfoService;

	public DailyWorkListAdapter(Context context) {
		this.context = context;
		pharmacyInfoService=new PharmacyInfoServiceImpl(context);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dailyDatas.size();
	}

	@Override
	public Object getItem(int location) {
		// TODO Auto-generated method stub
		return dailyDatas.get(location);
	}

	@Override
	public long getItemId(int location) {
		// TODO Auto-generated method stub
		return location;
	}

	@Override
	public View getView(int location, View convertView, ViewGroup vg) {
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = inflater
					.inflate(R.layout.daily_work_item_layout, null);
			 listItemView.tv_company=(TextView) convertView.findViewById(R.id.tv_date);
			 listItemView.tv_time=(TextView)convertView.findViewById(R.id.tv_time);
			 listItemView.tv_type=(TextView)convertView.findViewById(R.id.tv_is_syn);
			 listItemView.tv_time_period=(TextView)convertView.findViewById(R.id.tv_time_period);
			 
		}else{
			 listItemView = (ListItemView) convertView.getTag();
		}
		listItemView.tv_time_period.setVisibility(View.GONE);
		AttendenceInfoModel model=dailyDatas.get(location);
		listItemView.tv_company.setText(pharmacyInfoService.getPharmacyInfoById(model.getBcompanyId()).getBcompnayName());
		listItemView.tv_time.setText(DateUtil.formatDate(model.getAttendenceTime(),"yyyy-MM-dd HH:mm"));
		switch(model.getAttendanceToken()){
		case TypeIds.DAILY_WORK_SIGN_IN:
			listItemView.tv_type.setText(R.string.signin);
			break;
		case TypeIds.DAILY_WORK_SIGN_OUT:
			listItemView.tv_type.setText(R.string.signout);
			break;
		}
			return convertView;
		
	}

	public List<AttendenceInfoModel> getDailyDatas() {
		return dailyDatas;
	}

	public void setDailyDatas(List<AttendenceInfoModel> dailyDatas) {
		this.dailyDatas = dailyDatas;
	}

	private final class ListItemView {

		private TextView tv_company, tv_type, tv_time,tv_time_period;
	}

}
