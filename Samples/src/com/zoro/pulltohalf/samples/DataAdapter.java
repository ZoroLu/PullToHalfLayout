package com.zoro.pulltohalf.samples;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DataAdapter extends BaseAdapter {

	List<String> mList;
	Context mContext;

	public DataAdapter(Context context, List<String> list) {
		mContext = context;
		mList = list;
	}

	@Override
	public int getCount() {

		return mList != null ? mList.size() : 0;
	}

	@Override
	public Object getItem(int position) {

		return mList != null ? mList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item,
					null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.image_item);
			holder.textView = (TextView) convertView
					.findViewById(R.id.textView_item);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		holder.imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Toast.makeText(mContext,
						"Image on position of " + position + " is clicked.",
						Toast.LENGTH_SHORT).show();
			}
		});
		holder.textView.setText(mList.get(position));

		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		TextView textView;
	}

}
