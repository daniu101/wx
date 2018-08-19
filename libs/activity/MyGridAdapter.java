package com.yanni.activity;

import com.example.yanni.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGridAdapter extends BaseAdapter {
	private Context mContext;

	public String[] img_text = { "公司简介", "我的钱包", "加盟华屋", "业主二维码", "提交资料", "订单详情", "总部电话", "商户端二维码", "施工案例" };
	public int[] imgs = { R.drawable.jianjie, R.drawable.qianbao, R.drawable.jiameng, R.drawable.erweima,
			R.drawable.tijiao, R.drawable.dingdan, R.drawable.tel, R.drawable.qrshop, R.drawable.anli };

	public MyGridAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return img_text.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
		ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
		iv.setBackgroundResource(imgs[position]);

		tv.setText(img_text[position]);
		return convertView;
	}

}
