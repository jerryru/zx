package com.cn.android.zhengxun.app.adapter;

import java.util.List;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.activity.DailyMessageDetailActivity;
import com.cn.android.zhengxun.app.data.SendInfoData;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SendInfoAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<SendInfoData> list;
	private Context context;
	
	public SendInfoAdapter(Context context)
	{
		this.context=context;
		inflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int location) {
		// TODO Auto-generated method stub
		return list.get(location);
	}

	@Override
	public long getItemId(int location) {
		// TODO Auto-generated method stub
		return location;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();
            convertView = inflater.inflate(R.layout.send_info_item_layout,null);
            listItemView.img=(ImageView)convertView.findViewById(R.id.img_arrow);
            listItemView.txt=(TextView)convertView.findViewById(R.id.txt_send_info_title);
            convertView.setTag(listItemView);
            }else {
                listItemView = (ListItemView) convertView.getTag();
            }
        final SendInfoData sendInfo=list.get(position);
        listItemView.txt.setText(position+1+"."+sendInfo.getTitle());
        convertView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.putExtra("sendID",sendInfo.getId());
				intent.setClass(context, DailyMessageDetailActivity.class);
				context.startActivity(intent);
			}
        	
        });
		return convertView;
	}

	public List<SendInfoData> getList() {
		return list;
	}

	public void setList(List<SendInfoData> list) {
		this.list = list;
	}
	
	private final class ListItemView {
	
		private ImageView img;
		private TextView txt;

		
	}
}
