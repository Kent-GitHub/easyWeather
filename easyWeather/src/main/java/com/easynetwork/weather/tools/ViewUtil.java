package com.easynetwork.weather.tools;

import com.easynetwork.weather.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**数据下载或者网络没有打开的提示view设置<br>
 * ViewGroup为容器，可以是LinearLayout和SrcollView
 * @author WenYF
 *
 */
public class ViewUtil {
	public static void setLoadingView(Context context, ViewGroup parent){
		parent.removeAllViews();
		// 画圈圈
		LinearLayout rootView = new LinearLayout(context);
		rootView.setOrientation(LinearLayout.VERTICAL);
		rootView.setGravity(Gravity.CENTER_HORIZONTAL);
		int padding = ScreenUtil.dip2px(context, 180f);
		rootView.setPadding(0, padding, 0, 0);
		
		ProgressBar bar = new ProgressBar(context);
		bar.setIndeterminate(false);
		bar.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.shape_center_load_progress));
		rootView.addView(bar, new LayoutParams(ScreenUtil.dip2px(context, 36f), ScreenUtil.dip2px(context, 36f)));
		
		TextView tipView = new TextView(context);
		tipView.setTextSize(23.f);
		tipView.setTextColor(context.getResources().getColor(R.color.black_4d4d4d));
		tipView.setGravity(Gravity.CENTER);
		tipView.setLineSpacing(0, 1.25f);
		tipView.setText(Html.fromHtml("正在打开，请稍候"));
		tipView.setMovementMethod(LinkMovementMethod.getInstance());
		LinearLayout.LayoutParams params1 = 
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params1.topMargin = 18;
		rootView.addView(tipView, params1);
		
		parent.addView(rootView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	@SuppressLint("ResourceAsColor")
	public static void setNoNetworkView(Context context, ViewGroup parent){
		parent.removeAllViews();
		// 网络没有打开
		LinearLayout rootView = new LinearLayout(context);
		rootView.setOrientation(LinearLayout.VERTICAL);
		rootView.setGravity(Gravity.CENTER_HORIZONTAL);
		int padding = ScreenUtil.dip2px(context, 200f);
		rootView.setPadding(0, padding, 0, 0);
		
		TextView tipView = new TextView(context);
		tipView.setTextSize(23.f);
		tipView.setTextColor(context.getResources().getColor(R.color.black_4d4d4d));
		tipView.setGravity(Gravity.CENTER);
		tipView.setLineSpacing(0, 1.25f);
		tipView.setText("网络未连接\n请打开无线网络或数据流量");
		tipView.setMovementMethod(LinkMovementMethod.getInstance());
		rootView.addView(tipView, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

		parent.addView(rootView, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
	}
	
	public static void setNoConnectView(Context context, ViewGroup parent,
			View.OnClickListener listener) {
		parent.removeAllViews();
		// 网络请求失败
		LinearLayout rootView = new LinearLayout(context);
		rootView.setOrientation(LinearLayout.VERTICAL);
		rootView.setGravity(Gravity.CENTER_HORIZONTAL);
		int padding = ScreenUtil.dip2px(context, 80.f);
		rootView.setPadding(0, padding, 0, 0);
		
		//图片
		ImageView imageView = new ImageView(context);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setImageResource(R.drawable.network_error);
		rootView.addView(imageView, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		
		//文字描述
		TextView tipView = new TextView(context);
		tipView.setTextSize(22.f);
		tipView.setTextColor(context.getResources().getColor(R.color.gray_999999));
		tipView.setGravity(Gravity.CENTER);
		tipView.setLineSpacing(0, 1.25f);
		tipView.setText(Html.fromHtml("网络连接失败，再试一次吧"));
		tipView.setMovementMethod(LinkMovementMethod.getInstance());
		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params1.topMargin = ScreenUtil.dip2px(context, 23.f);
		rootView.addView(tipView, params1);
		
		//再试一次按钮
		TextView buttonView = new TextView(context);
		buttonView.setTextSize(26.f);
		buttonView.setTextColor(context.getResources().getColor(R.color.black_787878));
		buttonView.setClickable(true);
		//buttonView.setBackgroundResource(R.drawable.selector_request_again_button);
		buttonView.setBackgroundResource(R.drawable.btn_network_large);
		buttonView.setGravity(Gravity.CENTER);
		buttonView.setText("再试一次");
		buttonView.setOnClickListener(listener);
		
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params2.height = ScreenUtil.dip2px(context, 51.f);
		params2.width = ScreenUtil.dip2px(context, 173.f);
		params2.topMargin = ScreenUtil.dip2px(context, 23.f);
		rootView.addView(buttonView, params2);
		
		parent.addView(rootView, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
	}
	
}
