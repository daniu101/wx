package com.yanni.activity;

import com.example.yanni.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TELActivity extends Activity {

	String TEL = "4006908781";

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tel);

		// 返回主页
		toMainActivity();

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

	// 返回主页
	public void toMainActivity() {
		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(TELActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

}
