package com.android.mplayer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.mplayer.R;
import com.android.mplayer.constants.AppStateConstant;
import com.android.mplayer.dao.AlbumArtDao;
import com.android.mplayer.dao.MusicDao;
import com.android.mplayer.domain.LrcInfo;
import com.android.mplayer.domain.LrcProcess;
import com.android.mplayer.domain.Mp3Info;

/**
 * 播放器服务类  应用核心部分
 * @author JIANGPENG
 * */
public class MusicPlayService extends Service{

	private MediaPlayer mediaPlayer; // 媒体播放器对象
	private String path; 			// 音乐文件路径
	private boolean isPause = false; 		// 暂停状态
	public static boolean IS_FIRST_PLAY = true;
	public static boolean IS_PLAYING_MSG = false;  
	private int current  = 0; 		// 记录当前正在播放的音乐
	private int old = 0;
	private List<Mp3Info> mp3Infos;	//存放Mp3Info对象的集合
	private int collectId ;  //集合的Id
	private String collectType ;
	private int status = 3;			//播放状态，默认为顺序播放
	private MyServiceReceiver myReceiver;	//自定义广播接收器
	private int currentTime;		//当前播放进度
	private int duration;			//播放长度
	private LrcProcess mLrcProcess;	//歌词处理
	private List<LrcInfo> lrcList = new ArrayList<LrcInfo>(); //存放歌词列表对象
	private int index = 0;			//歌词检索值
	private Notification notification; //通知
	private NotificationManager notificationManager; //通知管理器
	/**
	 * handler用来接收服务内部消息，让后将信息以广播形式通知其他组件
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int message = msg.what;
			switch (message) {
			//更新当前播放的歌曲
			case AppStateConstant.UPDATE_CURRENT_INFO_MSG:
				if(mediaPlayer != null) {
					Intent intent = new Intent();
					intent.setAction("MUSIC_SERVICE_UPDATEUI_RECEIVER");
					if(old!=current){
						intent.putExtra("change",true);
						old=current;
					}else{
						intent.putExtra("change",false);
					}
					intent.putExtra("mp3Info",mp3Infos.get(current));
					sendBroadcast(intent);
				}
				break;
			//初始化小控制台
			case AppStateConstant.INIT_BOTTOM_INFO_MSG:
				if(mediaPlayer != null && !IS_FIRST_PLAY) {
					Intent intent = new Intent();
					intent.setAction("MUSIC_SERVICE_INIT_BOTTOM_RECEIVER");
					intent.putExtra("mp3Info",mp3Infos.get(current));
					sendBroadcast(intent);
				}
				break;
			//更新当前歌曲集合
			case AppStateConstant.INIT_CURRENT_SONG_INFO_MSG:
				Intent intent = new Intent();
				intent.setAction("MUSIC_SERVICE_INIT_CURRENT_SONG_RECEIVER");
				intent.putExtra("collectType",collectType);
				intent.putExtra("collectId",collectId);
				sendBroadcast(intent);
				break;
			default:
				break;
			}
		
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();

		mediaPlayer = new MediaPlayer();
		//默认集合里面放置所有歌曲
		mp3Infos = MusicDao.getAllMp3Infos(MusicPlayService.this);
		collectId = -1000; //-1000代表所有音乐
		collectType = AppStateConstant.IS_ALLSONG_ID;
		
		//设置音乐播放完成时的监听器
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				if (status == 2) { // 单曲循环
					mediaPlayer.start();
				} else if (status == 3) { // 全部循环
					current++;
					if(current > mp3Infos.size() - 1) {	//变为第一首的位置继续播放
						current = 0;
					}
					play(0);
				} else if (status == 1) { // 顺序播放
					current++;	//下一首位置
					if (current <= mp3Infos.size() - 1) {
						play(0);
					}else {
						mediaPlayer.seekTo(0);
						current = 0;
					}
				} else if(status == 4) {	//随机播放
					current = getRandomIndex(mp3Infos.size() - 1);
					play(0);
				}
			}
		});
        
		//注册广播
		myReceiver = new MyServiceReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("PLAY_SERVICE_ACTION");
		registerReceiver(myReceiver, filter);
		
		
		//增加消息栏
		showNotification();
		
	}

	/**
	 * 获取随机位置
	 * @param end
	 * @return
	 */
	protected int getRandomIndex(int end) {
		int index = (int) (Math.random() * end);
		return index;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		  super.onStart(intent, startId);
	}

