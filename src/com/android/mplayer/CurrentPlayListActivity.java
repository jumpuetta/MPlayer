package com.android.mplayer;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.mplayer.adapter.CurrentListItemAdapter;
import com.android.mplayer.constants.AppStateConstant;
import com.android.mplayer.dao.AlbumDao;
import com.android.mplayer.dao.ArtistDao;
import com.android.mplayer.dao.MusicDao;
import com.android.mplayer.dao.PlayListDao;
import com.android.mplayer.domain.Mp3Info;
import com.android.mplayer.service.ExitApplicationService;

/**
 * 用于当前播放列别集合
 */
public class CurrentPlayListActivity extends Activity implements OnClickListener {
    private CurrentPlayListReceiver myReceiver;	
    private ListView current_playlist_list;
    private ImageButton bt_current_to_play;
    private TextView tv_current_title;
    private List<Mp3Info> mp3Infos;
	@Override
	protected void onCreate(Bundle inState) {
		super.onCreate(inState);
		setContentView(R.layout.current_play_song_list);
		initShowActivity();
		attachListeners();	
		
		myReceiver = new CurrentPlayListReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("MUSIC_SERVICE_INIT_CURRENT_SONG_RECEIVER");
		registerReceiver(myReceiver, filter);
		
        ExitApplicationService.getInstance().addActivity(this);
	}
	
	  protected void onResume() {
		    super.onResume();
		    Intent intent = new Intent();
	        intent.setAction("PLAY_SERVICE_ACTION");
	        intent.putExtra("MSG", AppStateConstant.INIT_CURRENT_SONG_CALL_BACK_INFO);
	        sendBroadcast(intent);
	  }
	
	 private void initShowActivity(){
		 current_playlist_list = (ListView)findViewById(R.id.current_playlist_list);
		 bt_current_to_play = (ImageButton)findViewById(R.id.bt_current_to_play);
		 tv_current_title = (TextView)findViewById(R.id.tv_current_title);
		 mp3Infos =null;
	 }
	
	private void attachListeners(){
		bt_current_to_play.setOnClickListener(this);
	}
	
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClassName(this, "com.android.mplayer.MainPlayerActivity");
		startActivity(intent);
	    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	    ExitApplicationService.getInstance().removeActivity(this);
	    finish();
	}
	@Override
	/**
	 * 返回按钮单击事件
	 * */
	public void onClick(View v) {
		onBackPressed();
	}
	
	/**
	 * 重写元素单击事件
	 * **/
	public void addItemClickListener(final String collectType,final int id){
		current_playlist_list.setOnItemClickListener(new OnItemClickListener() {
 			public void onItemClick(AdapterView<?> arg0, View v,
 					int arg2, long arg3) {
 				int position = Integer.parseInt(((TextView)v.findViewById(R.id.current_item_position)).getText().toString().trim());
 				Intent intent = new Intent();
 				intent.setAction("PLAY_SERVICE_ACTION");
 				intent.putExtra("listPosition", position);
 				intent.putExtra("collectType", collectType);
 				intent.putExtra("collectId", id);
 				intent.putExtra("MSG", AppStateConstant.PLAY_MSG);
 				sendBroadcast(intent);					
 			}
 		});
	}
	
	/**
	 * 接受来自服务的信息来确定要现实的内容
	 * */
	 public class CurrentPlayListReceiver extends BroadcastReceiver{

		public void onReceive(Context context, Intent intent) {
			//更新MusicListActivity的UI
			String collectType = intent.getStringExtra("collectType");
			int id = intent.getIntExtra("collectId",0);
			String title = "";
			if(AppStateConstant.IS_ALLSONG_ID.equals(collectType)){
				mp3Infos = MusicDao.getAllMp3Infos(getApplicationContext());
				title = getString(R.string.current_all_song_list);
				current_playlist_list.setAdapter(new CurrentListItemAdapter(CurrentPlayListActivity.this, mp3Infos));
				addItemClickListener(AppStateConstant.IS_ALLSONG_ID,id);
			}else if(AppStateConstant.IS_ALBUM_ID.equals(collectType)){
				title = getString(R.string.current_album_list)+AlbumDao.getAlbumByAlbumId(getApplicationContext(), id).getAlbum();
				mp3Infos = MusicDao.getMp3InfosByAlbumID(getApplicationContext(), id);
				current_playlist_list.setAdapter(new CurrentListItemAdapter(CurrentPlayListActivity.this, mp3Infos));
				addItemClickListener(AppStateConstant.IS_ALBUM_ID,id);
			}else if(AppStateConstant.IS_ARTIST_ID.equals(collectType)){
				title = getString(R.string.current_artist_list)+ArtistDao.getArtistByArtistId(getApplicationContext(), id).getArtist();
				mp3Infos = MusicDao.getMp3InfosByArtistID(getApplicationContext(), id);
				current_playlist_list.setAdapter(new CurrentListItemAdapter(CurrentPlayListActivity.this, mp3Infos));
				addItemClickListener(AppStateConstant.IS_ARTIST_ID,id);
			}else if(AppStateConstant.IS_PLAYLIST_ID.equals(collectType)){
				mp3Infos = MusicDao.getMp3InfosByPlayListId(getApplicationContext(), id);
				title = getString(R.string.current_playlist_list)+PlayListDao.getPlayListByListId(getApplicationContext(), id).getPlayList();
				current_playlist_list.setAdapter(new CurrentListItemAdapter(CurrentPlayListActivity.this, mp3Infos));
				addItemClickListener(AppStateConstant.IS_PLAYLIST_ID,id);
			}else {
				mp3Infos = new ArrayList<Mp3Info>();
			}
			tv_current_title.setText(title);
			
		}
	}
	 
}
