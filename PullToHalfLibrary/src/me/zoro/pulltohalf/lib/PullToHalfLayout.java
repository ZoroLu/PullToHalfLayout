package me.zoro.pulltohalf.lib;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class PullToHalfLayout extends FrameLayout {

	private static final float DEFAULT_SCROLL_THRESHOLD = 0.3f;
	private static final float DEFAULT_TOUCH_SIZE = 0.27f;
	private static final float DEFAULT_TOUCH_PRESSURE = 0.21f;
	private static final float DEFAULT_PULL_DOWN_PERCENT = 0.4f;
	private static final int MIN_FLING_VELOCITY = 400;
	private static final int SCROLL_BACK_DELAY = 500;

	private boolean mPullEnable = true;
	private View mContentView;
	private boolean mInLayout = false;
	private ViewDragHelper mViewDragHelper;
	private HeheCallBack mHeheCallBack;
	private int mContentTop = 0;
	private float mTouchSize = DEFAULT_TOUCH_SIZE;
	private float mTouchPressure = DEFAULT_TOUCH_PRESSURE;
	private float mScrollPercent = 0;
	private float mScrollThreshold = DEFAULT_SCROLL_THRESHOLD;
	private ArrayList<OnPullToHalfListener> mPullListeners;
	boolean isPullStart = false;
	boolean isHalfStop = false;

	float mPullDownPercent = DEFAULT_PULL_DOWN_PERCENT;
	Handler mHandler = new Handler();

	public PullToHalfLayout(Context context) {
		this(context, null);
	}

	public PullToHalfLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PullToHalfLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	// set
	private void setContentView(View contentView) {
		mContentView = contentView;
	}

	public void setScrollThreshold(float scrollThreshold) {
		if (scrollThreshold > 1.0f || scrollThreshold < 0.0f) {
			mScrollThreshold = DEFAULT_SCROLL_THRESHOLD;
			return;
		}
		mScrollThreshold = scrollThreshold;
	}

	//TODO  diffrent dip,diffent size
	public void setTouchSize(float touchSize) {
		if (touchSize > 1.0 || touchSize < 0) {
			mTouchSize = DEFAULT_TOUCH_SIZE;
			return;
		}
		mTouchSize = touchSize;
	}
	
	public void setTouchPressure(float touchPressure){
		if(touchPressure > 1.0f || touchPressure < 0){
			mTouchPressure = DEFAULT_TOUCH_PRESSURE;
			return;
		}
		mTouchPressure = touchPressure;
	}

	public void setPullEnable(boolean enable) {
		mPullEnable = enable;
	}

	// get
	public boolean getEnable() {
		return mPullEnable;
	}

	public void addPullToHalfListener(OnPullToHalfListener listener) {
		if (mPullListeners == null) {
			mPullListeners = new ArrayList<OnPullToHalfListener>();
		}
		mPullListeners.add(listener);
	}

	private void init() {

		// TODO add support setting by attri 

		mHeheCallBack = new HeheCallBack();
		mViewDragHelper = ViewDragHelper.create(this, mHeheCallBack);

		final float density = getResources().getDisplayMetrics().density;
		final float minVel = density * MIN_FLING_VELOCITY;
		mViewDragHelper.setMinVelocity(minVel);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		mInLayout = true;
		if (mContentView != null) {
			mContentView.layout(0, mContentTop,
					mContentView.getMeasuredWidth(),
					mContentTop + mContentView.getMeasuredHeight());
			mInLayout = false;
		}
	}

	@Override
	public void requestLayout() {
		if (!mInLayout) {
			super.requestLayout();
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		if (mPullEnable && ev.getAction() == MotionEvent.ACTION_DOWN
				&& (ev.getSize() > mTouchSize || ev.getPressure() > mTouchPressure)) {
			isPullStart = true;
			if (mPullListeners != null) {
				for (OnPullToHalfListener listener : mPullListeners) {
					listener.onPullStart();
				}
			}
			return mViewDragHelper.shouldInterceptTouchEvent(ev);
		} else if (mPullEnable && ev.getAction() == MotionEvent.ACTION_UP
				&& isPullStart) {
			isPullStart = false;
			return mViewDragHelper.shouldInterceptTouchEvent(ev);
		} else if (isPullStart) {
			return mViewDragHelper.shouldInterceptTouchEvent(ev);
		}
		isPullStart = false;
		if (isHalfStop) {
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					back();

				}
			}, SCROLL_BACK_DELAY);
		}
		return false;
	}

	private void back() {
		mContentTop = 0;
		mViewDragHelper.smoothSlideViewTo(mContentView, 0, mContentTop);
		isHalfStop = false;
		postInvalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!mPullEnable || !isPullStart) {
			return false;
		}
		mViewDragHelper.processTouchEvent(event);
		return true;
	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		return super.drawChild(canvas, child, drawingTime);
	}

	public void attachToActivity(Activity activity) {
		TypedArray a = activity.getTheme().obtainStyledAttributes(
				new int[] { android.R.attr.windowBackground });
		int background = a.getResourceId(0, 0);
		a.recycle();

		ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
		ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
		decorChild.setBackgroundResource(background);
		decor.removeView(decorChild);
		decor.setBackgroundResource(background);
		addView(decorChild);
		setContentView(decorChild);
		decor.addView(this);
	}

	@Override
	public void computeScroll() {
		if (mViewDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	/**
	 * 
	 * @author luguanquan
	 * 
	 */
	class HeheCallBack extends ViewDragHelper.Callback {

		private boolean mIsScrollerOverValid = false;

		@Override
		public boolean tryCaptureView(View child, int pointerId) {

			return true;
		}

		@Override
		public int getViewHorizontalDragRange(View child) {
			return super.getViewHorizontalDragRange(child);
		}

		@Override
		public int getViewVerticalDragRange(View child) {
			return ViewDragHelper.EDGE_TOP | ViewDragHelper.EDGE_BOTTOM;
		}

		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			return super.clampViewPositionHorizontal(child, left, dx);
		}

		@Override
		public int clampViewPositionVertical(View child, int top, int dy) {

			int ret = Math.max(0, Math.min(top, child.getHeight()));
			return ret;
		}

		@Override
		public void onViewDragStateChanged(int state) {
			super.onViewDragStateChanged(state);
			if (state == ViewDragHelper.STATE_IDLE) {
				isPullStart = false;
			}
			if (mPullListeners != null && !mPullListeners.isEmpty()) {
				for (OnPullToHalfListener listener : mPullListeners) {
					listener.onScrollStateChange(state, mScrollPercent);
				}
			}
		}

		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			super.onViewPositionChanged(changedView, left, top, dx, dy);

			mScrollPercent = Math.abs((top * 1.0f) / changedView.getHeight());
			mContentTop = top;
			invalidate();
			if (!mIsScrollerOverValid && mScrollPercent < mScrollThreshold) {
				mIsScrollerOverValid = true;
			}
			if (mPullListeners != null
					&& !mPullListeners.isEmpty()
					&& mIsScrollerOverValid
					&& mViewDragHelper.getViewDragState() == ViewDragHelper.STATE_DRAGGING) {
				mIsScrollerOverValid = false;
				for (OnPullToHalfListener listener : mPullListeners) {
					listener.onScrollOverThreshold();
				}
			}
		}

		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			final int childHeight = releasedChild.getHeight();
			// top 可以根据情况按比例滑倒一定程度
			int top = yvel > 0 || yvel == 0
					&& mScrollPercent > mScrollThreshold ? (int) (childHeight * mPullDownPercent)
					: 0;
			mContentTop = top;
			mViewDragHelper.settleCapturedViewAt(0, top);
			if (top > 0) {
				isHalfStop = true;
			} else {
				isHalfStop = false;
			}
			invalidate();
		}

	}

}