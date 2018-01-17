package com.android.mplayer.service;

 import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
/**
 * 应用退出服务
 * @author JIANGPENG
 * */
 public class ExitApplicationService extends Application {
 

 private List<Activity> activityList=new LinkedList<Activity>();
 
 private static ExitApplicationService instance;

   private ExitApplicationService(){
  
   }
   /**
    * 单例模式中获取唯一的ExitApplication 实例
    * @author JIANGPENG
    * */
  public static ExitApplicationService getInstance(){
    if(null == instance){
      instance = new ExitApplicationService(); 
     }
     return instance;
   }
  
  /**
   * 添加Activity 到容器中
   * @author JIANGPENG
   * */
    public void addActivity(Activity activity){
     activityList.add(activity);
   }
    
    /**
     * 将Activity 从容器中移除
     * @author JIANGPENG
     * */
    public void removeActivity(Activity activity){
        activityList.remove(activity);
      }
    
    /**
     * 退出应用
     * @author JIANGPENG
     * */
   public void exit(){
	     for(int i = 0;i < activityList.size() ;i++){
		  Activity activity = activityList.get(i);
		  activityList.set(i, null);
		  activity.finish();
	     } 
	     android.os.Process.killProcess(android.os.Process.myPid());
    }
 }
