package com.yanni.activity;

import com.example.yanni.R;
import com.yanni.common.CommonUri;
import com.yanni.util.PostUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	private MyGridView gridview;
	String response, params = "YN=1";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 子线程请求
		new Thread() {
			@Override
			public void run() {

				// 返回String
				response = PostUtil.sendPost(CommonUri.NEWVERSION_URL, params);

				handler.sendEmptyMessage(0x123);

			}

		}.start();

		gridview = (MyGridView) findViewById(R.id.gridview);
		gridview.setAdapter(new MyGridAdapter(this));

		gridview.setOnItemClickListener(clickListener);

	}

	private OnItemClickListener clickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub

			Intent intent = new Intent();

			// arg2 判断跳入的页面
			if (arg2 == 0) {

				intent.setClass(MainActivity.this, AboutUSActivity.class);

			} else if (arg2 == 1) {

				intent.setClass(MainActivity.this, QianbaoActivity.class);

			} else if (arg2 == 2) {

				intent.setClass(MainActivity.this, JiamengActivity.class);

			} else if (arg2 == 3) {

				intent.setClass(MainActivity.this, QRActivity.class);

			} else if (arg2 == 4) {

				intent.setClass(MainActivity.this, TijiaoActivity.class);

			} else if (arg2 == 5) {

				intent.setClass(MainActivity.this, OrderActivity.class);

			} else if (arg2 == 6) {

				intent.setClass(MainActivity.this, TELActivity.class);

			} else if (arg2 == 7) {

				intent.setClass(MainActivity.this, QRShopActivity.class);

			} else if (arg2 == 8) {

				intent.setClass(MainActivity.this, AnliActivity.class);

			}

			startActivity(intent);
			finish();

		}

	};

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {

			if (msg.what == 0x123) {

				response = response.replace(" ", "").trim();

				if (response.equals("1")) {

					dialog();

				}

			}

		}

	};

	// 提醒
	private void dialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this); // 先得到构造器
		builder.setTitle("请扫描商户端二维码更新您的APP，如已更新，请勿理会！"); // 设置标题
		builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() { // 设置确定按钮
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss(); // 关闭dialog
				//
				Toast.makeText(getApplicationContext(), "请扫描商户端二维码更新您的APP，如已更新，请勿理会！", Toast.LENGTH_SHORT).show();
			}
		});

		// 参数都设置完成了，创建并显示出来
		builder.create().show();
	}
}
