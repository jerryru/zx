package com.cn.android.zhengxun.app.adapter;

import java.util.List;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.model.ClerkModel;
import com.cn.android.zhengxun.app.util.HandlerCommand;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ClerkListAdapter extends BaseAdapter {
	private List<ClerkModel> clerks;
	private LayoutInflater inflater;
	private Context context;
	private ClerkModel selectedModel;
	private Handler handler;
	
	public ClerkListAdapter(Context context,Handler handler){
		this.context=context;
		this.handler=handler;
		inflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return clerks.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return clerks.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup vg) {
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
        final ClerkModel clerk=clerks.get(position);
        listItemView.txt.setText(clerk.getName());
        listItemView.img.setImageResource(R.color.transparent);
        convertView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				selectedModel=clerks.get(position);
				Message msg=handler.obtainMessage(HandlerCommand.GOT_CLERK);
				handler.sendMessage(msg);
				
			}
        	
        });
		return convertView;
	}

	public List<ClerkModel> getClerks() {
		return clerks;
	}

	public void setClerks(List<ClerkModel> clerks) {
		this.clerks = clerks;
	}

	public ClerkModel getSelectedModel() {
		return selectedModel;
	}

	public void setSelectedModel(ClerkModel selectedModel) {
		this.selectedModel = selectedModel;
	}

	private final class ListItemView {
		
		private ImageView img;
		private TextView txt;

		
	}
}
