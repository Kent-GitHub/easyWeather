package com.easynetwork.weather.core.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import me.tangke.slidemenu.SlideMenu;

/**
 * Created by yanming on 2016/8/17.
 */
public class MySlideMenu extends SlideMenu {
    public MySlideMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MySlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySlideMenu(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        return super.dispatchKeyEvent(event);
    }

}
