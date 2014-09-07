package com.cn.android.zhengxun.app.activity;

import java.util.List;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.adapter.PharmacyAdapter;
import com.cn.android.zhengxun.app.constants.StringConstants;
import com.cn.android.zhengxun.app.constants.TypeIds;
import com.cn.android.zhengxun.app.data.UserData;
import com.cn.android.zhengxun.app.model.PharmacyInfoModel;
import com.cn.android.zhengxun.app.model.UserModel;
import com.cn.android.zhengxun.app.service.PharmacyInfoService;
import com.cn.android.zhengxun.app.service.impl.PharmacyInfoServiceImpl;
import com.cn.android.zhengxun.app.util.HandlerCommand;
import com.cn.android.zhengxun.app.util.UserSessionUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DailyWorkSheetActivity extends Activity {

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			this.finish();
			}
	}

	private TextView txt_title,txt_company,tv_no_results;
	private Button btn_back, btn_select;
	private RelativeLayout rl_sigin, rl_sigout;
	private List<PharmacyInfoModel> companies;
	private PharmacyInfoService pharmacyInfoService;
	private PharmacyInfoModel pharmacyInfoModel;
	private PharmacyAdapter pharmacyAdapter;
	private ListView lst;
	private RelativeLayout layout_pop, rl_search_area;
	private LinearLayout ll_search_area;
	private EditText et_search;
	private ImageView img_search;
	private UserModel user = new UserModel();
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case HandlerCommand.GOT_COMPANY:

				ll_search_area.setVisibility(View.GONE);
				pharmacyInfoModel = pharmacyAdapter.getSelectedModel();
				txt_company.setText(pharmacyInfoModel.getBcompnayName());
				break;
			}
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.worksheet_layout);
		initViews();
		initListeners();
		initData();
	}

	private void initData() {
		pharmacyInfoService = new PharmacyInfoServiceImpl(this, handler);

		user.setUserName(UserData.getInstance().getUser_id());
		user.setSellerCode(UserData.getInstance().getSellerCode());
		if (null == user.getSellerCode()||"".equals(user.getSellerCode())){
			user=UserSessionUtil.getUser(this);
		}
		if (null != user.getSellerCode() && !"".equals(user.getSellerCode())) {
			companies = pharmacyInfoService.getPharmacyInfoBySellerCode(user
					.getSellerCode());
		}
//		if (companies.size() > 0) {
//			pharmacyInfoModel = companies.get(0);
//			txt_company.setText(companies.get(0).getBcompnayName());
//		} else {
			txt_company.setText(R.string.none);
			// }
			pharmacyAdapter = new PharmacyAdapter(this, handler);
			pharmacyAdapter.setCompanies(companies);
			lst.setAdapter(pharmacyAdapter);
			lst.invalidateViews();
//		}
	}

	private void initViews() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		btn_back = (Button) findViewById(R.id.back);
		btn_select = (Button) findViewById(R.id.btn_right);
		rl_sigin = (RelativeLayout) findViewById(R.id.rl_sigin_layout);
		rl_sigout = (RelativeLayout) findViewById(R.id.rl_sigout_layout);
		lst = (ListView) findViewById(R.id.lst_company);
		txt_company = (TextView) findViewById(R.id.tv_search_topic);

	layout_pop = (RelativeLayout) findViewById(R.id.layout_pop_source);
		txt_title.setText(R.string.worksheet);
		btn_select.setVisibility(View.VISIBLE);
		
		et_search=(EditText)findViewById(R.id.et_search_area);
		img_search=(ImageView)findViewById(R.id.img_search);
		ll_search_area = (LinearLayout) findViewById(R.id.ll_company_layout);
		rl_search_area=(RelativeLayout)findViewById(R.id.rl_search_area);
		tv_no_results=(TextView)findViewById(R.id.tv_search_no_results);
	}

	private void initListeners() {
		btn_back.setOnClickListener(listener);
		rl_sigin.setOnClickListener(listener);
		rl_sigout.setOnClickListener(listener);
		layout_pop.setOnClickListener(listener);
		btn_select.setOnClickListener(listener);
		
		img_search.setOnClickListener(listener);
		rl_search_area.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				DailyWorkSheetActivity.this.finish();
				break;
			case R.id.rl_sigin_layout:
				if(null!=pharmacyInfoModel){
				Intent intent = new Intent();
				intent.putExtra(StringConstants.TYPE,
						TypeIds.DAILY_WORK_SIGN_IN);
				intent.putExtra(PharmacyInfoModel.BCOMPANYID, pharmacyInfoModel.getBcompanyId());
				intent.putExtra(PharmacyInfoModel.BREG_ADDRESS,pharmacyInfoModel.getBregaddress());
				intent.putExtra(PharmacyInfoModel.CUSTOMER_LAT, pharmacyInfoModel.getCustomerLat());
				intent.putExtra(PharmacyInfoModel.CUSTOMER_LOT, pharmacyInfoModel.getCustomerLot());
				intent.setClass(DailyWorkSheetActivity.this, MapActivity.class);
				DailyWorkSheetActivity.this.startActivityForResult(intent,TypeIds.DAILY_WORK_SIGN_IN);
				}else{
					Toast.makeText(DailyWorkSheetActivity.this, R.string.select_shop, 1000)
					.show();
				}
				break;
			case R.id.rl_sigout_layout:
				if(null!=pharmacyInfoModel){
				Intent intent1 = new Intent();
				intent1.putExtra(StringConstants.TYPE,
						TypeIds.DAILY_WORK_SIGN_OUT);
				intent1.putExtra(PharmacyInfoModel.BCOMPANYID, pharmacyInfoModel.getBcompanyId());
				intent1.putExtra(PharmacyInfoModel.BREG_ADDRESS,pharmacyInfoModel.getBregaddress());
				intent1.putExtra(PharmacyInfoModel.CUSTOMER_LAT, pharmacyInfoModel.getCustomerLat());
				intent1.putExtra(PharmacyInfoModel.CUSTOMER_LOT, pharmacyInfoModel.getCustomerLot());
				intent1.setClass(DailyWorkSheetActivity.this, MapActivity.class);
				DailyWorkSheetActivity.this.startActivityForResult(intent1,TypeIds.DAILY_WORK_SIGN_OUT);
				}else{
					Toast.makeText(DailyWorkSheetActivity.this, R.string.select_shop, 1000)
					.show();
				}
				break;
			case R.id.layout_pop_source:
				ll_search_area.setVisibility(View.VISIBLE);
				tv_no_results.setVisibility(View.GONE);
				break;
			case R.id.btn_right:
				Intent intent=new Intent();
				intent.setClass(DailyWorkSheetActivity.this, DailyWorkListActivity.class);
				DailyWorkSheetActivity.this.startActivity(intent);
				break;
			case R.id.img_search:
				String search_input=et_search.getText().toString();
				String sellerCode=user.getSellerCode();
				if (null == user.getSellerCode() || "".equals(user.getSellerCode())) {
					sellerCode = UserSessionUtil.getUser(DailyWorkSheetActivity.this).getSellerCode();
				}
				companies=pharmacyInfoService.getPharmacyInfoByName(sellerCode,search_input);
				pharmacyAdapter.setCompanies(companies);
				lst.setAdapter(pharmacyAdapter);
				lst.invalidateViews();
				if(null==companies||companies.size()==0){
					tv_no_results.setVisibility(View.VISIBLE);
				}else{
					tv_no_results.setVisibility(View.GONE);
				}
				break;
			case R.id.rl_search_area:
				break;

			}

		}

	};

}
