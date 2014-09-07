package com.cn.android.zhengxun.app.activity;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.data.SendInfoData;
import com.cn.android.zhengxun.app.service.SendInfoService;
import com.cn.android.zhengxun.app.service.impl.SendInfoServiceImpl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DailyMessageDetailActivity extends Activity {

	private Button btn_back;
	private TextView txt_content,title;
	private SendInfoService sendInfoService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_message_detail_layout);
		initViews();
		initData();
		initListeners();
	}

	private void initViews() {
		btn_back=(Button)findViewById(R.id.back);
		title=(TextView)findViewById(R.id.txt_title);
		txt_content=(TextView)findViewById(R.id.txt_system_msg_content);
	}
	private void initData() {

		Intent intent=this.getIntent();
		String id=intent.getStringExtra("sendID");
		sendInfoService=new SendInfoServiceImpl(this,handler);
		SendInfoData sendInfo=sendInfoService.getDetail(id);
		title.setText(sendInfo.getTitle());
		if(null!=sendInfo.getContent()){
		txt_content.setText(Html.fromHtml(sendInfo.getContent()));
		}
	}

	private void initListeners() {
		
		btn_back.setOnClickListener(listener);
		
	}
	private OnClickListener listener=new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.back:
				DailyMessageDetailActivity.this.finish();
				break;
			}
		}
		
	}; 
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
		
		}
		
	};
}
