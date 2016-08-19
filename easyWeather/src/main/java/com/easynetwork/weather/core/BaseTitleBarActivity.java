package com.easynetwork.weather.core;

import com.easynetwork.weather.R;
import com.easynetwork.weather.view.FadeRelativeLayout;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseTitleBarActivity extends FragmentActivity {
	private boolean isFullScreen = false;
	protected BaseTitleBarPageView mBaseTitleBarPageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setFullScreen(true);
		setNoTitleBar();
		//replaceTitleBarLayout();
	}
	
	/** 隐藏标题栏*/
	protected void setNoTitleBar() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
    /** 
     * 设置全屏 与否
     */  
    protected void setFullScreen(boolean full) {
    	int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
    	getWindow().setFlags(flag,flag);  
    	if(full){
    		getWindow().setFlags(flag,flag);  
            isFullScreen = true;
    	}else{
    		getWindow().clearFlags(flag);
	    	isFullScreen = false;
    	}
        
    }  
  
	protected boolean isFullScreen(){
		return isFullScreen;
	}
	
	protected void replaceTitleBarLayout(View rootView){
		//FadeRelativeLayout nRootView = (FadeRelativeLayout) findViewById(R.id.weather_container_view);
		FadeRelativeLayout mRootView = (FadeRelativeLayout) rootView;
		View contentView = mRootView.findViewById(R.id.weather_content_view);
		ViewGroup parent = (ViewGroup) mRootView;
		mBaseTitleBarPageView = (BaseTitleBarPageView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_title_bar_page, null);
		mBaseTitleBarPageView.setFullScreen(isFullScreen());
		mBaseTitleBarPageView.setTitleBarView(mBaseTitleBarPageView.findViewById(R.id.weather_action_bar));
		mBaseTitleBarPageView.setStatusBarHeight(50);
		mBaseTitleBarPageView.setShadowHeight(6);
		parent.removeView(contentView);
		mBaseTitleBarPageView.addContentView(contentView);
		parent.addView(mBaseTitleBarPageView);
	}
}
