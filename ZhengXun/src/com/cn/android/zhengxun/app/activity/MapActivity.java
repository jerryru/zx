package com.cn.android.zhengxun.app.activity;

import java.io.File;
import java.util.Date;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.cn.android.zhengxun.app.MyApplication;
import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.constants.StringConstants;
import com.cn.android.zhengxun.app.constants.TypeIds;
import com.cn.android.zhengxun.app.data.UserData;
import com.cn.android.zhengxun.app.map.LocateHelper;
import com.cn.android.zhengxun.app.map.listener.MySearchListener;
import com.cn.android.zhengxun.app.model.AttendenceInfoModel;
import com.cn.android.zhengxun.app.model.PharmacyInfoModel;
import com.cn.android.zhengxun.app.model.UserModel;
import com.cn.android.zhengxun.app.service.DailyWorkService;
import com.cn.android.zhengxun.app.service.impl.DailyWorkServiceImpl;
import com.cn.android.zhengxun.app.util.AppHelper;
import com.cn.android.zhengxun.app.util.HandlerCommand;
import com.cn.android.zhengxun.app.util.ImageUtil;
import com.cn.android.zhengxun.app.util.UserSessionUtil;
import com.cn.android.zhengxun.app.widgets.IdGenerator;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MapActivity extends Activity {

	protected static final int REQUESTCODE = 0;
	private BMapManager mBMapMan = null;
	private MapView mMapView = null;
	private LocateHelper local = null;
	private TextView txt_address;// txt_company;
	private Button btn_abnormal;
	private Button btn_back, btn_submit;
	// private ListView lst;
	// private RelativeLayout layout_pop;
	private LocationData locData;
	private BDLocation location;
	private MKPoiInfo mInfo;
	private DailyWorkService dailyWorkService;
	// private List<PharmacyInfoModel> companies;
	// private PharmacyInfoService pharmacyInfoService;
	private PharmacyInfoModel pharmacyInfoModel;
	// private PharmacyAdapter pharmacyAdapter;
	private Bitmap bitmap;
	private String AudiofileName;
	private String abnormal_remark;
	private int typeId;
	private TextView txt_title;
	private MyLocationOverlay myLocationOverlay = null;

	// private UserModel user = new UserModel();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBMapMan = ((MyApplication) this.getApplication()).mBMapMan;
		if (mBMapMan == null) {
			mBMapMan = new BMapManager(getApplicationContext());
			/**
			 * 如果BMapManager没有初始化则初始化BMapManager
			 */
			mBMapMan.init(null);
		}
		// 注意：请在试用setContentView前初始化BMapManager对象，否则会报错
		setContentView(R.layout.map_layout);
		initViews();
		initListeners();
		initData();
		check();
		initMap();
		initLocation();
		// initOverLays();

	}

	private void initOverLays() {
		myLocationOverlay = new MyLocationOverlay(mMapView);
		try {
			if (null != mMapView.getOverlays() && null != myLocationOverlay) {
				myLocationOverlay.setData(locData);
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(myLocationOverlay);
			}

			mMapView.refresh();

		} catch (NullPointerException e) {
			Log.e("exception", "NullPointerException", e);
		}
	}

	private void check() {
		if (!LocateHelper.isOpenGPS(this) && !AppHelper.isNetworkConected(this)) {
			AppHelper.showExitDialog(this, getString(R.string.gps_not_open),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// Intent intent = new Intent();
							// intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							// try
							// {
							// MapActivity.this.startActivity(intent);
							//
							//
							// } catch(ActivityNotFoundException ex)
							// {
							//
							// // The Android SDK doc says that the location
							// settings activity
							// // may not be found. In that case show the
							// general settings.
							//
							// // General settings activity
							// intent.setAction(Settings.ACTION_SETTINGS);
							// try {
							// MapActivity.this.startActivity(intent);
							// } catch (Exception e) {
							// }
							// }
						}

					});
		}

	}

	private void initViews() {
		mMapView = (MapView) findViewById(R.id.bmapsView);
		txt_address = (TextView) findViewById(R.id.txt_current_location);
		txt_title = (TextView) findViewById(R.id.txt_title);
		/* btn_refresh = (ImageView) findViewById(R.id.btn_refefsh); */
		btn_back = (Button) findViewById(R.id.back);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_abnormal = (Button) findViewById(R.id.btn_abnormal_submit);
		// txt_company = (TextView) findViewById(R.id.tv_search_topic);
		// lst = (ListView) findViewById(R.id.lst_company);
		/* layout_pop = (RelativeLayout) findViewById(R.id.layout_pop_source); */
	}

	private void initMap() {
		mMapView.setBuiltInZoomControls(true);
		// 设置启用内置的缩放控件
		MapController mMapController = mMapView.getController();
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		GeoPoint point = new GeoPoint((int) (39.915 * 1E6),
				(int) (116.404 * 1E6));
		// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mMapController.setCenter(point);// 设置地图中心点
		mMapController.setZoom(12);// 设置地图zoom级别
	}

	private void initLocation() {

		local = new LocateHelper(mBMapMan, this, handler);
		local.onCreate();

		local.start();
		local.request();

	}

	private void initListeners() {

		btn_back.setOnClickListener(listener);
		btn_submit.setOnClickListener(listener);
		btn_abnormal.setOnClickListener(listener);
		/* layout_pop.setOnClickListener(listener); */

	}

	private void initData() {
		/*
		 * pharmacyInfoService = new PharmacyInfoServiceImpl(this, handler);
		 * 
		 * user.setUserName(UserData.getInstance().getUser_id());
		 * user.setSellerCode(UserData.getInstance().getSellerCode()); if (null
		 * != user.getSellerCode()||!"".equals(user.getSellerCode())) {
		 * companies = pharmacyInfoService.getPharmacyInfoBySellerCode(user
		 * .getSellerCode()); }
		 */
		// if(companies.size()>0){
		// pharmacyInfoModel=companies.get(0);
		// txt_company.setText(companies.get(0).getBcompnayName());
		// }else{
		// txt_company.setText(R.string.none);
		// // }
		// pharmacyAdapter = new PharmacyAdapter(this, handler);
		// pharmacyAdapter.setCompanies(companies);
		// lst.setAdapter(pharmacyAdapter);
		// lst.invalidateViews();
		Intent intent = this.getIntent();
		typeId = intent.getIntExtra(StringConstants.TYPE, -1);
		pharmacyInfoModel = new PharmacyInfoModel();
		pharmacyInfoModel.setBcompanyId(intent
				.getStringExtra(PharmacyInfoModel.BCOMPANYID));
		pharmacyInfoModel.setBregaddress(intent
				.getStringExtra(PharmacyInfoModel.BREG_ADDRESS));
		pharmacyInfoModel.setCustomerLat(intent
				.getStringExtra(PharmacyInfoModel.CUSTOMER_LAT));
		pharmacyInfoModel.setCustomerLot(intent
				.getStringExtra(PharmacyInfoModel.CUSTOMER_LOT));
		switch (typeId) {
		case TypeIds.DAILY_WORK_SIGN_IN:
			txt_title.setText(R.string.signin);
			break;
		case TypeIds.DAILY_WORK_SIGN_OUT:
			txt_title.setText(R.string.signout);
			break;
		}
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// case R.id.btn_refefsh:
			// local.start();
			// local.request();
			// break;
			case R.id.back:
				MapActivity.this.finish();
				break;
			case R.id.btn_submit:
				submit();
				break;
			case R.id.btn_abnormal_submit:
				Intent intent = new Intent();
				intent.setClass(MapActivity.this, AbnormalRemarkActivity.class);

				MapActivity.this.startActivityForResult(intent, REQUESTCODE);
				break;
			/*
			 * case R.id.layout_pop_source: lst.setVisibility(View.VISIBLE);
			 * break;
			 */
			}

		}

	};

	private void submit() {

		submitData(typeId);

	}

	private void submitData(int typeId) {
		dailyWorkService = new DailyWorkServiceImpl(this);
		AttendenceInfoModel dailyWorkData = new AttendenceInfoModel();
		switch (typeId) {
		case TypeIds.DAILY_WORK_SIGN_IN:
			dailyWorkData.setAttendanceToken(TypeIds.DAILY_WORK_SIGN_IN);
			if (null != pharmacyInfoModel) {
				getData(dailyWorkData);
				boolean isExist = dailyWorkService.isExist(dailyWorkData);
				if (!isExist) {
					boolean bool = dailyWorkService.sigin(dailyWorkData);
					if (!bool) {
						// boolean
						// isSynchronized=dailyWorkService.addAttendenceToServer(dailyWorkData);
						// if(isSynchronized){
						// dailyWorkData.setIsSynchronized(1);
						// dailyWorkService.update(dailyWorkData);}
						// MapActivity.this.finish();}else{
						Toast.makeText(MapActivity.this,
								R.string.signined_failed, 1000).show();
					} else {
						Toast.makeText(MapActivity.this, R.string.signined,
								1000).show();
						Intent intent = new Intent();
						intent.setClass(MapActivity.this,
								DailyWorkSheetActivity.class);
						setResult(RESULT_OK, intent);
						MapActivity.this.finish();
					}
				} else {
					dailyWorkService.update(dailyWorkData);
					Toast.makeText(MapActivity.this, R.string.signined, 1000)
							.show();
					Intent intent = new Intent();
					intent.setClass(MapActivity.this,
							DailyWorkSheetActivity.class);
					setResult(RESULT_OK, intent);
					MapActivity.this.finish();
				}
			} else {
				Toast.makeText(MapActivity.this, R.string.select_shop, 1000)
						.show();

			}

			break;
		case TypeIds.DAILY_WORK_SIGN_OUT:

			dailyWorkData.setAttendanceToken(TypeIds.DAILY_WORK_SIGN_OUT);
			if (null != pharmacyInfoModel) {
				getData(dailyWorkData);
				boolean isExist = dailyWorkService.isExist(dailyWorkData);
				if (!isExist) {
					boolean bool = dailyWorkService.sigout(dailyWorkData);

					if (!bool) {
						// boolean
						// isSynchronized=dailyWorkService.addAttendenceToServer(dailyWorkData);
						// if(isSynchronized){
						// dailyWorkData.setIsSynchronized(1);
						// dailyWorkService.update(dailyWorkData);}
						// MapActivity.this.finish();}else{
						Toast.makeText(MapActivity.this,
								R.string.signouted_failed, 1000).show();
					} else {
						Toast.makeText(MapActivity.this, R.string.signouted,
								1000).show();
						Intent intent = new Intent();
						intent.setClass(MapActivity.this,
								DailyWorkSheetActivity.class);
						setResult(RESULT_OK, intent);
						MapActivity.this.finish();
					}
				} else {
					dailyWorkService.update(dailyWorkData);
					Toast.makeText(MapActivity.this, R.string.signouted, 1000)
							.show();
					Intent intent = new Intent();
					intent.setClass(MapActivity.this,
							DailyWorkSheetActivity.class);
					setResult(RESULT_OK, intent);
					MapActivity.this.finish();
				}

			} else {
				Toast.makeText(MapActivity.this, R.string.select_shop, 1000)
						.show();
			}
			break;
		}
	}

	private void getData(AttendenceInfoModel dailyWorkData) {
		UserModel user = new UserModel();
		user.setUserName(UserData.getInstance().getUser_id());
		user.setSellerCode(UserData.getInstance().getSellerCode());
		if (null == user.getSellerCode() || "".equals(user.getSellerCode())) {
			user = UserSessionUtil.getUser(this);
		}
		if (location != null && location.getLatitude() > 5
				&& location.getLongitude() > 10) {
			dailyWorkData.setLat(location.getLatitude() + "");
			dailyWorkData.setLot(location.getLongitude() + "");

			if (null != location.getAddrStr()
					&& !"".equals(location.getAddrStr())) {

				dailyWorkData.setAttendanceLoaction(txt_address.getText()
						.toString());
				dailyWorkData.setAttendence_address(txt_address.getText()
						.toString());
			} else {
				dailyWorkData.setAttendanceLoaction("");
				dailyWorkData.setAttendence_address("");
			}
		} else {
			dailyWorkData.setLat("0");
			dailyWorkData.setLot("0");
			dailyWorkData.setAttendanceLoaction(getString(R.string.exception));
			dailyWorkData.setAttendence_address(getString(R.string.exception));
		}

		dailyWorkData.setAttendceId(IdGenerator.getId());
		dailyWorkData.setAttendenceTime(new Date());
		if (null != pharmacyInfoModel) {
			dailyWorkData.setBcompanyId(pharmacyInfoModel.getBcompanyId());
		} else {
			AppHelper.showExitDialog(this,
					this.getString(R.string.select_shop_hint),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}

					});
		}
		if (null != bitmap || null != this.AudiofileName
				|| null != this.abnormal_remark) {
			if (null != bitmap) {
				dailyWorkData.setAbnorma_PositioningPhoto(ImageUtil
						.bitmap2Bytes(bitmap));
			}
			if (null != this.AudiofileName && !"".equals(AudiofileName)) {
				dailyWorkData.setAbnormalPositioningAudio(ImageUtil
						.File2byte(Environment.getExternalStorageDirectory()
								+ File.separator + "Android" + File.separator
								+ "data" + File.separator
								+ this.getPackageName() + File.separator
								+ this.AudiofileName));
				dailyWorkData.setAbnormalPositioningAudioFile(AudiofileName);
			}
			if (null != this.abnormal_remark && !"".equals(abnormal_remark)) {
				dailyWorkData.setAbnormal_Positioning_Remark(abnormal_remark);
			}
		} else {
			dailyWorkData.setAttendenceType(1);
			dailyWorkData.setAbnormalPositioningRemark("");
			dailyWorkData.setAbnorma_PositioningPhoto(new byte[0]);
			dailyWorkData.setAbnormalPositioningAudio(new byte[0]);
		}
		dailyWorkData.setIsSynchronized(0);

		dailyWorkData.setSellerCode(user.getSellerCode());

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HandlerCommand.GOT_LOCATION:
				if (null != local) {

					location = local.getMlocation();

					locData = new LocationData();
					if (null != location) {
						if (location.getLatitude() > 5
								&& location.getLongitude() > 10) {

							locData.latitude = location.getLatitude();
							locData.longitude = location.getLongitude();
							locData.direction = location.getDirection();
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

					initOverLays();
					mMapView.getController().animateTo(
							new GeoPoint((int) (locData.latitude * 1e6),
									(int) (locData.longitude * 1e6)));
					// mMKSearch.reverseGeocode(new GeoPoint((int)
					// (locData.latitude
					// * 1e6),
					// (int) (locData.longitude * 1e6))); //逆地址解析
				} else {
					local = new LocateHelper(mBMapMan, MapActivity.this,
							handler);
					local.onCreate();

					local.start();
				}
				break;
			case HandlerCommand.GOT_POI:
				// mPoiLocation=local.getMpoiLocation();
				//
				// if(null!=location){
				// txt_address.setText(location.getAddrStr()+mPoiLocation.getAddrStr()+"附近");
				// }else{
				// txt_address.setText(mPoiLocation.getAddrStr()+"附近");
				// }
				mInfo = local.getPoiInfo();
				if (null != location && null != mInfo) {
					GeoPoint start = new GeoPoint(
							(int) (location.getLatitude() * 1e6),
							(int) (location.getLongitude() * 1e6));
					GeoPoint end = mInfo.pt;
					int distance = (int) DistanceUtil.getDistance(start, end);
					if (null != location.getAddrStr()) {
						txt_address.setText(location.getAddrStr() + mInfo.name
								+ local.getDirection() + distance + "米附近");
					} else {
						txt_address.setText(mInfo.name + local.getDirection()
								+ distance + "米附近");
					}
				}
				break;
			/*
			 * case HandlerCommand.GOT_COMPANY:
			 * 
			 * lst.setVisibility(View.GONE); pharmacyInfoModel =
			 * pharmacyAdapter.getSelectedModel();
			 * txt_company.setText(pharmacyInfoModel.getBcompnayName()); break;
			 */
			}
		}

	};

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		if (mBMapMan != null) {
			if (null != local) {
				local.stop();
			}
			mBMapMan.stop();
		}

		super.onDestroy();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		if (mBMapMan != null) {
			mBMapMan.stop();

		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		if (mBMapMan != null) {
			mBMapMan.start();

		}
		super.onResume();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUESTCODE:
				Bundle bundle = data.getExtras();
				bitmap = bundle
						.getParcelable(StringConstants.ABNORMAL_POSITIONING_PHOTO);
				AudiofileName = bundle.getString(StringConstants.SUMMARY_AUDIO);
				this.abnormal_remark = bundle
						.getString(StringConstants.ABNORMAL_POSITIONING_REMARK);
				submit();

				break;
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}
}
