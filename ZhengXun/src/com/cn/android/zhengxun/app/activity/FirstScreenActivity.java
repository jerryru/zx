package com.cn.android.zhengxun.app.activity;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.util.HandlerCommand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class FirstScreenActivity extends Activity {

	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case HandlerCommand.END_WELCOME:
				Intent intent=new Intent();
				intent.setClass(FirstScreenActivity.this, LoginActivity.class);
				FirstScreenActivity.this.startActivity(intent);
				FirstScreenActivity.this.finish();
				break;
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_screen_layout);
		Thread thread=new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					Thread.sleep(3000);
					Message msg=handler.obtainMessage(HandlerCommand.END_WELCOME);
					handler.sendMessage(msg);
				} catch (InterruptedException e) {
					Log.e("exception", "InterruptedException", e);
				}
			}
			
		};
		thread.start();
	}

}
