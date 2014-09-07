package com.cn.android.zhengxun.app.map.listener;

import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;

public class MySearchListener implements MKSearchListener {
	private Context context;
	public MySearchListener(Context context){
		this.context=context;
	}

	@Override
	public void onGetAddrResult(MKAddrInfo res, int error) {
		if (error != 0) {
	        String str = String.format("错误号：%d", error);
	        Toast.makeText(context, str,1000).show();
	        return;
	    }
	    //地图移动到该点
//	    mMapView.getController().animateTo(res.geoPt);
	    if (res.type == MKAddrInfo.MK_GEOCODE) {
	        //地理编码：通过地址检索坐标点
	        String strInfo = String.format("纬度：%f 经度：%f", res.geoPt.getLatitudeE6()/1e6, res.geoPt.getLongitudeE6()/1e6);
	        Toast.makeText(context, strInfo, 1000).show();
	    }
	    if (res.type == MKAddrInfo.MK_REVERSEGEOCODE) {
	        //反地理编码：通过坐标点检索详细地址及周边poi
	        String strInfo = res.strAddr;
	        Toast.makeText(context, strInfo,1000).show();
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
	public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {
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

}
