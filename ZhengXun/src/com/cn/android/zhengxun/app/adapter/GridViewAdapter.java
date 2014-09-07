package com.cn.android.zhengxun.app.adapter;

import java.io.IOException;
import java.util.List;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.activity.DailyMessageActivity;
import com.cn.android.zhengxun.app.activity.DailyWorkSheetActivity;
import com.cn.android.zhengxun.app.activity.HomeVisitActivity;
import com.cn.android.zhengxun.app.activity.TourActivity;
import com.cn.android.zhengxun.app.data.CategoryData;
import com.cn.android.zhengxun.app.model.UserModel;
//import com.cn.android.zhengxun.app.service.SynchronizationService;
//import com.cn.android.zhengxun.app.service.impl.SynchronizationServiceImpl;
import com.cn.android.zhengxun.app.util.AppHelper;
import com.cn.android.zhengxun.app.util.HandlerCommand;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<CategoryData> categoryList;
	private Context context;
	private UserModel user;
//	private SynchronizationService synService;
	private Handler handler;
	
	public GridViewAdapter(Context context,Handler handler) {
		this.context = context;
		this.handler=handler;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return categoryList.size();
	}

	@Override
	public Object getItem(int location) {
		// TODO Auto-generated method stub
		return categoryList.get(location);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup vg) {
		   ListItemView listItemView = null;
	        if (convertView == null) {
	            listItemView = new ListItemView();
	            convertView = inflater.inflate(R.layout.grid_item_layout,null);
	            listItemView.img=(ImageView)convertView.findViewById(R.id.img_pic);
	            listItemView.txt=(TextView)convertView.findViewById(R.id.txt_name);
	            listItemView.layout=(LinearLayout)convertView.findViewById(R.id.gv_item_layout);
	            convertView.setTag(listItemView);
	            }else {
	                listItemView = (ListItemView) convertView.getTag();
	            }
	        listItemView.clear();
	        final CategoryData categoryData = categoryList.get(position);
			try {
	        	String path=categoryData.getPicPath();
	        	if(!"".equals(path)&&null!=path){
				listItemView.img.setImageBitmap(BitmapFactory.decodeStream((context.getAssets().open(path))));
	        	}else{
	        		listItemView.img.setImageResource(R.drawable.img_downloading);
					}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
			}
	        listItemView.txt.setText(categoryData.getName());
	        convertView.setOnClickListener(new OnClickListener(){

	    		@Override
	    		public void onClick(View arg0) {
	    					dealBusiness(categoryData);
	    		}

				private void dealBusiness(final CategoryData categoryData) {
					switch(categoryData.getTypeId())
					{
					case 0:
						dealDailyWork(categoryData);
						break;
					case 1:
						dealSellBusiness(categoryData);
						break;
					case 2:
						dealPersonalIssues(categoryData);
						break;
					case 3:
						break;
					}
				}

				private void dealPersonalIssues(CategoryData categoryData) {
					switch(categoryData.getId()){
					case 0:
					case 1:
					case 2:
						AppHelper.showExitDialog(context, context.getString(R.string.developing),
								new android.content.DialogInterface.OnClickListener()
								{

									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										
									}
								});
						break;
					}
					
				}

				private void dealSellBusiness(CategoryData categoryData) {
					switch(categoryData.getId()){
					case 0:
						Intent intent0=new Intent();
						intent0.setClass(context, TourActivity.class);
						context.startActivity(intent0);
						break;
					case 1:
						Intent intent1=new Intent();
						intent1.setClass(context, HomeVisitActivity.class);
						context.startActivity(intent1);
						break;
					case 2:
					default:
						AppHelper.showExitDialog(context, context.getString(R.string.developing),
								new android.content.DialogInterface.OnClickListener()
								{

									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										
									}
								});
						break;
					}
					
				}

				private void dealDailyWork(CategoryData categoryData) {
					switch(categoryData.getId()){
					case 0:
						Intent intent0=new Intent();
						intent0.setClass(context, DailyMessageActivity.class);
						context.startActivity(intent0);
						break;
					case 1:
						Intent intent1=new Intent();
						intent1.setClass(context, DailyWorkSheetActivity.class);
						context.startActivity(intent1);
						break;
					case 6:						
//						synService=new SynchronizationServiceImpl(context);
//						synService.synchronizeData();
//						AppHelper.showExitDialog(context,
//								context.getString(R.string.not_support_due_to_test),
//								new android.content.DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog, int which) {
//										
//									}
//								});
						Message msg=handler.obtainMessage(HandlerCommand.BEGIN_SUBMIT);
						handler.sendMessage(msg);
						break;
					case 2:
						default:
						AppHelper.showExitDialog(context, context.getString(R.string.developing),
								new android.content.DialogInterface.OnClickListener()
								{

									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										//TODO
									}
								});
						break;
					}
					
				}
	    		
	    	});
	        return convertView;
	}


	public List<CategoryData> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<CategoryData> categoryList) {
		this.categoryList = categoryList;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	private final class ListItemView {
		private LinearLayout layout;
		private ImageView img;
		private TextView txt;

		public void clear() {
			img.setImageResource(R.drawable.img_downloading);
			txt.setText("null");

		}
	}
}
