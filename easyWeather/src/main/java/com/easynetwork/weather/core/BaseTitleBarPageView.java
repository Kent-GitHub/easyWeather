package com.easynetwork.weather.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class BaseTitleBarPageView extends FrameLayout{
	private ViewGroup mContentView;
	private int mStatusBarHeight = 50;
	private boolean isFullScreen;
	private int mWidth, mHeight;
	private int mShadowHeight = 6;
	private View titleBarView;
	
	public BaseTitleBarPageView(Context context) {
		super(context);
	}

	public BaseTitleBarPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		mWidth = wm.getDefaultDisplay().getWidth();
		mHeight = wm.getDefaultDisplay().getHeight();
		int height = mHeight;
		if(!isFullScreen){
			height -= mStatusBarHeight;
		}
		setMeasuredDimension(mWidth, height);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		//super.onLayout(changed, left, top, right, bottom);
		int t = 0;
		int b = t;
		int l = left;
		int r = right;
		// 重新layout标题栏	
		if (titleBarView != null) {
			b += titleBarView.getMeasuredHeight();
			titleBarView.layout(l, t, r, b);
		}

		//重新layout content，如果需要显示在标题栏阴影的下面， t减去阴影的高度就行了。
		if (mContentView != null) {
			t = b - mShadowHeight;
			b = mHeight;
			mContentView.getLayoutParams().height = b - t;
			if(!isFullScreen){
				mContentView.getLayoutParams().height -= mStatusBarHeight;
			}
			mContentView.layout(l, t, r, b);
		}
	}
	
	public void setTitleBarView(View titleBarView){
		this.titleBarView = titleBarView;
	}
	
	public void setFullScreen(boolean isFullScreen){
		this.isFullScreen = isFullScreen;
	}
	
	public void setShadowHeight(int shadowHeight){
		this.mShadowHeight = shadowHeight;
	}
	
	public void setStatusBarHeight(int statusBarHeight){
		this.mStatusBarHeight = statusBarHeight;
	}
	
	public void addContentView(View contentView) {
		mContentView = (ViewGroup) contentView;
		addView(contentView, getChildCount() - 1);
	}
}
