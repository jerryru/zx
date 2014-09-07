package com.cn.android.zhengxun.app.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cn.android.zhengxun.app.MainActivity;
import com.cn.android.zhengxun.app.MyApplication;
import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.constants.StringConstants;
import com.cn.android.zhengxun.app.data.UserData;
import com.cn.android.zhengxun.app.model.PrincipleModel;
import com.cn.android.zhengxun.app.model.UserModel;
import com.cn.android.zhengxun.app.service.LoginService;
import com.cn.android.zhengxun.app.service.PrincipleService;
import com.cn.android.zhengxun.app.service.UserService;
import com.cn.android.zhengxun.app.service.impl.LoginResults;
import com.cn.android.zhengxun.app.service.impl.LoginServiceImpl;
import com.cn.android.zhengxun.app.service.impl.PrincipleServiceImpl;
import com.cn.android.zhengxun.app.service.impl.UserServiceImpl;
import com.cn.android.zhengxun.app.util.HandlerCommand;
import com.cn.android.zhengxun.app.util.StreamTool;
import com.cn.android.zhengxun.app.util.StringUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	@Override
	protected void onDestroy() {
		if(null!=((MyApplication)this.getApplication()).mBMapMan){
		((MyApplication)this.getApplication()).mBMapMan.destroy();
		((MyApplication)this.getApplication()).mBMapMan=null;
		}
		super.onDestroy();
	}

	private Button btn_login;

	private EditText userName, password;
	private CheckBox cb_remember_name, cb_remember_psw;
