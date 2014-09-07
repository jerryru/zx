package com.cn.android.zhengxun.app.activity;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.cn.android.zhengxun.app.MyApplication;
import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.adapter.ClerkListAdapter;
import com.cn.android.zhengxun.app.adapter.PharmacyAdapter;
import com.cn.android.zhengxun.app.constants.StringConstants;
import com.cn.android.zhengxun.app.constants.TypeIds;
import com.cn.android.zhengxun.app.data.UserData;
import com.cn.android.zhengxun.app.map.LocateHelper;
import com.cn.android.zhengxun.app.model.ClerkModel;
import com.cn.android.zhengxun.app.model.HomeVisitModel;
import com.cn.android.zhengxun.app.model.PharmacyInfoModel;
import com.cn.android.zhengxun.app.model.UserModel;
import com.cn.android.zhengxun.app.service.ClerkInfoService;
import com.cn.android.zhengxun.app.service.FamilyVisitService;
import com.cn.android.zhengxun.app.service.PharmacyInfoService;
import com.cn.android.zhengxun.app.service.impl.ClerkServiceImpl;
import com.cn.android.zhengxun.app.service.impl.FamilyVisitServiceImpl;
import com.cn.android.zhengxun.app.service.impl.PharmacyInfoServiceImpl;
import com.cn.android.zhengxun.app.util.AppHelper;
import com.cn.android.zhengxun.app.util.HandlerCommand;
import com.cn.android.zhengxun.app.util.ImageUtil;
import com.cn.android.zhengxun.app.util.UserSessionUtil;
import com.cn.android.zhengxun.app.widgets.IdGenerator;
//import com.cn.android.zhengxun.util.StringUtil;
//import com.cn.android.zhengxun.widgets.IdGenerator;
//import com.cn.android.zhengxun.widgets.SoundMeter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
//import android.os.Environment;
import android.os.Environment;
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

public class HomeVisitActivity extends Activity {

	private TextView txt_title, tv_company, tv_clerk, txt_address, txt_door,
			txt_display, txt_summary;
	private Button btn_back, btn_select, btn_submit;
	private ListView lst;
	private RelativeLayout layout_company, layout_clerk, rl_door, rl_gift,
			rl_summary, rl_search_area;
	private LinearLayout ll_search_area;
	private EditText et_search;
	private ImageView img_search,img_refresh;
	
	private ClerkListAdapter clerksAdapter;
	private List<ClerkModel> clerks;
	private ClerkInfoService clerkService;
	private ClerkModel clerk;
	private PharmacyAdapter pharmacyAdapter;
	private List<PharmacyInfoModel> companies;
	private PharmacyInfoService pharmacyInfoService;
	private PharmacyInfoModel pharmacyInfoModel;
	private UserModel user;
	private FamilyVisitService familyService;
	private LocateHelper local = null;
	private BDLocation location;
	private BMapManager mBMapMan = null;
	private Bitmap door_bitmap, gift_bitmap;
	private String summary_remark, summary_audio;
	private HomeVisitModel model;
	private Date visit_in_Time;
	private int audio_length;
	private TextView tv_no_results;

