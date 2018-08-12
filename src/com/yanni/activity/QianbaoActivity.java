package com.yanni.activity;

import com.example.yanni.R;
import com.yanni.common.CommonUri;
import com.yanni.util.PostUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QianbaoActivity extends Activity {

	String response, params;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qianbao);

		SharedPreferences sharedPreferences = getSharedPreferences("yanni", Activity.MODE_PRIVATE);
		String Token = sharedPreferences.getString("Token", "");

		if (Token.isEmpty()) {

			// 跳入登录
			toLoginActivity();

		}

		// 返回主页
		toMainActivity();

		// 开启子线程
		params = "Token=" + Token;

		// 子线程请求
		new Thread() {
			@Override
			public void run() {

				// 返回String
				response = PostUtil.sendPost(CommonUri.BQB_URL, params);
				handler.sendEmptyMessage(0x123);

			}

		}.start();

	}

	// 跳入登录
	public void toLoginActivity() {

		Intent intent = new Intent();
		intent.setClass(QianbaoActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();

	}

	// 返回主页
	public void toMainActivity() {
		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(QianbaoActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {

			if (msg.what == 0x123) {

				response = response.replace(" ", "").trim();

				// 设置金额
				setTXT(response);

			}

		}

	};

	// 设置
	public void setTXT(String response) {

		// 状态
		TextView qianbao = (TextView) findViewById(R.id.qianbao);
		qianbao.setText("金额：" + response + "元");

	}

}
