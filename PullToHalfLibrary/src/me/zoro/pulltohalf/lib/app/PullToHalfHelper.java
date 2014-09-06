package me.zoro.pulltohalf.lib.app;

import me.zoro.pulldownlibrary.R;
import me.zoro.pulltohalf.lib.PullToHalfLayout;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

public class PullToHalfHelper {

	private Activity mActivity;
	private PullToHalfLayout mPullToHalfLayout;

	public PullToHalfHelper(Activity activity) {
		mActivity = activity;
	}

	public void onActivityCreate() {
		mPullToHalfLayout = (PullToHalfLayout) LayoutInflater.from(mActivity)
				.inflate(R.layout.pull_to_half_layout, null);
	}

	public void onPostCreate() {
		mPullToHalfLayout.attachToActivity(mActivity);
	}

	public View findViewById(int id) {
		if (mPullToHalfLayout != null) {
			return mPullToHalfLayout.findViewById(id);
		}
		return null;
	}

	public PullToHalfLayout getPullToHalfLayout() {
		return mPullToHalfLayout;
	}
}
