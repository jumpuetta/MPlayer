package com.android.mplayer.domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.mplayer.R;
import com.android.mplayer.constants.AppStateConstant;
import com.android.mplayer.utils.NetUtil;
import com.android.mplayer.utils.SearchLrcUtil;



/**
 *	处理歌词的类
 */
public class LrcProcess {
	private List<LrcInfo> lrcList;	//List集合存放歌词内容对象
	private LrcInfo mLrcContent;		//声明一个歌词内容对象
	private Context context;
	/**
	 * 无参构造函数用来实例化对象
	 */
	public LrcProcess(Context context) {
		lrcList = new ArrayList<LrcInfo>();
		this.context = context;
	}
	
	/**
	 * 读取本地指定.lrc歌词
	 * @param path
	 * @return
	 */
	public String readLRC(String path) {
		//定义一个StringBuilder对象，用来存放歌词内容
		StringBuilder stringBuilder = new StringBuilder();
		File f = new File(path.replace(".mp3", ".lrc"));
		
		try {
			//创建一个文件输入流对象
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(fis, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while((s = br.readLine()) != null) {
				//替换字符
				s = s.replace("[", "");
				s = s.replace("]", "@");
				
				//分离“@”字符
				String splitLrcData[] = s.split("@");
				if(splitLrcData.length > 1) {
					//新创建歌词内容对象
					mLrcContent = new LrcInfo();
					
					mLrcContent.setLrcStr(splitLrcData[1]);
					
					//处理歌词取得歌曲的时间
					int lrcTime = time2Str(splitLrcData[0]);
					
					mLrcContent.setLrcTime(lrcTime);
					
					//添加进列表数组
					lrcList.add(mLrcContent);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			stringBuilder.append("木有歌词文件，赶紧去下载！...");
		} catch (IOException e) {
			e.printStackTrace();
			stringBuilder.append("木有读取到歌词哦！");
		}
		return stringBuilder.toString();
	}
	
	/**
	 * 读取解析lrc歌词，若没有则从网络上下载
	 * @param path
	 * @return
	 */
	public int readLRC(Mp3Info mp3) {
		//定义一个StringBuilder对象，用来存放歌词内容
		File f ;
		String title = mp3.getTitle();
		String artist = mp3.getArtist();
		String displayName = mp3.getDisplayName().split("\\.")[0];
		
		f = new File(AppStateConstant.APP_PATH+"lrc/"+title+"-"+artist+".lrc");
		if(f.exists()){
			parseLrc(f);
			return 1;
		}else{
			f = new File(AppStateConstant.APP_PATH+"lrc/"+displayName+".lrc");
		    if(f.exists()){
		    	parseLrc(f);
		    	return 1;
		    }else{
		    	if(!NetUtil.isNetworkConnected(context)){
	                Toast.makeText(context, context.getString(R.string.net_not_use), Toast.LENGTH_SHORT).show();     				
				}else{
					downLoadLrc(title,artist);
				} 
		    }
		}
		return 0;	
//			  new Thread(new Runnable() {//新建线程开启歌词下载
//				@Override
//				public void run() {
//					boolean flag = new SearchLrcUtil(title, artist,context).saveLrcInScard(title+"-"+artist+".lrc");
//				    if(flag){
//				    	Intent intent = new Intent();
//				    	intent.setAction("PLAY_SERVICE_ACTION");
//					    intent.putExtra("MSG", AppStateConstant.INIT_LRC_AGAIN);
//					    context.sendBroadcast(intent);
//				    }else{
//				    	Intent intent = new Intent();
//				    	intent.setAction("PLAY_SERVICE_ACTION");
//					    intent.putExtra("MSG", AppStateConstant.LRC_NOT_FOUND);
//					    context.sendBroadcast(intent);
//				    }
//				}
//			  }).start();
			
	}
	
	
	/**
	 * 按歌曲名，歌手名从网上获取歌词
	 * 以歌曲名—歌手名为文件名
	 * */
    public void downLoadLrc(final String title,final String artist){
    	new Thread(new Runnable() {//新建线程开启歌词下载
			@Override
			public void run() {
				boolean flag = new SearchLrcUtil(title, artist,context).saveLrcInScard(title+"-"+artist+".lrc");
			    if(flag){
			    	Intent intent = new Intent();
			    	intent.setAction("PLAY_SERVICE_ACTION");
				    intent.putExtra("MSG", AppStateConstant.INIT_LRC_AGAIN);
				    context.sendBroadcast(intent);
			    }else{
			    	Intent intent = new Intent();
			    	intent.setAction("PLAY_SERVICE_ACTION");
				    intent.putExtra("MSG", AppStateConstant.LRC_NOT_FOUND);
				    context.sendBroadcast(intent);
			    }
			}
		  }).start();
    }
	
	
	/**
	 * 从文件里面解析歌词
	 * */
	
	public void parseLrc(File f){
		 try {
			//创建一个文件输入流对象
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(fis, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while((s = br.readLine()) != null) {
				//替换字符
				s = s.replace("[", "");
				s = s.replace("]", "@");
				//分离“@”字符
				String splitLrcData[] = s.split("@");
				if(splitLrcData.length > 1) {
					for(int i = 0;i < splitLrcData.length-1;i++){
					//新创建歌词内容对象
					mLrcContent = new LrcInfo();
					//处理歌词取得歌曲的时间
					int lrcTime = time2Str(splitLrcData[i]);
					mLrcContent.setLrcTime(lrcTime);
					mLrcContent.setLrcStr(splitLrcData[splitLrcData.length-1].replace(" ","\n"));
					//添加进列表数组
					lrcList.add(mLrcContent);
					}
				}
			}
			for(int i = 0;i<lrcList.size()-1;i++)//对歌词按时间进行排序
				for(int j = i+1;j<lrcList.size();j++){
					if(lrcList.get(i).getLrcTime()>lrcList.get(j).getLrcTime()){
						mLrcContent = lrcList.get(i);
						lrcList.set(i, lrcList.get(j));
						lrcList.set(j, mLrcContent);
					}
				}
			  } catch (FileNotFoundException e) {
			    e.printStackTrace();
			  } catch (IOException e) {
				e.printStackTrace();
			  }
	}
	
	
	/**
	 * 解析歌词时间
	 * 歌词内容格式如下：
	 * [00:02.32]陈奕迅
	 * [00:03.43]好久不见
	 * [00:05.22]歌词制作  王涛
	 * @param timeStr
	 * @return
	 */
	public int time2Str(String timeStr) {
		int minute = 0,second = 0,millisecond = 0;
		timeStr = timeStr.replace(":", ".");
		timeStr = timeStr.replace(".", "@");
		
		String timeData[] = timeStr.split("@");	//将时间分隔成字符串数组
		
		if(timeData.length == 3){
			//分离出分、秒并转换为整型
		   minute = Integer.parseInt(timeData[0]);
		   second = Integer.parseInt(timeData[1]);
		   millisecond = Integer.parseInt(timeData[2]);
		}else if(timeData.length == 2){
		   minute = Integer.parseInt(timeData[0]);
		   second = Integer.parseInt(timeData[1]);
		   millisecond = 0;
		}
		
		//计算上一行与下一行的时间转换为毫秒数
		int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
		return currentTime;
	}
	
	public List<LrcInfo> getLrcList() {
		return lrcList;
	}
}
