package com.yanni.activity;

import com.example.yanni.R;
import com.yanni.common.CommonUri;
import com.yanni.util.PostUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	String response, params, TEL = "4006908781";
	private EditText et_phone, et_pwd;
	private Button btn_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// 找到输入框
		et_phone = (EditText) super.findViewById(R.id.et_phone);
		et_pwd = (EditText) super.findViewById(R.id.et_pwd);

		// 点击进入
		btn_login = (Button) super.findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new codeOnClickListener());

		// 拨打总部招商电话
		Button btn_tel = (Button) super.findViewById(R.id.btn_tel);
		btn_tel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + TEL));
				startActivity(intent);
			}
		});

	}

	// 点击进入的类
	public class codeOnClickListener implements OnClickListener {

		@Override
		@SuppressLint("NewApi")
		public void onClick(View v) {
			// TODO 自动生成的方法存根

			// 获取密令
			String Phone = et_phone.getText().toString();
			String PWD = et_pwd.getText().toString();

			if (Phone.isEmpty() || PWD.isEmpty()) {

				Toast.makeText(getApplicationContext(), "手机号码 或 密码 为空", Toast.LENGTH_SHORT).show();

			} else {

				params = "Phone=" + Phone + "&PWD=" + PWD;

				// 子线程请求
				new Thread() {
					@Override
					public void run() {

						// 返回String
						response = PostUtil.sendPost(CommonUri.LOGIN_URL, params);
						handler.sendEmptyMessage(0x123);

					}

				}.start();

			}

		}

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {

			if (msg.what == 0x123) {

				response = response.replace(" ", "").trim();

				if (response.equals("0")) {

					Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();

				} else {

					SharedPreferences sharedPreferences = getSharedPreferences("yanni", Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("Token", response);
					editor.commit();

					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					finish();

					Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();

				}

			}

		}

	};

}
