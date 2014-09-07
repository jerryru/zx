package com.cn.android.zhengxun.app.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.adapter.TourListAdapter;
import com.cn.android.zhengxun.app.model.TourModel;
import com.cn.android.zhengxun.app.service.TourInfoService;
import com.cn.android.zhengxun.app.service.impl.TourInfoServiceImpl;
import com.cn.android.zhengxun.app.util.DateUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TourListActivity extends Activity {

	private TextView txt_title, tv_from, tv_to;
	private Button btn_back, btn_select;
	private TourInfoService tourService;
	private ListView mLst;
	private TourListAdapter adapter;
	private List<TourModel> list;
	private Calendar cal = Calendar.getInstance();
	private LinearLayout ll_date_picker;

	private Date start = Calendar.getInstance().getTime();
	private Date end = Calendar.getInstance().getTime();
	private OnClickListener clicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				TourListActivity.this.finish();
				break;
			case R.id.tv_from_date:
				new DatePickerDialog(TourListActivity.this,

				new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker dp, int year, int month,
							int day) {
						cal.set(Calendar.YEAR, year);

						cal.set(Calendar.MONTH, month);

						cal.set(Calendar.DAY_OF_MONTH, day);

						start = cal.getTime();
						if (start.getTime() < Calendar.getInstance()
								.getTimeInMillis()) {
							tv_from.setText(DateUtil.formatDate(start,
									"yyyy-MM-dd"));
						} else {

						}
					}

				},

				cal.get(Calendar.YEAR),

				cal.get(Calendar.MONTH),

				cal.get(Calendar.DAY_OF_MONTH)

				).show();

				break;
			case R.id.tv_to_date:
				new DatePickerDialog(TourListActivity.this,

				new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker dp, int year, int month,
							int day) {
						cal.set(Calendar.YEAR, year);

						cal.set(Calendar.MONTH, month);

						cal.set(Calendar.DAY_OF_MONTH, day);

						end = cal.getTime();
						if (end.getTime() > start.getTime()) {

							tv_to.setText(DateUtil
									.formatDate(end, "yyyy-MM-dd"));
						} else {

						}
					}

				},

				cal.get(Calendar.YEAR),

				cal.get(Calendar.MONTH),

				cal.get(Calendar.DAY_OF_MONTH)

				).show();

				break;
			case R.id.btn_select_data:
				selecttData();
				break;
			}

		}

		
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_message_layout);
		initViews();
		initData();
		initListeners();
	}

	private void initData() {
		adapter = new TourListAdapter(this);
		tourService = new TourInfoServiceImpl(this);
		start=cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		end=cal.getTime();
		tv_from.setText(DateUtil.formatDate(start, "yyyy-MM-dd"));
		tv_to.setText(DateUtil.formatDate(end, "yyyy-MM-dd"));
	}

	private void initListeners() {
		btn_back.setOnClickListener(clicklistener);
		btn_select.setOnClickListener(clicklistener);
		tv_from.setOnClickListener(clicklistener);
		tv_to.setOnClickListener(clicklistener);

	}

	private void selecttData() {
		list = tourService.getTourInfos(start, end);
		adapter.setTours(list);
		mLst.setAdapter(adapter);
		mLst.invalidateViews();
	}

	private void initViews() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		tv_from = (TextView) findViewById(R.id.tv_from_date);
		tv_to = (TextView) findViewById(R.id.tv_to_date);
		mLst = (ListView) findViewById(R.id.lst_system_messages);
		btn_back = (Button) findViewById(R.id.back);
		btn_select = (Button) findViewById(R.id.btn_select_data);
		txt_title.setText(R.string.tour_select);
		
		ll_date_picker = (LinearLayout) findViewById(R.id.date_picker_layout);
		ll_date_picker.setVisibility(View.VISIBLE);
	}

}
