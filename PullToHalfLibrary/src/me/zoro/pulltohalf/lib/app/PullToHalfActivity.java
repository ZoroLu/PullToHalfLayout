package me.zoro.pulltohalf.lib.app;

import me.zoro.pulltohalf.lib.PullToHalfLayout;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class PullToHalfActivity extends Activity implements PullToHalfActivityBase{
	
	PullToHalfHelper mPullToHalfHelper;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPullToHalfHelper = new PullToHalfHelper(this);
		mPullToHalfHelper.onActivityCreate();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mPullToHalfHelper.onPostCreate();
	}
	
	@Override
	public View findViewById(int id) {
		View v = super.findViewById(id);
		if(v == null && mPullToHalfHelper != null){
			return mPullToHalfHelper.findViewById(id);
		}
		return v;
	}

	//  PullToHalfActivityBase
	
	@Override
	public PullToHalfLayout getPullToHalfLayout() {
		return mPullToHalfHelper.getPullToHalfLayout();
	}

	@Override
	public void setPullToHalfEnable(boolean enable) {
		
		mPullToHalfHelper.getPullToHalfLayout().setPullEnable(enable);	}

}
