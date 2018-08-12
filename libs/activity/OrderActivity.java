package com.yanni.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.yanni.R;
import com.yanni.common.CommonUri;
import com.yanni.common.GetState;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class OrderActivity extends Activity {

	private ListView lv_order;
	private ProgressDialog dialog;
	private MyAdapter adapter;;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order);

		SharedPreferences sharedPreferences = getSharedPreferences("yanni", Activity.MODE_PRIVATE);
		String Token = sharedPreferences.getString("Token", "");

		if (Token.isEmpty()) {

			// 跳入登录
			toLoginActivity();

		}

		// 返回主页
		toMainActivity();

		lv_order = (ListView) findViewById(R.id.lv_order);

		dialog = new ProgressDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("正在更新，请稍后...");
		adapter = new MyAdapter(this);

		// 新订单 State=1
		String url = CommonUri.ORDER_URL + "?Token=" + Token + "";
		new MyTask().execute(url);

	}

	// 跳入登录
	public void toLoginActivity() {

		Intent intent = new Intent();
		intent.setClass(OrderActivity.this, LoginActivity.class);
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
				intent.setClass(OrderActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	public class MyTask extends AsyncTask<String, Void, List<Map<String, Object>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// 显示对话框
			dialog.show();
		}

		@Override
		protected List<Map<String, Object>> doInBackground(String... params) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			try {
				// 获取网络JSON格式数据
				@SuppressWarnings("resource")
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(params[0]);
				HttpResponse httpResponse = httpClient.execute(httpPost);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String jsonString = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
					// 解析Json格式数据，并使用一个List<Map>存放
					JSONArray jsonArray = new JSONArray(jsonString);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("Quyu", jsonObject.get("Quyu"));
						map.put("Address", jsonObject.get("Address"));
						map.put("Color", jsonObject.get("Color"));
						map.put("State", jsonObject.get("State"));
						map.put("Commission", jsonObject.get("Commission"));
						map.put("TEL", jsonObject.get("TEL"));
						map.put("Time_AP", jsonObject.get("Time_AP"));

						list.add(map);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> result) {
			super.onPostExecute(result);
			// 把查询到的数据传递给适配器
			adapter.setData(result);
			// 为ListView设定适配器
			lv_order.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			// 隐藏对话框
			dialog.dismiss();

		}
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater layoutInflater;
		private List<Map<String, Object>> list = null;

		public MyAdapter(Context context) {
			layoutInflater = LayoutInflater.from(context);
		}

		public void setData(List<Map<String, Object>> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		@SuppressLint("InflateParams")
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				view = layoutInflater.inflate(R.layout.order_item, null);
			} else {
				view = convertView;
			}

			// 地址
			TextView Address = (TextView) view.findViewById(R.id.Address);
			Address.setText("施工地址：" + list.get(position).get("Quyu").toString() + " - "
					+ list.get(position).get("Address").toString());

			// 颜色
			TextView Color = (TextView) view.findViewById(R.id.Color);
			Color.setText("确认颜色：" + list.get(position).get("Color").toString());

			// 状态
			TextView State = (TextView) view.findViewById(R.id.state);
			State.setText("现在状态：" + GetState.getState(list.get(position).get("State").toString()));

			// 佣金
			TextView Commission = (TextView) view.findViewById(R.id.Commission);
			Commission.setText("订单佣金：" + list.get(position).get("Commission").toString());

			// 业主电话
			TextView Name = (TextView) view.findViewById(R.id.TEL);
			Name.setText("业主电话：" + list.get(position).get("TEL").toString());

			// 预约时间
			TextView TimeAP = (TextView) view.findViewById(R.id.TimeAP);
			TimeAP.setText("预约时间：" + list.get(position).get("Time_AP").toString());

			return view;
		}

	}

}
