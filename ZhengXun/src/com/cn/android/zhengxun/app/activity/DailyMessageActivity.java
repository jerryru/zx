package com.cn.android.zhengxun.app.activity;


import java.util.List;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.adapter.SendInfoAdapter;
import com.cn.android.zhengxun.app.data.SendInfoData;
import com.cn.android.zhengxun.app.service.SendInfoService;
import com.cn.android.zhengxun.app.service.impl.SendInfoServiceImpl;
import com.cn.android.zhengxun.app.util.HandlerCommand;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class DailyMessageActivity extends Activity {

	private TextView txt_title;
	private Button btn_select,btn_back;
	private SendInfoService sendInfoService;
	private ListView mLst;
	private SendInfoAdapter adapter;
	private List<SendInfoData> sendInfos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_message_layout);
		initViews();
		initData();
		initListeners();
		synChonizedData();
	}

	private void synChonizedData() {
		Thread thread=new Thread(){
			@Override
			public void run(){
				sendInfos=sendInfoService.getSynChonizedAll();
				sendInfoService.insertToDB(sendInfos);
				Message msg=handler.obtainMessage(HandlerCommand.GOT_SEND_INFO);
				handler.sendMessage(msg);
			}
		};
		thread.start();
		
	}

	private void initData() {
		sendInfoService=new SendInfoServiceImpl(this,handler);
		List<SendInfoData> sendInfoData=sendInfoService.getAll();
		adapter=new SendInfoAdapter(this);
		adapter.setList(sendInfoData);
		mLst.setAdapter(adapter);
	}
	private void initViews() {
		txt_title=(TextView)findViewById(R.id.txt_title);
		mLst=(ListView)findViewById(R.id.lst_system_messages);
		btn_select=(Button)findViewById(R.id.btn_right);
		btn_back=(Button)findViewById(R.id.back);
		txt_title.setText(R.string.announcement);
		
		btn_select.setVisibility(View.VISIBLE);
		
		
	}
	private void initListeners() {
		
		btn_back.setOnClickListener(listener);
		
	}

	private OnClickListener listener=new OnClickListener(){

		@Override
		public void onClick(View v) {
			
			switch(v.getId()){
			case R.id.back:
				DailyMessageActivity.this.finish();
				break;
			}
		}
		
	};
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case HandlerCommand.GOT_SEND_INFO:
				List<SendInfoData> sendInfoList=sendInfoService.getAll();
				
				adapter.setList(sendInfoList);
				mLst.setAdapter(adapter);
				break;
			}
		}
		
	};
}
