package com.cn.android.zhengxun.app.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.constants.MessageConstants;
import com.cn.android.zhengxun.app.constants.StringConstants;
import com.cn.android.zhengxun.app.constants.TypeIds;
import com.cn.android.zhengxun.app.util.ImageUtil;
import com.cn.android.zhengxun.app.util.LocalUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class PhotoActivity extends Activity{

	
	private ImageView img_photo;
	private Bitmap returnBitmap;
	private Button btn_ok,btn_cancel;
	private int typeId;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_layout);
		Intent intent=getIntent();
		typeId=intent.getIntExtra(StringConstants.TYPE, 0);
		initViews();
		initListeners();
		Intent intentp=new Intent("android.media.action.IMAGE_CAPTURE");
		this.startActivityForResult(intentp, MessageConstants.REQUEST_CODE_CAMERA);
	}

	private void initListeners() {
		btn_ok.setOnClickListener(listener);
		btn_cancel.setOnClickListener(listener);
		
	}

	private void initViews() {
		img_photo=(ImageView)findViewById(R.id.img_photo);
		btn_ok=(Button)findViewById(R.id.btn_phot_ok);
		btn_cancel=(Button)findViewById(R.id.btn_photo_cancel);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){

		    Bundle extras = data.getExtras();
		    
		    if(extras!=null){

		       Bitmap bitmap = (Bitmap) extras.get(StringConstants.CODE_PHOTO_BITMAP_DATA);
		       Bitmap water_img=ImageUtil.watermarkBitmap(bitmap, null,getStringForImage());
		       img_photo.setImageBitmap(water_img);
		       returnBitmap=water_img;
		       }
	}else if(resultCode==RESULT_CANCELED){
		
	    	   PhotoActivity.this.finish();
	      
	}
	}
	
	private String getStringForImage(){
		String tel=LocalUtil.getLoclPhonumber(this);
		Calendar c=Calendar.getInstance();
		Date time=c.getTime(); 
		DateFormat format=new SimpleDateFormat("yyMMddHHmmss");
		
		String date=format.format(time);
		if(null==tel||tel.length()<4){
			tel="00000000";
		}
		String result=tel.substring(tel.length()-4, tel.length())+date;
		
		return result;
	}
	
	private OnClickListener listener=new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.btn_phot_ok:
				switch(typeId){
				case TypeIds.TOUR_DOOR:
				Intent intent=new Intent(PhotoActivity.this,TourActivity.class);
				intent.putExtra(StringConstants.TOUR_DOOR_PHOTO_OK, returnBitmap);
				setResult(RESULT_OK, intent);
				PhotoActivity.this.finish();
				break;
				 
				case TypeIds.TOUR_DISPALY:
					Intent intent_d=new Intent(PhotoActivity.this,TourActivity.class);
					intent_d.putExtra(StringConstants.TOUR_DISPLAY_PHOTO_OK, returnBitmap);
					setResult(RESULT_OK, intent_d);
					PhotoActivity.this.finish();
					break;
				case TypeIds.VISIT_DOOR:
					Intent intent_vd=new Intent(PhotoActivity.this,TourActivity.class);
					intent_vd.putExtra(StringConstants.VISIT_DOOR_PHOTO_OK, returnBitmap);
					setResult(RESULT_OK, intent_vd);
					PhotoActivity.this.finish();
					break;
					 
					case TypeIds.VISIT_GIFT:
						Intent intent_vg=new Intent(PhotoActivity.this,TourActivity.class);
						intent_vg.putExtra(StringConstants.VISIT_GIFT_PHOTO_OK, returnBitmap);
						setResult(RESULT_OK, intent_vg);
						PhotoActivity.this.finish();
						break;
					case TypeIds.ABNORMAL_POSTIONING:
						Intent intent_ap=new Intent(PhotoActivity.this,AbnormalRemarkActivity.class);
						intent_ap.putExtra(StringConstants.ABNORMAL_POSITIONING_PHOTO, returnBitmap);
						setResult(RESULT_OK, intent_ap);
						PhotoActivity.this.finish();
						break;
				}
				break;
			case R.id.btn_photo_cancel:
				PhotoActivity.this.finish();
				break;
			}
			
		}
		
	};
}