//	private ImageView img_first_screen;
	private UserModel user = new UserModel();
	private UserService userService;
	private UserModel userModel = null;
	private LoginService login;
	private ProgressDialog proDialog;
	private UserData userData;
	private PrincipleService principleService;
	private PrincipleModel pricipleModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initView();
		getBackupData();
	}

	private void getBackupData() {
		String filePath = StringUtil.USER_TOKEN_PATH + File.separator
				+ this.getPackageName() + File.separator
				+ StringUtil.USER_TOKEN_FILE;

		File file = new File(filePath);
		if (file.exists()) {
			List<String> result = new ArrayList<String>();
			try {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String str = null;

				while ((str = br.readLine()) != null) {
					result.add(str);

					System.out.println(str);
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (result.size() > 1) {
				user.setUserName(result.get(0));
				user.setPassword(result.get(1));
			} else if (result.size() > 0) {
				user.setUserName(result.get(0));
			}
			userName.setText(user.getUserName());
			cb_remember_name.setChecked(true);
			if (null != user.getPassword() && !"".equals(user.getPassword())) {
				password.setText(user.getPassword());
				cb_remember_psw.setChecked(true);
			}

		}
	}

	private void initView() {
		btn_login = (Button) findViewById(R.id.btn_login_user_name);
		userName = (EditText) findViewById(R.id.edit_phone_no1);
		password = (EditText) findViewById(R.id.edit_password1);
		cb_remember_name = (CheckBox) findViewById(R.id.cb_remember_username);
		cb_remember_psw = (CheckBox) findViewById(R.id.cb_remember_password);
		//img_first_screen=(ImageView)findViewById(R.id.img_first_screen);
		//img_first_screen.setVisibility(View.VISIBLE);		
		proDialog = new ProgressDialog(this);
		proDialog.setMessage(getString(R.string.loading));
		btn_login.setOnClickListener(listerner);

		userData = UserData.getInstance();
	}

	private OnClickListener listerner = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String filePath = StringUtil.USER_TOKEN_PATH + File.separator
					+ LoginActivity.this.getPackageName() + File.separator
					+ StringUtil.USER_TOKEN_FILE;
			StringBuilder sb = new StringBuilder();
			File file = deleteOnExist(filePath);
			if (cb_remember_psw.isChecked()) {
				sb.append(userName.getText().toString()
						+ System.getProperty("line.separator"));
				sb.append(password.getText().toString());

				saveUserData(sb, file, filePath);

			} else {
				if (cb_remember_name.isChecked()) {
					sb.append(userName.getText().toString());

					saveUserData(sb, file, filePath);

				}
			}
			user.setUserName(userName.getText().toString());
			user.setPassword(password.getText().toString());
			// 保存用户的session
			UserData.getInstance().setUser_id(user.getUserName());
			login();

		}

	};

	private void login() {
		userService = new UserServiceImpl(this);
		int isLogin = userService.login(user);
		if (isLogin==LoginResults.SUCESSFUL) {
			loginSuccess();
		} else if(isLogin==LoginResults.WRONG_PSW) {
			Toast.makeText(this, getString(R.string.wrong_psw), 1000).show();
		} if(isLogin==LoginResults.NO_SUCH_USER){
			if (null != proDialog) {
				proDialog.show();
			}
			Thread t = new Thread() {

				@Override
				public void run() {
					login = new LoginServiceImpl(LoginActivity.this, handler);
					principleService = new PrincipleServiceImpl(
							LoginActivity.this, handler);
					login.login(user);
					pricipleModel=principleService.getPrincipleByUser(user);
				}

			};
			t.start();

		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 调用webservice登录成功
			case HandlerCommand.LOGIN_SUCCESSFUL:
				userModel = login.getReturnUser();
				if (null != userModel) {
					userModel.setPassword(user.getPassword());
					user=userModel;
					boolean bool = userService.insertUser(userModel);

					if(!proDialog.isShowing()){
						proDialog.show();}
					proDialog.setMessage(getString(R.string.initlizing));
				} else {
					loginFailed();
				}
				break;
			case HandlerCommand.LOGIN_IOEXCEPTION:
				if (null != proDialog) {
					proDialog.dismiss();
				}
				Toast.makeText(LoginActivity.this,
						getString(R.string.io_exception), 1000).show();
				break;
			case HandlerCommand.LOGIN_XML_EXCEPTION:
			case HandlerCommand.LOGIN_FAILDED:
				loginFailed();
				break;
			case HandlerCommand.GOT_USER_INFO:
				
				
				pricipleModel=principleService.getPrinciple();
				principleService.insertToDB(pricipleModel);

				break;
			case HandlerCommand.INITAILIZED:
				
				loginSuccess();
				if(null!=proDialog){
					proDialog.dismiss();
				}
				break;
			}

		}

	};

	// 登陆成功
	private void loginSuccess() {
		UserData.getInstance().setSellerCode(user.getSellerCode());
		UserData.getInstance().setUser_id(user.getUserName());
		saveUserInfo();
		Intent intent = new Intent();
		intent.putExtra(UserModel.USER_NAME, user.getUserName());
		intent.putExtra(UserModel.USER_PSW,user.getPassword());
		if(null!=user.getSellerCode()&&"".equals(user.getSellerCode())){
		intent.putExtra(UserModel.SELLER_CODE, user.getSellerCode());
		}
		intent.setClass(LoginActivity.this, MainActivity.class);
		LoginActivity.this.startActivity(intent);
	}

	private void saveUserInfo() {
		String filePath = StringUtil.USER_TOKEN_PATH + File.separator
				+ LoginActivity.this.getPackageName() + File.separator
				+ StringUtil.USER_TMP_FILE;
		StringBuilder sb = new StringBuilder();
		File file = deleteOnExist(filePath);
		sb.append(user.getUserName()+System.getProperty("line.separator"));
		sb.append(user.getSellerCode());
		saveUserData(sb,file,filePath);
	}

	private void loginFailed() {
		if (null != proDialog) {
			proDialog.dismiss();
		}
		Toast.makeText(this, StringConstants.LOGIN_FAILED, 1000).show();

	}

	private void saveUserData(StringBuilder sb, File file, String filePath) {

		try {
			file.getParentFile().mkdirs();
			file.createNewFile();

			StreamTool.saveToSdCard(filePath, sb.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private File deleteOnExist(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		return file;
	}
}