	// private SoundMeter mSensor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mBMapMan = ((MyApplication)this.getApplication()).mBMapMan;
		mBMapMan.start();
		setContentView(R.layout.visit_layout);
		initViews();
		getLocations();
		initListeners();
		initData();
	}

	private void initData() {
		clerkService = new ClerkServiceImpl(this);
		pharmacyInfoService = new PharmacyInfoServiceImpl(this, handler);
		tv_company.setText(R.string.none);
		tv_clerk.setText(R.string.none);
		user = new UserModel();
		user.setUserName(UserData.getInstance().getUser_id());
		user.setSellerCode(UserData.getInstance().getSellerCode());
	}

	private void getLocations() {
		local = new LocateHelper(mBMapMan,this, handler);
		local.onCreate();
		

	}

	private void initListeners() {
		rl_summary.setOnClickListener(listener);
		layout_company.setOnClickListener(listener);
		layout_clerk.setOnClickListener(listener);
		rl_door.setOnClickListener(listener);
		rl_gift.setOnClickListener(listener);
		btn_back.setOnClickListener(listener);
		btn_select.setOnClickListener(listener);
		btn_submit.setOnClickListener(listener);
		img_search.setOnClickListener(listener);
		rl_search_area.setOnClickListener(listener);
		img_refresh.setOnClickListener(listener);
	}

	private void initViews() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		rl_summary = (RelativeLayout) findViewById(R.id.rl_visit_summary_layout);
		tv_company = (TextView) findViewById(R.id.tv_search_topic);
		tv_clerk = (TextView) findViewById(R.id.tv_visit_selected_clerk);
		rl_door = (RelativeLayout) findViewById(R.id.rl_visit_door_photo_layout);
		rl_gift = (RelativeLayout) findViewById(R.id.rl_visit_gift_photo_layout);
		lst = (ListView) findViewById(R.id.lst_company_visit);
		txt_address = (TextView) findViewById(R.id.tv_current_lcaotion);
		layout_company = (RelativeLayout) findViewById(R.id.layout_pop_source);
		layout_clerk = (RelativeLayout) findViewById(R.id.layout_visit_pop_resource);
		btn_back = (Button) findViewById(R.id.back);
		btn_select = (Button) findViewById(R.id.btn_right);

		txt_door = (TextView) findViewById(R.id.txt_visit_gate_photo);
		txt_display = (TextView) findViewById(R.id.txt_visit_gift_photo);
		txt_summary = (TextView) findViewById(R.id.txt_visit_summary);

		// mSensor=new SoundMeter(this);
		txt_title.setText(R.string.visit);
		clerksAdapter = new ClerkListAdapter(this, handler);
		pharmacyAdapter = new PharmacyAdapter(this, handler);
		btn_select.setVisibility(View.VISIBLE);
		familyService = new FamilyVisitServiceImpl(this, handler);
		btn_submit = (Button) findViewById(R.id.btn_submit_visit_info);
		
		et_search=(EditText)findViewById(R.id.et_search_area);
		img_search=(ImageView)findViewById(R.id.img_search);
		img_refresh=(ImageView)findViewById(R.id.img_refresh);
		
		ll_search_area = (LinearLayout) findViewById(R.id.ll_company_layout);
		rl_search_area=(RelativeLayout)findViewById(R.id.rl_search_area);
		tv_no_results=(TextView)findViewById(R.id.tv_search_no_results);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rl_visit_summary_layout:
				// mSensor.start(StringUtil.getStringByDateTime()+".amr");
				if (null == gift_bitmap) {
					Toast.makeText(HomeVisitActivity.this,
							R.string.without_gift_photo, 1000).show();
				} else {
					Intent intent_s = new Intent();
					intent_s.setClass(HomeVisitActivity.this,
							SummaryActivity.class);
					intent_s.putExtra(StringConstants.TYPE,
							TypeIds.VISIT_SUMMARY);
					HomeVisitActivity.this.startActivityForResult(intent_s,
							TypeIds.VISIT_SUMMARY);

				}
				break;
			case R.id.rl_visit_door_photo_layout:
				visit_in_Time = Calendar.getInstance().getTime();
				if (null != clerk) {
					Intent intent = new Intent();
					intent.setClass(HomeVisitActivity.this, PhotoActivity.class);
					intent.putExtra(StringConstants.TYPE, TypeIds.VISIT_DOOR);
					HomeVisitActivity.this.startActivityForResult(intent,
							TypeIds.VISIT_DOOR);

				} else {
					Toast.makeText(HomeVisitActivity.this,
							R.string.with_no_clerk, 1000).show();
				}
				break;
			case R.id.rl_visit_gift_photo_layout:
				if (null == door_bitmap) {
					Toast.makeText(HomeVisitActivity.this,
							R.string.door_photo_hint, 1000).show();
				} else {
					Intent intent_d = new Intent();
					intent_d.setClass(HomeVisitActivity.this,
							PhotoActivity.class);
					intent_d.putExtra(StringConstants.TYPE, TypeIds.VISIT_GIFT);
					HomeVisitActivity.this.startActivityForResult(intent_d,
							TypeIds.VISIT_GIFT);
				}
				break;
			case R.id.layout_pop_source:
				if (null == user.getSellerCode()
						|| "".equals(user.getSellerCode())) {
					user = UserSessionUtil.getUser(HomeVisitActivity.this);
				}
				if (null != user.getSellerCode()
						&& !"".equals(user.getSellerCode())) {
					companies = pharmacyInfoService
							.getPharmacyInfoBySellerCode(user.getSellerCode());
				}
				ll_search_area.setVisibility(View.VISIBLE);
				rl_search_area.setVisibility(View.VISIBLE);
				tv_no_results.setVisibility(View.GONE);
				pharmacyAdapter.setCompanies(companies);
				lst.setAdapter(pharmacyAdapter);
				lst.invalidateViews();
				break;
			case R.id.layout_visit_pop_resource:
				if (null != pharmacyInfoModel) {
					clerks = clerkService.getClerks(pharmacyInfoModel
							.getBcompanyId());
					rl_search_area.setVisibility(View.GONE);
					ll_search_area.setVisibility(View.VISIBLE);
					tv_no_results.setVisibility(View.GONE);
					clerksAdapter.setClerks(clerks);
					lst.setAdapter(clerksAdapter);
					lst.invalidateViews();
				} else {
					Toast.makeText(HomeVisitActivity.this,
							R.string.with_no_company, 1000).show();
				}
				break;
			case R.id.back:
				HomeVisitActivity.this.finish();
				break;
			// 查询
			case R.id.btn_right:
				Intent intent = new Intent();
				intent.setClass(HomeVisitActivity.this,
						HomeVisitListActivity.class);
				HomeVisitActivity.this.startActivity(intent);
				break;
			case R.id.btn_submit_visit_info:
				if (null == door_bitmap || null == gift_bitmap
						|| null == summary_remark || null == pharmacyInfoModel) {
					AppHelper.showExitDialog(HomeVisitActivity.this,
							getString(R.string.process_unfinished),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {

								}

							});
				} else {
					submit();
				}
				break;
			case R.id.img_search:
				String search_input=et_search.getText().toString();
				String sellerCode=user.getSellerCode();
				if (null == user.getSellerCode() || "".equals(user.getSellerCode())) {
					sellerCode = UserSessionUtil.getUser(HomeVisitActivity.this).getSellerCode();
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
			case R.id.img_refresh:
				if(null!=local){
					local.start();
					local.request();
				}
			}

		}

	};

	private Handler handler = new Handler() {

		private MKPoiInfo mInfo;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HandlerCommand.GOT_COMPANY:
				pharmacyInfoModel = pharmacyAdapter.getSelectedModel();
				ll_search_area.setVisibility(View.GONE);
				tv_company.setText(pharmacyInfoModel.getBcompnayName());
				clerk = null;
				tv_clerk.setText("");
				break;
			case HandlerCommand.GOT_CLERK:
				clerk = clerksAdapter.getSelectedModel();
				ll_search_area.setVisibility(View.GONE);
				tv_clerk.setText(clerk.getName());
				txt_door.setTextColor(HomeVisitActivity.this.getResources()
						.getColor(R.color.orange));
				txt_display.setTextColor(HomeVisitActivity.this.getResources()
						.getColor(R.color.black));
				txt_summary.setTextColor(HomeVisitActivity.this.getResources()
						.getColor(R.color.black));
				break;
			case HandlerCommand.GOT_LOCATION:
				location = local.getMlocation();
				location = local.getMlocation();
				if (null != location) {
					if (location.getLatitude() > 5
							&& location.getLongitude() > 10) {
						if (!"".equals(location.getAddrStr())&&null!=location.getAddrStr()) {
							txt_address.setText(location.getAddrStr());
						} else {
							txt_address
									.setText(getString(R.string.get_loc_without_address));
						}
					} else {

						txt_address
								.setText(getString(R.string.without_gps_info));}

					}else {

						txt_address
								.setText(getString(R.string.without_gps_info));

				}

				break;
			case HandlerCommand.GOT_POI:
				mInfo=local.getPoiInfo();
				if(null!=location&&null!=mInfo){
					GeoPoint start=new GeoPoint((int)(location.getLatitude()*1e6),(int)(location.getLongitude()*1e6));
					GeoPoint end=mInfo.pt;
					int distance=(int)DistanceUtil.getDistance(start, end);
					if(null!=location.getAddrStr()){
					txt_address.setText(location.getAddrStr()+mInfo.name+local.getDirection()+distance+"米附近");
				}else{
					txt_address.setText(mInfo.name+local.getDirection()+distance+"米附近");
				}
				}
				break;
			}
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Toast.makeText(HomeVisitActivity.this, R.string.saved, 1000).show();
			switch (requestCode) {
			case TypeIds.VISIT_DOOR:
				Bundle bundle = data.getExtras();
				door_bitmap = bundle
						.getParcelable(StringConstants.VISIT_DOOR_PHOTO_OK);
				txt_door.setTextColor(HomeVisitActivity.this.getResources()
						.getColor(R.color.green));
				txt_display.setTextColor(HomeVisitActivity.this.getResources()
						.getColor(R.color.orange));
				break;
			case TypeIds.VISIT_GIFT:
				Bundle bundle_d = data.getExtras();
				gift_bitmap = bundle_d
						.getParcelable(StringConstants.VISIT_GIFT_PHOTO_OK);
				txt_display.setTextColor(HomeVisitActivity.this.getResources()
						.getColor(R.color.green));
				txt_summary.setTextColor(HomeVisitActivity.this.getResources()
						.getColor(R.color.orange));
				break;
			case TypeIds.VISIT_SUMMARY:
				txt_summary.setTextColor(HomeVisitActivity.this.getResources()
						.getColor(R.color.green));
				Bundle bundle_s = data.getExtras();
				summary_remark = bundle_s
						.getString(StringConstants.SUMMARY_TEXT);
				summary_audio = bundle_s
						.getString(StringConstants.SUMMARY_AUDIO);
				audio_length = bundle_s
						.getInt(HomeVisitModel.Visit_audio_length);

				break;
			}
		}

	}

	private void submit() {
		HomeVisitModel visit = new HomeVisitModel();
		visit.setAbnormal_Positioning_Audio(new byte[0]);
		visit.setAbnormal_Positioning_Photo(new byte[0]);
		visit.setAbnormal_Positioning_Remark("");
		visit.setAppraise_State(0);
		visit.setBcompany_Id(pharmacyInfoModel.getBcompanyId());
		visit.setClerk_Id(clerk.getId());
		visit.setDoor_Photo(ImageUtil.bitmap2Bytes(door_bitmap));
		visit.setFamily_Visit_Id(IdGenerator.getId());
		visit.setGift_Photo(ImageUtil.bitmap2Bytes(gift_bitmap));
		visit.setIsSynchronized(0);
		if (null == user.getSellerCode() || "".equals(user.getSellerCode())) {
			user = UserSessionUtil.getUser(this);
		}
		visit.setSellerCode(user.getSellerCode());
		visit.setUser_Id(user.getUserName());
		if (null != summary_audio && !"".equals(summary_audio)) {
			visit.setVisit_audio(ImageUtil.File2byte(Environment
					.getExternalStorageDirectory()
					+ File.separator
					+ "Android"
					+ File.separator
					+ "data"
					+ File.separator
					+ HomeVisitActivity.this.getPackageName()
					+ File.separator
					+ summary_audio));
			visit.setVisit_audio_File(summary_audio);
			visit.setVisit_audio_length(audio_length);
		}
		visit.setVisit_in_Time(visit_in_Time);
		if (null != location && location.getLatitude() > 10
				&& location.getLongitude() > 20) {
			visit.setVisit_in_Lat(location.getLatitude() + "");

			visit.setVisit_in_Lon(location.getLongitude() + "");

			visit.setVisit_out_Lat(location.getLatitude() + "");

			visit.setVisit_out_Lon(location.getLongitude() + "");
			
			if(null!=location.getAddrStr()&&!"".equals(location.getAddrStr())){
			visit.setVisit_in_Location(txt_address.getText().toString());
			visit.setVisit_out_Location(txt_address.getText().toString());
			}else{
			visit.setVisit_in_Location("");
			visit.setVisit_out_Location("");
			}
		}else{
			visit.setVisit_in_Lat("0");
			visit.setVisit_in_Lon("0");
			visit.setVisit_out_Lat("0");
			visit.setVisit_out_Lon("0");
			visit.setVisit_in_Location(getString(R.string.exception));
			visit.setVisit_out_Location(getString(R.string.exception));
		}
		visit.setVisit_out_Time(Calendar.getInstance().getTime());
		visit.setVisit_Remark(summary_remark);

		boolean bool = familyService.insertDB(visit);
		if (bool) {
			Toast.makeText(HomeVisitActivity.this, R.string.submited, 1000)
					.show();
			HomeVisitActivity.this.finish();
		} else {
			Toast.makeText(HomeVisitActivity.this, R.string.submit_failed, 1000)
					.show();
		}
	}

	@Override
	protected void onDestroy() {		
		if (mBMapMan != null) {
			mBMapMan.stop();
			
			if(null!=local){
				local.stop();
			}
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {		
		if (mBMapMan != null) {
			mBMapMan.stop();			
		}
		super.onPause();
	}

	@Override
	protected void onResume() {		
		if (mBMapMan != null) {
			mBMapMan.start();
			if(null!=local){
				local.start();
			}
		}		
		super.onResume();
		
	}
}
