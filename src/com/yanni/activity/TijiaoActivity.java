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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TijiaoActivity extends Activity {

	EditText et_Region, et_Store, et_Shop, et_TEL, et_PWD, et_Name, et_Bank, et_WX, et_Alipay, et_Remark;
	String params, response;

	@Override
	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tijiao);

		// 返回主页
		toMainActivity();

		SharedPreferences sharedPreferences = getSharedPreferences("yanni", Activity.MODE_PRIVATE);
		String Token = sharedPreferences.getString("Token", "");

		if (Token.isEmpty()) {

			// 获取所有信息
			et_Region = (EditText) super.findViewById(R.id.et_Region);
			et_Store = (EditText) super.findViewById(R.id.et_Store);
			et_Shop = (EditText) super.findViewById(R.id.et_Shop);
			et_TEL = (EditText) super.findViewById(R.id.et_TEL);
			et_PWD = (EditText) super.findViewById(R.id.et_PWD);
			et_Name = (EditText) super.findViewById(R.id.et_Name);
			et_Bank = (EditText) super.findViewById(R.id.et_Bank);
			et_WX = (EditText) super.findViewById(R.id.et_WX);
			et_Alipay = (EditText) super.findViewById(R.id.et_Alipay);
			et_Remark = (EditText) super.findViewById(R.id.et_Remark);

			// 提交
			Button btn_ok = (Button) super.findViewById(R.id.btn_ok);
			btn_ok.setOnClickListener(new codeOnClickListener());

		} else {

			// 提交成功
			toTijiaoOKActivity();

			Toast.makeText(getApplicationContext(), "您已提交过材料",

					Toast.LENGTH_SHORT).show();
		}

	}

	// 返回报告索引页
	public void toMainActivity() {
		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(TijiaoActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	// 点击进入的类
	public class codeOnClickListener implements OnClickListener {

		@Override
		@SuppressLint("NewApi")
		public void onClick(View v) {
			// TODO 自动生成的方法存根

			// 获取
			String Region = et_Region.getText().toString();
			String Store = et_Store.getText().toString();
			String Shop = et_Shop.getText().toString();
			String TEL = et_TEL.getText().toString();
			String PWD = et_PWD.getText().toString();
			String Name = et_Name.getText().toString();
			String Bank = et_Bank.getText().toString();
			String WX = et_WX.getText().toString();
			String Alipay = et_Alipay.getText().toString();
			String Remark = et_Remark.getText().toString();

			if (Region.isEmpty() || Store.isEmpty() || Shop.isEmpty() || TEL.isEmpty() || Name.isEmpty()) {

				Toast.makeText(getApplicationContext(), "有必填项未填写", Toast.LENGTH_SHORT).show();

			} else {

				params = "Region=" + Region + "&Store=" + Store + "&Shop=" + Shop + "&TEL=" + TEL + "&PWD=" + PWD
						+ "&Name=" + Name + "&Bank=" + Bank + "&WX=" + WX + "&Alipay=" + Alipay + "&Remark=" + Remark;

				// 子线程请求
				new Thread() {
					@Override
					public void run() {

						// 返回String
						response = PostUtil.sendPost(CommonUri.BTJ_URL, params);
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

				if (response.equals("1")) {

					// 提交成功
					toTijiaoOKActivity();

					Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();

				} else if (response.equals("0")) {

					// 提交成功
					toTijiaoOKActivity();

					Toast.makeText(getApplicationContext(), "您已经提交过资料，请登录", Toast.LENGTH_SHORT).show();

				} else {

					Toast.makeText(getApplicationContext(), "提交失败！", Toast.LENGTH_SHORT).show();

				}

			}

		}

	};

	// 提交过材料
	public void toTijiaoOKActivity() {

		Intent intent = new Intent();
		intent.setClass(TijiaoActivity.this, TijiaoOKActivity.class);
		startActivity(intent);
		finish();

	}

}
