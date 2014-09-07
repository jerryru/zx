package com.cn.android.zhengxun.app.map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.cn.android.zhengxun.app.MyApplication;
import com.cn.android.zhengxun.app.activity.MapActivity;
import com.cn.android.zhengxun.app.map.listener.MySearchListener;
import com.cn.android.zhengxun.app.util.HandlerCommand;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class LocateHelper {

	private Activity context;
	public LocationClient mLocationClient = null;
	private MKSearch mMKSearch = null; 
	private MKPoiInfo poiInfo;
	private String direction,addStr;
	private Handler handler;
	private BDLocation mlocation,mpoiLocation;
	private String key="餐饮";
	
	private MKSearchListener listener=new MySearchListener(context){

		@Override
		public void onGetAddrResult(MKAddrInfo res, int error) {
			addStr=res.strAddr;
		}

		@Override
		public void onGetPoiDetailSearchResult( int type, int error) {
			
		}

		@Override
		public void onGetPoiResult(MKPoiResult res, int type, int error) {
//		　　 错误号可参考MKEvent中的定义
			if(error == MKEvent.ERROR_RESULT_NOT_FOUND){
//				Toast.makeText(context, "抱歉，未找到结果",Toast.LENGTH_LONG).show();
				if("餐饮".equals(key)){
					key="银行";
				}else{
					key="餐饮";
				}
				return ;
		        }
		        else if (error != 0 || res == null) {
		        	if("餐饮".equals(key)){
						key="银行";
					}else{
						key="餐饮";
					}
//		        	Toast.makeText(context, "搜索出错啦..", Toast.LENGTH_LONG).show();
		return;
		}
		// 将poi结果显示到地图上
//		PoiOverlay poiOverlay = new PoiOverlay(MyMapActivity.this, mMapView);
//		poiOverlay.setData(res.getAllPoi());
//		　　mMapView.getOverlays().clear();
//		　　mMapView.getOverlays().add(poiOverlay);
//		　　mMapView.refresh();
		//当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空
	for(MKPoiInfo info : res.getAllPoi() ){
		if ( info.pt != null ){
			setPoiInfo(info);
			if(null!=mlocation){
				if(info.pt.getLatitudeE6()>mlocation.getLatitude()*1e6){
					if(info.pt.getLongitudeE6()>mlocation.getLongitude()*1e6){
						setDirection("西南");}else{
						direction="东南";
					}
				}else{
					if(info.pt.getLongitudeE6()>mlocation.getLongitude()*1e6){
						direction="西北";}else{
							direction="东北";
						}
				}
			}else{
				setDirection("");	
			}
			handler.sendMessage(handler.obtainMessage(HandlerCommand.GOT_POI));
			}
		
		break;
		}
		}

		
	};
	private BMapManager mBMapMan=null;

	public LocateHelper(Activity context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}
	public LocateHelper(BMapManager mBMapMan, Activity context, Handler handler) {
		this.mBMapMan=mBMapMan;
		this.context = context;
		this.handler = handler;
	}

	public static final boolean isOpenGPS(Context context)
	{
		LocationManager locationManager = (LocationManager)context.getApplicationContext().
				getSystemService(Context.LOCATION_SERVICE);
				return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	public void onCreate() {
		mLocationClient = ((MyApplication) context.getApplication()).mLocationClient;
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
//		option.setOpenGps(true);
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(7000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);			
		initSearch();
	}

	private void initSearch() {
		mMKSearch = new MKSearch();  
		mMKSearch.init(mBMapMan, listener);//注意，MKSearchListener只支持一个，以最后一次设置为准  
	}
 
	public void doSearchLocal(GeoPoint point,int position){
		mMKSearch.reverseGeocode(point);
	}
	
	
//	public void startMBmapMan(){
//		if(null!=mBMapMan){
//			mBMapMan.start();
//		}
//	}
//	
//	public void stopMBmapMan(){
//		if(null!=mBMapMan){
//			mBMapMan.stop();
//		}
//	}
//	
//	public void destroyMBmapMan(){
//		if(null!=mBMapMan){
//			mBMapMan.destroy();
//			mBMapMan=null;
//		}
//	}
	
	public void start() {
		if(mLocationClient != null && !mLocationClient.isStarted())
		{
		mLocationClient.start();
		}else{
			Log.d("start the location client", "mLocationClient is null or has been started");
		}
	}
	
	public void stop()
	{
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.stop();
			else 
			 Log.d("stop the location client", "mLocationClient is null or not started");
	}

	public void request() {
		
		if (mLocationClient != null && mLocationClient.isStarted())
			{mLocationClient.requestLocation();
		//     mLocationClient.requestPoi();
		}
			else 
			 Log.d("LocSDK3", "mLocationClient is null or not started");
	}

	public BDLocation getMlocation() {
		return mlocation;
	}

	public void setMlocation(BDLocation mlocation) {
		this.mlocation = mlocation;
	}

	private BDLocationListener myListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
		
//			if (location.getLocType() == BDLocation.TypeGpsLocation) {
//				
//			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//				
//			}
			mlocation = location;
			logMsg();

		}
		

		private void logMsg() {
			doSearch(mlocation);
			int what = HandlerCommand.GOT_LOCATION;
			Message msg=Message.obtain(handler, what);
			handler.sendMessage(msg);
//			stop();
		}

	

		public void doSearch(BDLocation mlocation) {
			doSearch(mlocation,key);
			
		}

		public void doSearch(BDLocation mlocation,String key) {
			GeoPoint mPt=new GeoPoint((int)(mlocation.getLatitude()*1e6),(int)(mlocation.getLongitude()*1e6));
			mMKSearch.poiSearchNearBy(key, mPt, 400);
			
		}


		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			mpoiLocation=poiLocation;
			logPoiMsg();
		}


		private void logPoiMsg() {
			int what = HandlerCommand.GOT_POI;
			Message msg=Message.obtain(handler, what);
			handler.sendMessage(msg);
			
		}
	};

	public BDLocation getMpoiLocation() {
		return mpoiLocation;
	}

	public void setMpoiLocation(BDLocation mpoiLocation) {
		this.mpoiLocation = mpoiLocation;
	}
	public MKPoiInfo getPoiInfo() {
		return poiInfo;
	}
	public void setPoiInfo(MKPoiInfo poiInfo) {
		this.poiInfo = poiInfo;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
}
