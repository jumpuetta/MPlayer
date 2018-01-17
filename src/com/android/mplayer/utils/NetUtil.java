package com.android.mplayer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * 网络工具类，可判断各种网络连接状态
 * @author JIANGPENG
 * */
public class NetUtil {
	/**
	 * 判断是否有网络连接 
	 * @author JIANGPENG
	 * */
	public static  boolean isNetworkConnected(Context context) { 
		if (context != null) { 
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
		.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
		if (mNetworkInfo != null) { 
		return mNetworkInfo.isAvailable(); 
		} 
		} 
		return false; 
		}

	/**
	 * 判断WIFI网络是否可用 
	 * @author JIANGPENG
	 * */
	public static boolean isWifiConnected(Context context) { 
		if (context != null) { 
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
		.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo mWiFiNetworkInfo = mConnectivityManager 
		.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
		if (mWiFiNetworkInfo != null) { 
		   return mWiFiNetworkInfo.isAvailable(); 
		  } 
		 }  
		  return false; 
		}

	/**
	 * 判断MOBILE网络是否可用
	 * @author JIANGPENG
	 * */
	public static boolean isMobileConnected(Context context) { 
	 if (context != null) { 
	   ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
	   .getSystemService(Context.CONNECTIVITY_SERVICE); 
	   NetworkInfo mMobileNetworkInfo = mConnectivityManager 
	   .getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
	   if (mMobileNetworkInfo != null) { 
	     return mMobileNetworkInfo.isAvailable(); 
	  } 
	 } 
	  return false; 
	}

	/**
	 * 获取当前网络连接的类型信息
	 * @author JIANGPENG
	 * */
	public static int getConnectedType(Context context) { 
	  if (context != null) { 
	    ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
	    .getSystemService(Context.CONNECTIVITY_SERVICE); 
     	NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
    	if (mNetworkInfo != null && mNetworkInfo.isAvailable()) { 
    	   return mNetworkInfo.getType(); 
	   } 
	  } 
	  return -1; 
	 }

}
