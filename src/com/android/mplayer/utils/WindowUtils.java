package com.android.mplayer.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;


/**
 * 窗口工具类，用来获取各种屏幕属性
 * */
public class WindowUtils {

	private static int width;
	private static int height;
   /**
    * 获取设备屏幕宽度
    * @author JIANGPENG
    * @param context 上下文
    * @return int
    * */
	public static int getWidth(Context context) {
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels;
		return width;
	}
	
	
	 /**
	    * 获取设备屏幕高度
	    * @author JIANGPENG
	    * @param context 上下文
	    * @return int
	    * */
	public static int getHeight(Context context) {
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(metric);
		height = metric.heightPixels;
		return height;
	}

}
