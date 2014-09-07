package com.cn.android.zhengxun.app.adapter;

import java.util.List;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.activity.detail.TourDetailActivity;
import com.cn.android.zhengxun.app.model.TourModel;
import com.cn.android.zhengxun.app.service.PharmacyInfoService;
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

public class TourListAdapter extends BaseAdapter {

	private List<TourModel> tours;
	private Context context;
	private LayoutInflater inflater;
	private PharmacyInfoService pharmacyInfoService;
	public TourListAdapter(Context context){
		this.context=context;
		this.inflater=LayoutInflater.from(context);
		pharmacyInfoService=new PharmacyInfoServiceImpl(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tours.size();
	}

	@Override
	public Object getItem(int location) {
		// TODO Auto-generated method stub
		return tours.get(location);
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
		final TourModel tour=tours.get(location);
		listItemView.tv_company.setText(pharmacyInfoService.getPharmacyInfoById(tour.getBcompanyId()).getBcompnayName());
		 listItemView.tv_time.setText(DateUtil.formatDate(tour.getTourInTime(),"yyyy-MM-dd HH:mm"));
		 switch(tour.getIsNormal()){
		 case 0:
			 listItemView.tv_type.setText(R.string.abnormal_positioning);
			 break;
		 case 1:
			 listItemView.tv_type.setText(R.string.normal_positioning);
			 break;
		 }
		 long start=tour.getTourInTime().getTime();
		 long end=tour.getTour_out_Time().getTime();
		 int time=(int) ((end-start)/60000);
		 listItemView.tv_time_period.setText(time+"分钟");
		 convertView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(context, TourDetailActivity.class);
				intent.putExtra(TourModel.Tour_Id,tour.getTourId());
				context.startActivity(intent);
			}
			 
		 });
		return convertView;
	}

	public List<TourModel> getTours() {
		return tours;
	}

	public void setTours(List<TourModel> tours) {
		this.tours = tours;
	}

	private final class ListItemView {

		private TextView tv_company, tv_type, tv_time,tv_time_period;
	}

}
