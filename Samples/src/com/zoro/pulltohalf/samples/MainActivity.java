package com.zoro.pulltohalf.samples;

import java.util.ArrayList;
import java.util.List;

import me.zoro.pulltohalf.lib.OnPullToHalfListener;
import me.zoro.pulltohalf.lib.app.PullToHalfActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends PullToHalfActivity {

	ListView mListView;
	DataAdapter mDataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(item.getItemId() == R.id.action_settings){
			Toast.makeText(this, "On Clicked Setting!", Toast.LENGTH_SHORT).show();
		}
		return true;
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.listview);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			list.add("Item " + i);
		}
		mDataAdapter = new DataAdapter(this, list);
		mListView.setAdapter(mDataAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(MainActivity.this,
						"On Item Clicked : " + position, Toast.LENGTH_SHORT)
						.show();
			}
		});

		setPullToHalfEnable(true);

		getPullToHalfLayout().addPullToHalfListener(new OnPullToHalfListener() {

			@Override
			public void onScrollStateChange(int state, float scrollPercent) {

				logWithInfo("onScrollStateChange. State:" + state);
			}

			@Override
			public void onScrollOverThreshold() {

				logWithInfo("onScrollOverThreshold");
			}

			@Override
			public void onPullStart() {

				logWithInfo("on pull start");
			}
		});
	}

	private void logWithInfo(String info) {
		Log.d("OnPullToHalfListener", info);
	}
}
