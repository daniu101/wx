package com.yanni.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.yanni.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class QRActivity extends Activity {

	private ImageView qr;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qr);

		SharedPreferences sharedPreferences = getSharedPreferences("yanni", Activity.MODE_PRIVATE);
		String Token = sharedPreferences.getString("Token", "");

		if (Token.isEmpty()) {

			// 跳入登录
			toLoginActivity();

		} else {

			// 设置二维码
			final String imageUrl = "http://www.yannidaojia.com/shop/" + Token + "/qr.png";
			qr = (ImageView) super.findViewById(R.id.qr);
			new Thread() {
				@Override
				public void run() {

					Message message = handler.obtainMessage();
					message.obj = getHttpBitmap(imageUrl);
					message.arg1 = 1;
					handler.sendMessage(message);
				};

			}.start();
		}

		// 返回主页
		toMainActivity();

		// 分享
		toShare(Token);

	}

	// 跳入登录
	public void toLoginActivity() {

		Intent intent = new Intent();
		intent.setClass(QRActivity.this, LoginActivity.class);
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
				intent.setClass(QRActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	// 获取网络图片
	private Bitmap getHttpBitmap(String urlString) {
		URL url;
		Bitmap bitmap = null;

		try {
			url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(6000);
			connection.setDoInput(true);
			connection.setUseCaches(true);

			InputStream is = connection.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;

	}

	/** 保存方法 */
	@SuppressLint("SdCardPath")
	private void saveBitmap(Bitmap bitmap) throws IOException {
		File file = new File("/sdcard/DCIM/Camera/yanni.png");
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Toast.makeText(getApplicationContext(), "业主扫描二维码已经保存,请在图片库中查找", Toast.LENGTH_SHORT).show();

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {

			if (msg.arg1 == 1) {
				Bitmap bitmap = (Bitmap) msg.obj;
				qr.setImageBitmap(bitmap);
				try {
					saveBitmap(bitmap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	};

	// 分享
	public void toShare(final String Token) {
		Button btn_share = (Button) findViewById(R.id.btn_share);
		btn_share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				showShare(Token);

			}
		});
	}

	// 分享方法
	private void showShare(String Token) {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("华屋到家只做瓷缝，瓷砖美缝升级产品，华屋瓷缝剂");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://www.yannidaojia.com/shop/" + Token + "/index.html");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("华屋到家只做瓷缝，瓷砖美缝升级产品，华屋瓷缝剂");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://www.yannidaojia.com/shop/" + Token + "/index.html");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("华屋到家只做瓷缝，瓷砖美缝升级产品，华屋瓷缝剂");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://www.yannidaojia.com/shop/" + Token + "/index.html");
		// 分享图标
		oks.setImageUrl("http://www.yannidaojia.com/sharesdk.png");

		// 启动分享GUI
		oks.show(this);
	}

}
