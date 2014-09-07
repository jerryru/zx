package com.cn.android.zhengxun.app.adapter;

import java.util.List;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.activity.detail.FamilyDetailActivity;
import com.cn.android.zhengxun.app.model.HomeVisitModel;
import com.cn.android.zhengxun.app.service.ClerkInfoService;
import com.cn.android.zhengxun.app.service.PharmacyInfoService;
import com.cn.android.zhengxun.app.service.impl.ClerkServiceImpl;
import com.cn.android.zhengxun.app.service.impl.PharmacyInfoServiceImpl;
import com.cn.android.zhengxun.app.util.DateUtil;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HomeVisitListAdapter extends BaseAdapter {

	private List<HomeVisitModel> visits;
	private Context context;
	private LayoutInflater inflater;
	private PharmacyInfoService pharmacyInfoService;
	private ClerkInfoService clerkService;
	public HomeVisitListAdapter(Context context){
		this.context=context;
		this.inflater=LayoutInflater.from(context);
		pharmacyInfoService=new PharmacyInfoServiceImpl(context);
		clerkService=new ClerkServiceImpl(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return visits.size();
	}

	@Override
	public Object getItem(int location) {
		// TODO Auto-generated method stub
		return visits.get(location);
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
		final HomeVisitModel visit=visits.get(location);
		 listItemView.tv_company.setText(pharmacyInfoService.getPharmacyInfoById(visit.getBcompany_Id()).getBcompnayName());
		 listItemView.tv_time.setText(DateUtil.formatDate(visit.getVisit_in_Time(),"yyyy-MM-dd HH:mm"));
		 long start=visit.getVisit_in_Time().getTime();
		 long end=visit.getVisit_out_Time().getTime();
		 int time=(int) ((end-start)/60000);
		 listItemView.tv_time_period.setText(time+"分钟");
		 listItemView.tv_type.setText(clerkService.getClerk(visit.getClerk_Id()).getName());
		 convertView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(context, FamilyDetailActivity.class);
				intent.putExtra(HomeVisitModel.Family_Visit_Id, visit.getFamily_Visit_Id());
				context.startActivity(intent);
			}
			 
		 });
		return convertView;
	}

	public List<HomeVisitModel> getVisits() {
		return visits;
	}

	public void setVisits(List<HomeVisitModel> visits) {
		this.visits = visits;
	}

	private final class ListItemView {

		private TextView tv_company, tv_type, tv_time,tv_time_period;
	}
}
