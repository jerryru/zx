package com.cn.android.zhengxun.app.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.cn.android.zhengxun.app.MyApplication;
import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.adapter.PharmacyAdapter;
import com.cn.android.zhengxun.app.constants.StringConstants;
import com.cn.android.zhengxun.app.constants.TypeIds;
import com.cn.android.zhengxun.app.data.UserData;
import com.cn.android.zhengxun.app.map.LocateHelper;
import com.cn.android.zhengxun.app.model.PharmacyInfoModel;
import com.cn.android.zhengxun.app.model.TourModel;
import com.cn.android.zhengxun.app.model.UserModel;
import com.cn.android.zhengxun.app.service.PharmacyInfoService;
import com.cn.android.zhengxun.app.service.TourInfoService;
import com.cn.android.zhengxun.app.service.impl.PharmacyInfoServiceImpl;
import com.cn.android.zhengxun.app.service.impl.TourInfoServiceImpl;
import com.cn.android.zhengxun.app.util.AppHelper;
import com.cn.android.zhengxun.app.util.HandlerCommand;
import com.cn.android.zhengxun.app.util.UserSessionUtil;
import com.cn.android.zhengxun.app.widgets.IdGenerator;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

public class TourActivity extends Activity {


	private TextView txt_title, txt_compamny, txt_address, txt_door,
			txt_display, txt_sell, txt_stock, txt_summary,tv_no_results;
	private ListView lst;
	private Button btn_back, btn_select, btn_submit;
	private RelativeLayout layout_companies, rl_door, rl_display, rl_summary,rl_search_area;
	private LinearLayout ll_search_area;
	private EditText et_search;
	private ImageView img_search,img_refesh;
	private PharmacyAdapter pharmacyAdapter;
	private List<PharmacyInfoModel> companies;
	private PharmacyInfoService pharmacyInfoService;
	private PharmacyInfoModel pharmacyInfoModel;
	private LocateHelper local = null;
	private BMapManager mBMapMan = null;
	private BDLocation location,in_location;
	private UserModel user = new UserModel();
	private Bitmap door_bitmap, display_bitmap;
	private String summary_remark, summary_audio,in_address;
	private TourModel model = new TourModel();
	private TourInfoService tourService;
	private Date tourInTime;
	private int audio_length;
	private MKPoiInfo mInfo;
	private boolean gotPoi=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBMapMan=((MyApplication)this.getApplication()).mBMapMan;
		mBMapMan.start();
		setContentView(R.layout.tour_layout);
		initViews();
		initListeners();
		initData();
		getLocations();
	}

	private void getLocations() {
		local = new LocateHelper(mBMapMan,this, handler);
		local.onCreate();
		local.start();
		local.request();

	}

	private void initListeners() {
		layout_companies.setOnClickListener(listener);
		btn_back.setOnClickListener(listener);
		btn_select.setOnClickListener(listener);
		rl_door.setOnClickListener(listener);
		rl_display.setOnClickListener(listener);
		rl_summary.setOnClickListener(listener);
		btn_submit.setOnClickListener(listener);
		img_search.setOnClickListener(listener);
		rl_search_area.setOnClickListener(listener);
		img_refesh.setOnClickListener(listener);
	}

	private void initViews() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_compamny = (TextView) findViewById(R.id.tv_search_topic);
		txt_address = (TextView) findViewById(R.id.tv_current_lcaotion);
		rl_door = (RelativeLayout) findViewById(R.id.rl_tour_door_photo_layout);
		rl_display = (RelativeLayout) findViewById(R.id.rl_tour_display_photo_layout);
		rl_summary = (RelativeLayout) findViewById(R.id.rl_tour_summary_layout);
		btn_back = (Button) findViewById(R.id.back);
		btn_select = (Button) findViewById(R.id.btn_right);
		lst = (ListView) findViewById(R.id.lst_company_tour);
		layout_companies = (RelativeLayout) findViewById(R.id.layout_pop_source);
		ll_search_area = (LinearLayout) findViewById(R.id.ll_company_layout);
		txt_title.setText(R.string.tour);
		pharmacyAdapter = new PharmacyAdapter(this, handler);
		btn_select.setVisibility(View.VISIBLE);
		tourService = new TourInfoServiceImpl(this, handler);
		btn_submit = (Button) findViewById(R.id.btn_submit_tour);
		txt_door = (TextView) findViewById(R.id.tv_gate_photo);
		txt_display = (TextView) findViewById(R.id.tv_display_photo);
		txt_sell = (TextView) findViewById(R.id.tv_sell_form);
		txt_stock = (TextView) findViewById(R.id.tv_stock_form);
		txt_summary = (TextView) findViewById(R.id.tv_summary);		
		txt_sell.setTextColor(this.getResources().getColor(R.color.gray));
		txt_stock.setTextColor(this.getResources().getColor(R.color.gray));
		
		et_search=(EditText)findViewById(R.id.et_search_area);
		img_search=(ImageView)findViewById(R.id.img_search);
		img_refesh=(ImageView)findViewById(R.id.img_refresh);
		
		rl_search_area=(RelativeLayout)findViewById(R.id.rl_search_area);
		tv_no_results=(TextView)findViewById(R.id.tv_search_no_results);
	}

	private void initData() {
		user.setUserName(UserData.getInstance().getUser_id());
		user.setSellerCode(UserData.getInstance().getSellerCode());
		pharmacyInfoService = new PharmacyInfoServiceImpl(this, handler);
		if (null == user.getSellerCode() || "".equals(user.getSellerCode())) {
			user = UserSessionUtil.getUser(this);
		}
		companies = pharmacyInfoService.getPharmacyInfoBySellerCodeForTour(user
				.getSellerCode());
		pharmacyAdapter.setCompanies(companies);
		lst.setAdapter(pharmacyAdapter);
		lst.invalidateViews();
		txt_compamny.setText(R.string.none);
	}

	private Handler handler = new Handler() {

		

		

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HandlerCommand.GOT_COMPANY:
				pharmacyInfoModel = pharmacyAdapter.getSelectedModel();
				txt_compamny.setText(pharmacyInfoModel.getBcompnayName());
				ll_search_area.setVisibility(View.GONE);
				txt_door.setTextColor(TourActivity.this.getResources()
						.getColor(R.color.orange));
				break;
			case HandlerCommand.GOT_LOCATION:
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
				if(null==in_location){
					in_location=location;
					if(null!=location.getAddrStr()){
					in_address=location.getAddrStr();}
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
					if(!gotPoi&&null!=in_location&&!"".equals(in_location.getAddrStr())){						
						in_address=txt_address.getText().toString();
						gotPoi=true;
						}
				}
				break;
			}
		}
	};

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_pop_source:
				ll_search_area.setVisibility(View.VISIBLE);
				tv_no_results.setVisibility(View.GONE);
				break;
			case R.id.back:
				TourActivity.this.finish();
				break;
			case R.id.rl_tour_door_photo_layout:
								
				if (null != pharmacyInfoModel) {
					tourInTime = new Date();
					Intent intent = new Intent();
					intent.setClass(TourActivity.this, PhotoActivity.class);
					intent.putExtra(StringConstants.TYPE, TypeIds.TOUR_DOOR);
					TourActivity.this.startActivityForResult(intent,
							TypeIds.TOUR_DOOR);

				} else {
					Toast.makeText(TourActivity.this,
							R.string.select_shop_hint, 1000).show();
				}
				break;
			case R.id.rl_tour_display_photo_layout:
				if (null != door_bitmap) {
					Intent intent_d = new Intent();
					intent_d.setClass(TourActivity.this, PhotoActivity.class);
					intent_d.putExtra(StringConstants.TYPE,
							TypeIds.TOUR_DISPALY);
					TourActivity.this.startActivityForResult(intent_d,
							TypeIds.TOUR_DISPALY);

				} else {
					Toast.makeText(TourActivity.this, R.string.door_photo_hint,
							1000).show();
				}
				break;
			case R.id.rl_tour_summary_layout:
				if (null != display_bitmap) {
					Intent intent_s = new Intent();
					intent_s.setClass(TourActivity.this, SummaryActivity.class);
					intent_s.putExtra(StringConstants.TYPE,
							TypeIds.TOUR_SUMMARY);
					TourActivity.this.startActivityForResult(intent_s,
							TypeIds.TOUR_SUMMARY);

				} else {
					Toast.makeText(TourActivity.this,
							R.string.display_photo_hint, 1000).show();
				}
				break;
			case R.id.btn_right:
				Intent intent = new Intent();
				intent.setClass(TourActivity.this, TourListActivity.class);
				TourActivity.this.startActivity(intent);
				break;
			case R.id.btn_submit_tour:
				if (null == door_bitmap || null == display_bitmap
						|| null == summary_remark || null == pharmacyInfoModel) {
					AppHelper.showExitDialog(TourActivity.this,
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
					sellerCode = UserSessionUtil.getUser(TourActivity.this).getSellerCode();
				}
				companies=pharmacyInfoService.getPharmacyInfoByNameForTour(sellerCode,search_input);
				tv_no_results.setVisibility(View.GONE);
				pharmacyAdapter.setCompanies(companies);
				lst.setAdapter(pharmacyAdapter);
				lst.invalidateViews();
				if(null==companies||companies.size()==0){
					tv_no_results.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.rl_search_area:
				break;
			case R.id.img_refresh:
				if(null!=local){
					local.start();
					local.request();
				}
				break;
			}

		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Toast.makeText(TourActivity.this, R.string.saved, 1000).show();
			switch (requestCode) {
			case TypeIds.TOUR_DOOR:
				Bundle bundle = data.getExtras();
				door_bitmap = bundle
						.getParcelable(StringConstants.TOUR_DOOR_PHOTO_OK);
				txt_door.setTextColor(TourActivity.this.getResources()
						.getColor(R.color.green));
				txt_display.setTextColor(TourActivity.this.getResources()
						.getColor(R.color.orange));
				break;
			case TypeIds.TOUR_DISPALY:
				Bundle bundle_d = data.getExtras();
				display_bitmap = bundle_d
						.getParcelable(StringConstants.TOUR_DISPLAY_PHOTO_OK);
				txt_display.setTextColor(TourActivity.this.getResources()
						.getColor(R.color.green));
				txt_summary.setTextColor(TourActivity.this.getResources()
						.getColor(R.color.orange));
				break;
			case TypeIds.TOUR_SUMMARY:
				txt_summary.setTextColor(TourActivity.this.getResources()
						.getColor(R.color.green));
				Bundle bundle_s = data.getExtras();
				summary_remark = bundle_s
						.getString(StringConstants.SUMMARY_TEXT);
				summary_audio = bundle_s
						.getString(StringConstants.SUMMARY_AUDIO);
				audio_length = bundle_s.getInt(TourModel.Tour_audio_length);
				// boolean bool = tourService.synChronizeWithServer(model);
				// if (bool) {
				// Toast.makeText(TourActivity.this,
				// "Result server OK", 1000).show();
				// model.setIsSynchronized(1);
				// // tourService.updateInDB(model);
				// }
				break;
			}
		}

	}

	private void submit() {
		model.setAbnormal_Positioning_Audio(new byte[0]);
		model.setAbnormal_Positioning_Photo(new byte[0]);
		model.setAbnormal_Positioning_Remark("");
		model.setAppraiseState(0);
		model.setBcompanyId(pharmacyInfoModel.getBcompanyId());
		model.setDisplay_Photo(bitmap2Bytes(display_bitmap));
		model.setDoor_Photo(bitmap2Bytes(door_bitmap));
		model.setIsSynchronized(1);
		model.setSellercode(pharmacyInfoModel.getSellerCode());
		model.setStockState(0);
		if (null != summary_audio && !"".equals(summary_audio)) {
			model.setTour_audio(File2byte(Environment
					.getExternalStorageDirectory()
					+ File.separator
					+ "Android"
					+ File.separator
					+ "data"
					+ File.separator
					+ this.getPackageName() + File.separator + summary_audio));
			model.setTour_audio_file(summary_audio);
			model.setTour_audio_length(audio_length);
		}
		if (null != location && location.getLatitude() > 10
				&& location.getLongitude() > 20) {
			model.setIsNormal(1);
					
			model.setTourOutLat(location.getLatitude() + "");
			model.setTourOutLon(location.getLongitude() + "");
			if(null!=location.getAddrStr()&&!"".equals(location.getAddrStr())){				
			model.setTour_out_Location(txt_address.getText().toString());
			}else{
				
				model.setTour_out_Location("");
			}
		} else {
			model.setIsNormal(0);			
			model.setTour_out_Location(getString(R.string.exception));
			model.setTourOutLat("0");
			model.setTourOutLon("0");
		}
		if (null != in_location && in_location.getLatitude() > 10
				&& in_location.getLongitude() > 20) {
			model.setIsNormal(1);
			model.setTour_in_lat(in_location.getLatitude() + "");
			model.setTour_in_lot(in_location.getLongitude() + "");			
			
			if(null!=in_location.getAddrStr()&&!"".equals(in_location.getAddrStr())){				
			model.setTour_in_Location(in_address);
			
			}else{
				model.setTour_in_Location("");				
			}
		} else {
			model.setIsNormal(0);
			model.setTour_in_lat("0");
			model.setTour_in_lot("0");
			model.setTour_in_Location(getString(R.string.exception));

		}
		model.setTour_Remark(summary_remark);
		model.setTourInTime(tourInTime);
		model.setTour_out_Time(new Date());
		model.setUserId(user.getUserName());
		model.setTourId(IdGenerator.getId());

		boolean bool = tourService.insertDB(model);
		if (bool) {
			Toast.makeText(TourActivity.this, R.string.submited, 1000).show();
			TourActivity.this.finish();
		} else {
			Toast.makeText(TourActivity.this, R.string.submit_failed, 1000)
					.show();
		}
	}

	public byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static byte[] File2byte(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
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
