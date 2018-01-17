package com.android.mplayer.constants;

import android.os.Environment;

public class AppStateConstant {
	    public static final String APP_PATH = Environment.getExternalStorageDirectory().toString()+"/XXPlayer/";
	    public static final int PLAYLIST_UNKNOWN = -1; 
	    public static final int PLAYLIST_ALL = -2;
		public static final int PLAY_MSG = 1;		//播放
		public static final int PLAY_CURRENT_MSG = 2;   //播放当前
		public static final int PAUSE_MSG = 3;		//暂停
		public static final int STOP_MSG = 4;		//停止
		public static final int CONTINUE_MSG = 5;	//继续
		public static final int PRIVIOUS_MSG = 6;	//上一首
		public static final int NEXT_MSG = 7;		//下一首
		public static final int PROGRESS_CHANGE = 8;//进度改变
		public static final int PLAYING_MSG = 9;	//正在播放
		public static final int UPDATE_CURRENT_INFO_MSG = 10;//更新UI
		public static final int PLAY_MODE = 11;
		public static final int INIT_BOTTOM_CALL_BACK_INFO = 12;
		public static final int INIT_CURRENT_SONG_CALL_BACK_INFO = 13;
		public static final int INIT_BOTTOM_INFO_MSG = 14;
		public static final int INIT_CURRENT_SONG_INFO_MSG = 15;
		public static final int EXIT_APPLICATION = 16;
		public static final int INIT_LRC_AGAIN = 17;
		public static final int LRC_NOT_FOUND = 18;
		public static final String IS_ALBUM_ID = "IS_ALBUM_ID";
		public static final String IS_ARTIST_ID = "IS_ARTIST_ID";
		public static final String IS_ALLSONG_ID = "IS_ALLSONG_ID";
		public static final String IS_PLAYLIST_ID = "IS_PLAYLISTID_ID";
		
}
