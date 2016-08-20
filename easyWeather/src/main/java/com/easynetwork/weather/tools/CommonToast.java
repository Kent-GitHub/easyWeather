package com.easynetwork.weather.tools;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.easynetwork.weather.R;

/**
 * example:CommonToast.makeText(this, "删除失败，请稍后再试", Toast.LENGTH_LONG).show();
 * 
 * @author wang.huanbing
 * 
 */
public class CommonToast extends Toast {

	private TextView tView;
	private String text;
	private int time;
	private Context context;

	public CommonToast(Context context, String text, int time) {
		super(context);
		this.context = context;
		this.text = text;
		this.time = time;
		init();
	}

	@SuppressLint("NewApi")
	private void init() {
		LinearLayout rootView = new LinearLayout(context);
		rootView.setOrientation(LinearLayout.VERTICAL);
		rootView.setGravity(Gravity.CENTER_HORIZONTAL);

		tView = new TextView(context);
		tView.setTextSize(20.f);
		tView.setTextColor(context.getResources().getColor(
				android.R.color.white));
		tView.setBackground(context.getResources().getDrawable(
				R.drawable.bg_toast));
		tView.setGravity(Gravity.CENTER);
		tView.setText(text);
		tView.setMovementMethod(LinkMovementMethod.getInstance());

		LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rootView.addView(tView, params);

		setView(rootView);
		setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 320);// 160dp
		setDuration(time);
	}

	public void setText(String text) {
		tView.setText(text);
	}

	public static Toast makeText(Context context, CharSequence text,
			int duration) {
		CommonToast result = new CommonToast(context, text.toString(), duration);
		return result;
	}
}
