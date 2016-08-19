package com.easynetwork.weather.view;

import com.easynetwork.weather.tools.Log;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**背景图片淡入淡出，注意是图片，如果是颜色都是硬切，仅有图片切换有动画<br>
 * 通过两个view的背景来交替切换<br>
 * TODO 如果你有好的办法，请替换<br>
 * TODO 这里可能在硬切的时候，导致之前的fade动画没有消失
 * @author WenYF
 *
 */
public class FadeRelativeLayout extends RelativeLayout {
	private final static String TAG = "FadeImageView";
	private Context nContext;
	private View nFirstView;
	private View nSecondView;
	private boolean nIsShowFirst;
	
	private final static int sDefault_color = -2;
	
	private int nNowResId = -1;
	private int nNowColor = sDefault_color;
	
	private AlphaAnimation nFadeInAnim;
	private AlphaAnimation nFadeOutAnim;
	
	public FadeRelativeLayout(Context context) {
		this(context, null);
	}

	public FadeRelativeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FadeRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		nContext = context;
		
		nFirstView = new ImageView(nContext);
		nSecondView = new ImageView(nContext);
		
		RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT
				, LayoutParams.MATCH_PARENT);
		addView(nSecondView, 0, params);
		addView(nFirstView, 1, params);
		nSecondView.setBackgroundColor(Color.WHITE);
		nFirstView.setBackgroundColor(Color.WHITE);
		nNowColor = Color.WHITE;
		
		nSecondView.setVisibility(View.INVISIBLE);
		nFirstView.setVisibility(View.VISIBLE);
		nIsShowFirst = true;
		
		nFadeInAnim = new AlphaAnimation(0.f, 1.0f);
		nFadeOutAnim = new AlphaAnimation(1.f, 0.f);
	} 

	public void setBackgroundColor(int color) {
		if(color == nNowColor){
			return;
		}
		
		Log.w(TAG, "硬切颜色背景 nNowColor = " + nNowColor + "  color = " + color);
		nIsShowFirst = true;
		nFirstView.setBackgroundColor(color);
		
		nNowColor = color;
		nNowResId = -1;
		
		startFadeAnimation(0, false);
	}

	public void setBackgroundResource(int resid, int duration) {
		if(resid == nNowResId){
			return;
		}
		
		boolean enableFade = true;
		if(nNowResId == -1 || nNowColor != sDefault_color){
			Log.w(TAG, "硬切");
			nFirstView.setBackgroundResource(resid);
			nNowResId = resid;
			nNowColor = sDefault_color;
			nIsShowFirst = true;
			return;
		}
		
		Log.w(TAG, "动画");
		if (nIsShowFirst) {
	        nSecondView.setBackgroundResource(resid);
		} else {	
			nFirstView.setBackgroundResource(resid);
		}
		
		nNowResId = resid;
		nNowColor = sDefault_color;
		nIsShowFirst = !nIsShowFirst;
		
		startFadeAnimation(duration, enableFade);
	}
	
	private void startFadeAnimation(int duration, boolean enableFade){
		nFadeInAnim.cancel();
		nFadeOutAnim.cancel();
		nFirstView.clearAnimation();
		nSecondView.clearAnimation();
		if(nIsShowFirst){
			nFirstView.setVisibility(View.VISIBLE);
			nSecondView.setVisibility(View.GONE);
		}else{
			nFirstView.setVisibility(View.GONE);
			nSecondView.setVisibility(View.VISIBLE);
		}
		
		if(!enableFade){
			return;
		}
		
		nFadeInAnim.setDuration(duration);
		nFadeOutAnim.setDuration(duration);
		
		if(nIsShowFirst){
			nFirstView.startAnimation(nFadeInAnim);
			nSecondView.startAnimation(nFadeOutAnim);
		}else{
			nSecondView.startAnimation(nFadeInAnim);
			nFirstView.startAnimation(nFadeOutAnim);
		}
	}
}
