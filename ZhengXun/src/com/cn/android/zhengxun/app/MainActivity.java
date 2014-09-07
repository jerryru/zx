package com.cn.android.zhengxun.app;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.cn.android.zhengxun.app.adapter.GridViewAdapter;
import com.cn.android.zhengxun.app.data.CategoryData;
import com.cn.android.zhengxun.app.data.UserData;
import com.cn.android.zhengxun.app.model.AttendenceInfoModel;
import com.cn.android.zhengxun.app.model.HomeVisitModel;
import com.cn.android.zhengxun.app.model.TourModel;
import com.cn.android.zhengxun.app.model.UserModel;
import com.cn.android.zhengxun.app.service.CategoryDataService;
import com.cn.android.zhengxun.app.service.DailyWorkService;
import com.cn.android.zhengxun.app.service.FamilyVisitService;
import com.cn.android.zhengxun.app.service.SynchronizationService;
import com.cn.android.zhengxun.app.service.TourInfoService;
import com.cn.android.zhengxun.app.service.impl.CategoryDataServiceImpl;
import com.cn.android.zhengxun.app.service.impl.DailyWorkServiceImpl;
import com.cn.android.zhengxun.app.service.impl.FamilyVisitServiceImpl;
import com.cn.android.zhengxun.app.service.impl.SynchronizationServiceImpl;
import com.cn.android.zhengxun.app.service.impl.TourInfoServiceImpl;
import com.cn.android.zhengxun.app.util.AppHelper;
import com.cn.android.zhengxun.app.util.HandlerCommand;
import com.cn.android.zhengxun.app.util.ScreenManager;
import com.cn.android.zhengxun.app.util.UserSessionUtil;
import com.cn.android.zhengxun.app.widgets.FootBarView;
import com.cn.android.zhengxun.app.widgets.FootBarView.OnClickMenuBarLisntener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity {

	
	private FootBarView footMenuBar;
	private GridView gv;
	private GridViewAdapter adapter;
	private CategoryDataService cService;
	private List<CategoryData> categoryList;
	private TextView txt_title;
	private Button btn_exit;
	private ImageView img_more;
	private UserModel user=new UserModel();
	private SynchronizationService synService;
	private DailyWorkService daillyWorkService;
	private TourInfoService tourService;
	private FamilyVisitService visitService;
	private BMapManager mBMapMan = null;
	private MKSearch mMKSearch = null; 
	private MKAddrInfo addressInfo=null;
	private int type=0;
	private String id="";
	private boolean isFinished=false;
	private ProgressDialog proDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScreenManager.init(this.getApplicationContext());
		mBMapMan=((MyApplication)this.getApplication()).mBMapMan;
		if(null==mBMapMan){
			mBMapMan = new BMapManager(getApplicationContext());
            /**
             * 如果BMapManager没有初始化则初始化BMapManager
             */
			mBMapMan.init(null);
		}
		mBMapMan.start();
		
		setContentView(R.layout.activity_main);

		initView();
		init();
		setListners();
		
	}
	
	

	private void initView() {
		footMenuBar = (FootBarView) findViewById(R.id.bar_menu);
		gv = (GridView) findViewById(R.id.gv_items);
		txt_title=(TextView)findViewById(R.id.txt_main_title);
		btn_exit=(Button)findViewById(R.id.btn_exit);
		img_more=(ImageView)findViewById(R.id.img_more_bg);
		adapter=new GridViewAdapter(this,handler);		

	}

	private void setListners() {
		footMenuBar.setOnClickMenuBarLisntener(new OnClickMenuBarLisntener() {
			@Override
			public void onClickMenuBar(int index) {
				showSelectedView(index);
				showContentView(index);
				adapter();
				
			}

			
			

		});
		
		btn_exit.setOnClickListener(listener);
	}
	private android.view.View.OnClickListener listener=new android.view.View.OnClickListener(){

		@Override
		public void onClick(View v) {
		
			switch(v.getId()){
			case R.id.btn_exit:
				showExitDailog();
			
				break;
			}
			
		}

		
		
	};
	private void showExitDailog() {
		AppHelper.showExitDialog(this, getString(R.string.confirm),
				new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						MainActivity.this.finish();
					}
				});
	}
	private void adapter() {
		adapter.setUser(user);
		adapter.setCategoryList(categoryList);
		gv.setAdapter(adapter);
	}

	private void init() {
		cService=new CategoryDataServiceImpl(this);
		
		categoryList=new ArrayList<CategoryData>();
		user.setUserName(UserData.getInstance().getUser_id());
		user.setSellerCode(UserData.getInstance().getSellerCode());
		
		if(null!=user){
			Intent intent=getIntent();
			user=new UserModel();
			user.setUserName(intent.getStringExtra(UserModel.USER_NAME));
			user.setPassword(intent.getStringExtra(UserModel.USER_PSW));
			if(null!=intent.getStringExtra(UserModel.SELLER_CODE)){
			user.setSellerCode(intent.getStringExtra(UserModel.SELLER_CODE));
			}
			if (null==user||null == user.getSellerCode()||"".equals(user.getSellerCode())){
				user=UserSessionUtil.getUser(this);
			}
			
		}
		loadWorkSheetData();
		
		adapter();
		
	}
	private void showSelectedView(int index) {
		switch (index) {
		case 0:

			footMenuBar.setFootBarMargin(0, AppHelper.dip2px(this, -6), 0, 0);
			footMenuBar.setSelectTab(index);
			break;
		case 1:

			footMenuBar.setFootBarMargin(0, 0, 0, 0);
			footMenuBar.setSelectTab(index);

			break;
		case 2:

			footMenuBar.setFootBarMargin(0, 0, 0, 0);
			footMenuBar.setSelectTab(index);
			break;
		case 3:

			footMenuBar.setFootBarMargin(0, 0, 0, 0);
			footMenuBar.setSelectTab(index);
			break;
		}
		
	}

	private void showContentView(int index) {
		
		switch(index)
		{
		case 0:
			img_more.setVisibility(View.GONE);
			loadWorkSheetData();
			break;
		case 1:
			categoryList=cService.getData("MyAssistant.xml");
			txt_title.setText(R.string.sell_assistant);
			img_more.setVisibility(View.GONE);
			break;
		case 2:
			categoryList=cService.getData("MyRecords.xml");
			txt_title.setText(R.string.personal_center);
			img_more.setVisibility(View.GONE);
			break;
		case 3:
			categoryList=new ArrayList<CategoryData>();
			txt_title.setText(R.string.more);
			img_more.setVisibility(View.VISIBLE);
			AppHelper.showExitDialog(this, getString(R.string.developing),
					new OnClickListener()
					{

						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							
						}
					});
			break;
		}
	}



	private void loadWorkSheetData() {
		categoryList=cService.getData("DailyWork.xml");
		txt_title.setText(R.string.daily_work);
	}
	

	@Override
	public void onBackPressed() {
		
		showExitDailog();
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	private MKSearchListener localListener=new MKSearchListener(){

		@Override
		public void onGetAddrResult(MKAddrInfo res, int error) {
			if (error != 0) {
		        String str = String.format("错误号：%d", error);		        
		        return;
		        }
		    //地图移动到该点
//		    mMapView.getController().animateTo(res.geoPt);
		    if (res.type == MKAddrInfo.MK_GEOCODE) {
		        //地理编码：通过地址检索坐标点
		        String strInfo = String.format("纬度：%f 经度：%f", res.geoPt.getLatitudeE6()/1e6, res.geoPt.getLongitudeE6()/1e6);
		      
		    }
		    if (res.type == MKAddrInfo.MK_REVERSEGEOCODE) {
		   
		    	switch(type){
		    	case 0:
		    		if(null==daillyWorkService){
		    			daillyWorkService = new DailyWorkServiceImpl(MainActivity.this);
		    		}
		    		AttendenceInfoModel attendence=new AttendenceInfoModel();
		    		attendence.setAttendceId(id);
		    		attendence.setAttendanceLoaction(res.strAddr);
		    		attendence.setAttendence_address(res.strAddr);
		    		Toast.makeText(MainActivity.this,res.strAddr, 1000).show();
		    		daillyWorkService.updateLocal(attendence);
//		    		getAysnLocal();
		    		break;
		    	case 1:
		    		if(null==tourService){
		    			tourService = new TourInfoServiceImpl(MainActivity.this);
		    		}
		    		TourModel tour=new TourModel();
		    		tour.setTourId(id);
		    		tour.setTour_in_Location(res.strAddr);
		    		tour.setTour_out_Location(res.strAddr);
		    		Toast.makeText(MainActivity.this,res.strAddr, 1000).show();
		    		tourService.updateInDB(tour);
//		    		getAysnLocal();
		    		break;
		    	case 2:
		    		if(null==visitService){
		    			visitService = new FamilyVisitServiceImpl(MainActivity.this);
		    		}
		    		HomeVisitModel visit=new HomeVisitModel();
		    		visit.setFamily_Visit_Id(id);
		    		visit.setVisit_in_Location(res.strAddr);
		    		visit.setVisit_out_Location(res.strAddr);
		    		Toast.makeText(MainActivity.this,res.strAddr, 1000).show();
		    		visitService.updateInDB(visit);
//		    		getAysnLocal();
		    		break;
		    	}
		    	Message msg=handler.obtainMessage(HandlerCommand.GOT_LOCATION);
		    	handler.sendMessage(msg);
		    }
		}

		

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
				int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

	
	};
	private Handler handler=new Handler(){

		

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case HandlerCommand.BEGIN_SUBMIT:
				if (AppHelper.isNetworkConected(MainActivity.this)) {
				proDialog = new ProgressDialog(MainActivity.this);								
				proDialog.setMessage(MainActivity.this
						.getString(R.string.begin_submit));
				if (null != proDialog) {
					proDialog.show();
				}
				synService=new SynchronizationServiceImpl(MainActivity.this);
				mMKSearch = new MKSearch();  
				mMKSearch.init(mBMapMan, localListener);//注意，MKSearchListener只支持一个，以最后一次设置为准  
				
				final List<AttendenceInfoModel> dailyWorkDatas;
				daillyWorkService = new DailyWorkServiceImpl(MainActivity.this);
				dailyWorkDatas = daillyWorkService.getUnsynLocalAttendenceInfos();
				
				tourService = new TourInfoServiceImpl(MainActivity.this);
				final List<TourModel> tours = tourService.getAsynLocalTourInfos();
				
				visitService = new FamilyVisitServiceImpl(MainActivity.this);
				final List<HomeVisitModel> visits = visitService.getUnsynLocalVisits();
				
				if(dailyWorkDatas.size()>0){
					AttendenceInfoModel model=dailyWorkDatas.get(0);
					id=model.getAttendceId();
					type=0;
					mMKSearch.reverseGeocode(new GeoPoint((int)(Float.parseFloat(model.getLat())*1e6),(int) (Float.parseFloat(model.getLot())*1e6)));
				}
				if(tours.size()>0){
					TourModel model=tours.get(0);
					id=model.getTourId();
					type=1;
					mMKSearch.reverseGeocode(new GeoPoint((int)(Float.parseFloat(model.getTour_in_lat())*1e6),(int)(Float.parseFloat(model.getTour_in_lot())*1e6)));
				}else if(visits.size()>0){
					HomeVisitModel model=visits.get(0);
					id=model.getFamily_Visit_Id();
					type=2;
					mMKSearch.reverseGeocode(new GeoPoint((int)(Float.parseFloat(model.getVisit_in_Lat())*1e6),(int)(Float.parseFloat(model.getVisit_in_Lon())*1e6)));
				}else{
					isFinished=true;
					synService=new SynchronizationServiceImpl(MainActivity.this);
					synService.synchronizeData();
					
					if (null != proDialog) {
						proDialog.dismiss();
					}
				}}else{
					AppHelper.showExitDialog(MainActivity.this,
							MainActivity.this.getString(R.string.no_network),
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									Intent intent = new Intent();
									intent.setAction(Settings.ACTION_SETTINGS);
									MainActivity.this.startActivity(intent);
								}
							});
				}
				break;
			case HandlerCommand.GOT_LOCATION:
				getAysnLocal();
				break;
			}
		}
		
	};
	
	@Override
	protected void onDestroy() {
		if (mBMapMan != null) {
			
			mBMapMan.stop();	
			
		}
		if(null!=mMKSearch)
		{
			mMKSearch.destory();
		}
		super.onDestroy();
	}

	private void getAysnLocal() {
		if(!isFinished){
		final List<AttendenceInfoModel> dailyWorkDatas;
		daillyWorkService = new DailyWorkServiceImpl(MainActivity.this);
		dailyWorkDatas = daillyWorkService.getUnsynLocalAttendenceInfos();
		
		tourService = new TourInfoServiceImpl(MainActivity.this);
		final List<TourModel> tours = tourService.getAsynLocalTourInfos();
		
		visitService = new FamilyVisitServiceImpl(MainActivity.this);
		final List<HomeVisitModel> visits = visitService.getUnsynLocalVisits();
		if(null==mMKSearch){
			mMKSearch = new MKSearch();  
			mMKSearch.init(mBMapMan, localListener);//注意，MKSearchListener只支持一个，以最后一次设置为准  
		}
		if(dailyWorkDatas.size()>0){
			AttendenceInfoModel model=dailyWorkDatas.get(0);
			id=model.getAttendceId();
			type=0;
			mMKSearch.reverseGeocode(new GeoPoint((int)(Float.parseFloat(model.getLat())*1e6),(int) (Float.parseFloat(model.getLot())*1e6)));
			
		}
		if(dailyWorkDatas.isEmpty()&&tours.size()>0){
			TourModel model=tours.get(0);
			id=model.getTourId();
			type=1;
			mMKSearch.reverseGeocode(new GeoPoint((int)(Float.parseFloat(model.getTour_in_lat())*1e6),(int)(Float.parseFloat(model.getTour_in_lot())*1e6)));
		}
		 if(dailyWorkDatas.isEmpty()&&tours.isEmpty()&&visits.size()>0){
			HomeVisitModel model=visits.get(0);
			id=model.getFamily_Visit_Id();
			type=2;
			mMKSearch.reverseGeocode(new GeoPoint((int)(Float.parseFloat(model.getVisit_in_Lat())*1e6),(int)(Float.parseFloat(model.getVisit_in_Lon())*1e6)));
		}
		
		if(visits.isEmpty()&&dailyWorkDatas.isEmpty()&&tours.isEmpty()){
			isFinished=true;
			synService=new SynchronizationServiceImpl(MainActivity.this);
			synService.synchronizeData();
			
			if (null != proDialog) {
				proDialog.dismiss();
			}
		}
		}
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
			
		}
		super.onResume();

	}
}
