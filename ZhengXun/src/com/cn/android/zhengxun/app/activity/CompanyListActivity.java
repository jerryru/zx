package com.cn.android.zhengxun.app.activity;

import java.util.List;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.model.PharmacyInfoModel;
import com.cn.android.zhengxun.app.model.UserModel;
import com.cn.android.zhengxun.app.service.PharmacyInfoService;
import com.cn.android.zhengxun.app.service.impl.PharmacyInfoServiceImpl;
import com.cn.android.zhengxun.app.util.HandlerCommand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

public class CompanyListActivity extends Activity {

	private ListView lst;
	private List<PharmacyInfoModel> companies;
	private String sellerCode;
	private PharmacyInfoService pharmacyInfoService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_list_layout);
		initViews();
		initListeners();
		initData();
	}
	
	private void initData() {
		Intent intent=this.getIntent();
		sellerCode=intent.getStringExtra(UserModel.SELLER_CODE);
		pharmacyInfoService=new PharmacyInfoServiceImpl(this,handler);
		companies=pharmacyInfoService.getPharmacyInfoBySellerCode(sellerCode);
	}

	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case HandlerCommand.GOT_PHARAMCY_LIST:
				
				break;
			}
			
		}
		
	};
	private void initListeners() {
		
	}

	private void initViews() {
		lst=(ListView)findViewById(R.id.lst_company);
		
	}

}
