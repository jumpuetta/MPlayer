package com.android.mplayer.utils;

public class FileUtil {
	/**
	 * 判断是否由外部存储设备
	 * @author JIANGPENG
	 * */
	public static boolean usesExternalStorage() {  
	 if (android.os.Environment.getExternalStorageState().equals(  
		    android.os.Environment.MEDIA_MOUNTED)) {  
	          return true;  
		  } else  
		   return false;  
		 } 
}
