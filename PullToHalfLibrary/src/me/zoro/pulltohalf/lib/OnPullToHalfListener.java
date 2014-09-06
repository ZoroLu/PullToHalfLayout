package me.zoro.pulltohalf.lib;

public interface OnPullToHalfListener {

	public void onScrollStateChange(int state, float scrollPercent);

	public void onPullStart();

	public void onScrollOverThreshold();
}