	/**
	 * 初始化歌词配置
	 */
	public void initLrc(Mp3Info mp3){
		  mLrcProcess = new LrcProcess(getApplicationContext());
		  //读取歌词文件
		  mLrcProcess.readLRC(mp3);
		  //传回处理后的歌词文件
		  lrcList = mLrcProcess.getLrcList();
		  Intent intent = new Intent();
		  intent.putExtra("mp3", mp3);
		  intent.setAction("MUSIC_SERVICE_INIT_LRC_FOR_CURRENT_SONG");
		  sendBroadcast(intent);
	}
	
	/**
	 * 该进程用于更新进度条和显示歌词
	 * */
	Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			Intent intent = new Intent();
			intent.setAction("MUSIC_SERVICE_SEEKBAR_LRC_UPDATE");
			int time = mediaPlayer.getCurrentPosition();
			intent.putExtra("time", time);
			intent.putExtra("lrcIndex", lrcIndex());
			sendBroadcast(intent);
			handler.postDelayed(mRunnable, 100);
		}
	};
	
	/**
	 * 根据时间获取歌词显示的索引值
	 * @return
	 */
	public int lrcIndex() {
		if(mediaPlayer.isPlaying()) {
			currentTime = mediaPlayer.getCurrentPosition();
			duration = mediaPlayer.getDuration();
		}
		if(currentTime < duration) {
			for (int i = 0; i < lrcList.size(); i++) {
				if (i < lrcList.size() - 1) {
					if (currentTime < lrcList.get(i).getLrcTime() && i == 0) {
						index = i;
					}
					if (currentTime > lrcList.get(i).getLrcTime()
							&& currentTime < lrcList.get(i + 1).getLrcTime()) {
						index = i;
					}
				}
				if (i == lrcList.size() - 1
						&& currentTime > lrcList.get(i).getLrcTime()) {
					index = i;
				}
			}
		}
		return index;
	}
	
	/**
	 * 播放音乐
	 * @param position
	 */
	private void play(int currentTime) {
		try {
			path = mp3Infos.get(current).getUrl();
			initLrc(mp3Infos.get(current));
			handler.post(mRunnable);
			mediaPlayer.reset();// 把各项参数恢复到初始状态
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare(); // 进行缓冲
			mediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));// 注册一个监听器
			isPause = false;
			IS_PLAYING_MSG = true;
			IS_FIRST_PLAY = false;
			Message msg = new Message();
			msg.what = AppStateConstant.UPDATE_CURRENT_INFO_MSG;
			handler.sendMessage(msg);
			updateNotification(current);
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     * 拖动进度条是调用的函数，比play简单省资源，可防止seekBar闪烁
     * */
	private void seekToPlay(int currentTime) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/**
	 * 暂停音乐
	 */
	private void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			IS_PLAYING_MSG = false;
			isPause = true;
			updateNotification();
			Message msg = new Message();
			msg.what = AppStateConstant.UPDATE_CURRENT_INFO_MSG;
			handler.sendMessage(msg);
		}
	}
   
	/**
	 * 重新开始音乐
	 * */
	private void resume() {
		if (isPause) {
			mediaPlayer.start();
			IS_PLAYING_MSG = true;
			isPause = false;
			updateNotification();
			Message msg = new Message();
			msg.what = AppStateConstant.UPDATE_CURRENT_INFO_MSG;
			handler.sendMessage(msg);
		}
	}

	/**
	 * 上一首
	 */
	private void previous() {
		current--;
		if(current == -1)
			current = mp3Infos.size() - 1;
		path = mp3Infos.get(current).getUrl();
		play(0);
	}

	/**
	 * 下一首
	 */
	private void next() {
        current++;
        if(current == mp3Infos.size())
			current = 0;
        path = mp3Infos.get(current).getUrl();
		play(0);
	}

	/**
	 * 停止音乐
	 */
	private void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			IS_PLAYING_MSG = false;
			updateNotification();
			try {
				mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDestroy() {
	  if (mediaPlayer != null) {//释放媒体资源
			mediaPlayer.stop();
			mediaPlayer.release();
			IS_PLAYING_MSG = false;
			mediaPlayer = null;
			handler.removeCallbacks(mRunnable);
			mp3Infos = null;
			lrcList = null;
		}
	  notificationManager.cancel(0);
	  super.onDestroy();
	}

	/**
	 * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
	 */
	private final class PreparedListener implements OnPreparedListener {
		private int currentTime;

		public PreparedListener(int currentTime) {
			this.currentTime = currentTime;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start(); // 开始播放
			if (currentTime > 0) { // 如果音乐不是从头播放
				mediaPlayer.seekTo(currentTime);
			}
		}
	}
	
	/**
	 * 服务器内部广播，接受界面发送过来的指令
	 * */

	public class MyServiceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int msg = intent.getIntExtra("MSG", 0);	
			Message message = new Message();
			switch (msg) {
			  case AppStateConstant.PLAY_MODE:
				int control = intent.getIntExtra("playMode", 3);
				switch (control) {
				case 1:
					status = 1; // 将播放状态置为1表示：顺序播放
					break;
				case 2:
					status = 2;	//将播放状态置为2表示：单曲循环
					break;
				case 3:
					status = 3;	//将播放状态置为3表示：
					break;
				case 4:
					status = 4;	//将播放状态置为4表示：随机播放
					break;
			    default:
			    	status = 3;	//将播放状态置为3表示：全部循环 
					break;
				}
				break;
				
			  case AppStateConstant.PLAY_MSG://直接播放音乐
					  current = intent.getIntExtra("listPosition", 0);	//当前播放歌曲的在mp3Infos的位置
					  String type = intent.getStringExtra("collectType");
					  int id = intent.getIntExtra("collectId", -1000);//默认为所有音乐
					  changeCollect(type,id);
					  play(0);
			    break;
			  case AppStateConstant.PLAY_CURRENT_MSG://播放当前音乐
					  if(isPause){
						resume();
					  }else{
						//直接播放当前集合里面的第一首音乐
					    play(0);
					  }
                break;
			  case AppStateConstant.PAUSE_MSG:	//暂停
				   pause();	
				   break; 	
			  case AppStateConstant.STOP_MSG:		//停止
				   stop();
				   break;
			  case AppStateConstant.CONTINUE_MSG:	//继续播放
				   resume();
				   break;
			  case AppStateConstant.PRIVIOUS_MSG:	//上一首
				   previous();
				   break;
			  case AppStateConstant.NEXT_MSG:		//下一首
				   next();
				   break;
			  case AppStateConstant.PROGRESS_CHANGE:	//进度更新
				   currentTime = intent.getIntExtra("jumppoint", 0);
				   seekToPlay(currentTime);
				   break; 	  
			  case AppStateConstant.PLAYING_MSG:  
				   handler.sendEmptyMessage(1);
				   break;
			  case AppStateConstant.INIT_BOTTOM_CALL_BACK_INFO://初始化小控制台
				   message.what = AppStateConstant.INIT_BOTTOM_INFO_MSG;
				   handler.sendMessage(message);
				   break;
			  case AppStateConstant.INIT_CURRENT_SONG_CALL_BACK_INFO:
				    message.what = AppStateConstant.INIT_CURRENT_SONG_INFO_MSG;
					handler.sendMessage(message);
				   break;
			  case AppStateConstant.INIT_LRC_AGAIN://当歌词下载完成后第二次初始化歌词
				   initLrc(mp3Infos.get(current));
				   break;
			  case AppStateConstant.LRC_NOT_FOUND://歌词没发现 打印土司
				  Toast.makeText(context, getString(R.string.not_found_lrc), Toast.LENGTH_SHORT).show(); 
				   break;
			  case AppStateConstant.EXIT_APPLICATION://退出应用
				   notificationManager.cancel(0);
				   ExitApplicationService.getInstance().exit();
				   break;
			default:
				break;
			}
		}
		
		/**
		 * 改变歌曲集合
		 * @author JIANGPENG
		 * @param type 集合类型
		 * @param id 集合ID
		 * */
		private void changeCollect(String type, int id){
			if(type.equals(collectType) && id == collectId){//若和当期集合一样 则不作任何操作
				return;
			}else{
				if(type.equals(AppStateConstant.IS_ALLSONG_ID)){
					mp3Infos = MusicDao.getAllMp3Infos(getApplicationContext());
					collectType = type;
					collectId = id;
				}else if(type.equals(AppStateConstant.IS_ALBUM_ID)){
					mp3Infos = MusicDao.getMp3InfosByAlbumID(getApplicationContext(), id);
					collectType = type;
					collectId = id;
				}else if(type.equals(AppStateConstant.IS_ARTIST_ID)){
					mp3Infos = MusicDao.getMp3InfosByArtistID(getApplicationContext(), id);
					collectType = type;
					collectId = id;
				}else if(type.equals(AppStateConstant.IS_PLAYLIST_ID)){
					mp3Infos = MusicDao.getMp3InfosByPlayListId(getApplicationContext(), id);
					collectType = type;
					collectId = id;
				}
			}
		}
	}
       
     /**
      * 显示通知栏
      * @author JIANGPENG
      * */
	 private void showNotification() {
		    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    Mp3Info mp3Info = mp3Infos.get(0);
	        Bitmap b = AlbumArtDao.getArtwork(getApplicationContext(), 
	        		mp3Info.getId(), mp3Info.getAlbumId(), true, true);
	        RemoteViews views = new RemoteViews(getPackageName(), R.layout.notification_small_layout);
	        RemoteViews bigViews = new RemoteViews(getPackageName(), R.layout.notification_big_layout);	        
	        if (b != null) {
	            views.setViewVisibility(R.id.status_bar_icon, View.GONE);
	            views.setViewVisibility(R.id.status_bar_album_art, View.VISIBLE);
	            views.setImageViewBitmap(R.id.status_bar_album_art, b);
	            bigViews.setImageViewBitmap(R.id.status_bar_album_art, b);
	        } else {
	            views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
	            views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
	        }
	        
	        Intent intentNex = new Intent();
            intentNex.putExtra("MSG", AppStateConstant.NEXT_MSG);
            PendingIntent pendNex = PendingIntent.getBroadcast(getApplicationContext(), 0, intentNex, 0); 
            bigViews.setOnClickPendingIntent(R.id.status_bar_next, pendNex);
            views.setOnClickPendingIntent(R.id.status_bar_next, pendNex);
	        
	        
	        Intent intentPre = new Intent();
	        intentPre.setAction("PLAY_SERVICE_ACTION");
	        intentPre.putExtra("MSG", AppStateConstant.PRIVIOUS_MSG);
            PendingIntent pendPre = PendingIntent.getBroadcast(getApplicationContext(), 1, intentPre, 1); 
            bigViews.setOnClickPendingIntent(R.id.status_bar_prev, pendPre);
	        
           
           
            Intent intentPlay = new Intent();
            if(IS_PLAYING_MSG){
            	intentPlay.putExtra("MSG", AppStateConstant.PAUSE_MSG);
  		    }else{
  		    	intentPlay.putExtra("MSG", AppStateConstant.PLAY_CURRENT_MSG);
  		    }
            PendingIntent pendPlay = PendingIntent.getBroadcast(getApplicationContext(), 2, intentPlay, 2); 
            bigViews.setOnClickPendingIntent(R.id.status_bar_play, pendPlay);
            views.setOnClickPendingIntent(R.id.status_bar_play, pendPlay);
            
            Intent intentExit = new Intent();
            intentExit.putExtra("MSG",AppStateConstant.EXIT_APPLICATION);
            PendingIntent pendExit = PendingIntent.getBroadcast(getApplicationContext(), 3, intentExit, 3); 
            bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pendExit);
            views.setOnClickPendingIntent(R.id.status_bar_collapse, pendExit);
            
            
	        views.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);
	        bigViews.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);

	        views.setTextViewText(R.id.status_bar_track_name, mp3Info.getDisplayName());
	        bigViews.setTextViewText(R.id.status_bar_track_name, mp3Info.getDisplayName());
	        
	        views.setTextViewText(R.id.status_bar_artist_name, mp3Info.getArtist());
	        bigViews.setTextViewText(R.id.status_bar_artist_name, mp3Info.getArtist());
	        
	        bigViews.setTextViewText(R.id.status_bar_album_name, mp3Info.getAlbum());
	        
	        notification = new Notification.Builder(this).build();
	        notification.contentView = views;
	        notification.bigContentView = bigViews;
	       // notification.flags = Notification.;
	        notification.icon = R.drawable.stat_notify_music;
	        notification.contentIntent = PendingIntent
	                .getActivity(this, 0, new Intent("com.android.mplayer.MainPlayerActivity")
	                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
	        notificationManager.notify(0, notification);
	        startForeground(0, notification);
	    }
	    
	
	  /**
      * 更新通知栏
      * @author JIANGPENG
      * */
	public void updateNotification(int current){
		notification.contentView.setImageViewBitmap(R.id.status_bar_album_art,
      		  AlbumArtDao.getArtwork(getApplicationContext(), 
      				  mp3Infos.get(0).getId(), mp3Infos.get(current).getAlbumId(), true, true) );  
		notification.contentView.setTextViewText(R.id.status_bar_track_name,mp3Infos.get(current).getDisplayName());
		notification.contentView.setTextViewText(R.id.status_bar_artist_name,mp3Infos.get(current).getArtist());
		notification.contentView.setTextViewText(R.id.status_bar_album_name,mp3Infos.get(current).getAlbum());
		notification.bigContentView.setImageViewBitmap(R.id.status_bar_album_art,
	      		  AlbumArtDao.getArtwork(getApplicationContext(), 
	      				  mp3Infos.get(0).getId(), mp3Infos.get(current).getAlbumId(), true, true) );  
	    notification.bigContentView.setTextViewText(R.id.status_bar_track_name,mp3Infos.get(current).getDisplayName());
	    notification.bigContentView.setTextViewText(R.id.status_bar_artist_name,mp3Infos.get(current).getArtist());
	    notification.bigContentView.setTextViewText(R.id.status_bar_album_name,mp3Infos.get(current).getAlbum());
		
	    if(IS_PLAYING_MSG){
	    	notification.contentView.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);
	    	notification.bigContentView.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);
		}else{
			notification.contentView.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_play);
	    	notification.bigContentView.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_play);
		}
	    notificationManager.notify(0, notification);  
	}
	
	 /**
     * 更新通知栏
     * @author JIANGPENG
     * */
	public void updateNotification(){
	    if(IS_PLAYING_MSG){
	    	notification.contentView.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);
	    	notification.bigContentView.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);
		}else{
			notification.contentView.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_play);
	    	notification.bigContentView.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_play);
		}
	    notificationManager.notify(0, notification);  
	}
}
