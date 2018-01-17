package com.android.mplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.mplayer.adapter.PlayListSongItemAdapter;
import com.android.mplayer.adapter.SongListItemAdapter;
import com.android.mplayer.constants.AppStateConstant;
import com.android.mplayer.dao.AlbumArtDao;
import com.android.mplayer.dao.MusicDao;
import com.android.mplayer.domain.Mp3Info;
import com.android.mplayer.service.ExitApplicationService;
import com.android.mplayer.service.MusicPlayService;
import com.android.mplayer.utils.LoadSmallImageUtil;

public class AlbumArtistBroswerActivity extends Activity implements OnClickListener{
	private ImageButton track_play_back;
	private ImageButton audio_player_prev;
	private ImageButton audio_player_play;
	private ImageButton audio_player_next;
	private ImageView bottom_action_bar_song_art;
	private ImageView half_artist_image;
	private TextView half_album_name;
	private TextView half_artist_name;
	private TextView track_play_title;
	private TextView bottom_action_bar_music_title;
	private TextView bottom_action_bar_artist_name;
	private ListView list_view;
	private BaseAdapter  adapter;
	private long id;
	private AlbumArtistReceiver myReceiver;
	private Animation anim ;
   protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.album_or_artist_browser);
	initShowActivity();
	attachListeners();
	
	myReceiver = new AlbumArtistReceiver();
	IntentFilter filter = new IntentFilter();
	filter.addAction("MUSIC_SERVICE_UPDATEUI_RECEIVER");
	filter.addAction("MUSIC_SERVICE_INIT_BOTTOM_RECEIVER");
	filter.addAction("FRESH_PLAY_LIST_ITEM_RECEIVER");
	registerReceiver(myReceiver, filter);
	
	ExitApplicationService.getInstance().addActivity(this);
   }
   
  @Override
    protected void onResume() {
	    super.onResume();
	    Intent intent = new Intent();
        intent.setAction("PLAY_SERVICE_ACTION");
        intent.putExtra("MSG", AppStateConstant.INIT_BOTTOM_CALL_BACK_INFO);//初始化小控制台广播
        sendBroadcast(intent);
    }
  
  /**
   * 初始化界面信息
   * */
  @Override
   protected void onStart() {
	  super.onStart();
	  Intent intent = getIntent();
		String type = intent.getStringExtra("type");
		if(AppStateConstant.IS_ALBUM_ID.equals(type)){//显示专辑里面的歌曲信息
			track_play_title.setText(intent.getStringExtra("album"));
			half_album_name.setText(intent.getStringExtra("album"));
			half_artist_name.setText(intent.getStringExtra("artist"));
            id = Long.parseLong(intent.getStringExtra("albumId"));
			loadImage(half_artist_image,(int)id,AppStateConstant.IS_ALBUM_ID);
			adapter = new SongListItemAdapter(this,MusicDao.getMp3InfosByAlbumID(this, id));
			list_view.setAdapter(adapter);
			list_view.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View v,
						int arg2, long arg3) {
					int position = Integer.parseInt(((TextView)v.findViewById(R.id.listview_item_position)).getText().toString().trim());
					Intent intent = new Intent();
					intent.setAction("PLAY_SERVICE_ACTION");
					intent.putExtra("listPosition", position);
					intent.putExtra("collectType", AppStateConstant.IS_ALBUM_ID);
					intent.putExtra("collectId", (int)id);
					intent.putExtra("MSG", AppStateConstant.PLAY_MSG);
					sendBroadcast(intent);					
				}
			});
		}else if(AppStateConstant.IS_ARTIST_ID.equals(type)){//显示艺术家里面的歌曲信息
			track_play_title.setText(intent.getStringExtra("artist"));
			half_artist_name.setText(intent.getStringExtra("artist"));
			half_artist_name.setTextSize(20);
			id = Integer.parseInt(intent.getStringExtra("artistId"));
			loadImage(half_artist_image,(int)id,AppStateConstant.IS_ARTIST_ID);
			adapter = new SongListItemAdapter(this,MusicDao.getMp3InfosByArtistID(this, id));
			list_view.setAdapter(adapter);
			list_view.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View v,
						int arg2, long arg3) {
					int position = Integer.parseInt(((TextView)v.findViewById(R.id.listview_item_position)).getText().toString().trim());
					Intent intent = new Intent();
					intent.setAction("PLAY_SERVICE_ACTION");
					intent.putExtra("listPosition", position);
					intent.putExtra("collectType", AppStateConstant.IS_ARTIST_ID);
					intent.putExtra("collectId", (int)id);
					intent.putExtra("MSG", AppStateConstant.PLAY_MSG);
					sendBroadcast(intent);					
				}
			});
		}else if(AppStateConstant.IS_PLAYLIST_ID.equals(type)){//显示播放列表里面的歌曲信息
			track_play_title.setText(intent.getStringExtra("playList"));
			half_artist_name.setText(intent.getStringExtra("playList"));
			half_artist_name.setTextSize(20);
			id = Integer.parseInt(intent.getStringExtra("playListId"));
			loadImage(half_artist_image,(int)id,AppStateConstant.IS_ARTIST_ID);
			adapter = new PlayListSongItemAdapter(this,MusicDao.getMp3InfosByPlayListId(this, id),id);
			list_view.setAdapter(adapter);
			list_view.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View v,
						int arg2, long arg3) {
					int position = Integer.parseInt(((TextView)v.findViewById(R.id.listview_item_position)).getText().toString().trim());
					Intent intent = new Intent();
					intent.setAction("PLAY_SERVICE_ACTION");
					intent.putExtra("listPosition", position);
					intent.putExtra("collectType", AppStateConstant.IS_PLAYLIST_ID);
					intent.putExtra("collectId", (int)id);
					intent.putExtra("MSG", AppStateConstant.PLAY_MSG);
					sendBroadcast(intent);					
				}
			});
		}
   }
  
  /**
   * 初始化控件类
   * */
  private void initShowActivity(){
	  anim = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
	  track_play_back = (ImageButton)findViewById(R.id.track_play_back);
	  track_play_title = (TextView)findViewById(R.id.track_play_title);
	  half_artist_image = (ImageView)findViewById(R.id.half_artist_image);
	  half_album_name = (TextView)findViewById(R.id.half_album_name);
	  half_artist_name = (TextView)findViewById(R.id.half_artist_name);
	  list_view = (ListView)findViewById(R.id.viewPager);
	  audio_player_prev = (ImageButton)findViewById(R.id.audio_player_prev);
	  audio_player_play = (ImageButton)findViewById(R.id.audio_player_play);
	  audio_player_next = (ImageButton)findViewById(R.id.audio_player_next);
	  bottom_action_bar_music_title = (TextView)findViewById(R.id.bottom_action_bar_music_title);
	  bottom_action_bar_artist_name = (TextView)findViewById(R.id.bottom_action_bar_artist_name);
	  bottom_action_bar_song_art = (ImageView)findViewById(R.id.bottom_action_bar_song_art);
  }

  /**
   * 给控件添加事件
   * */
  private void attachListeners(){
	  track_play_back.setOnClickListener(this);
	  audio_player_prev.setOnClickListener(this);
	  audio_player_play.setOnClickListener(this);
	  audio_player_next.setOnClickListener(this);
	  bottom_action_bar_song_art.setOnClickListener(this);
  }
  
  /**
   * 加载专辑图片
   * */
  private void loadImage(ImageView imageView,int id,String type){
	     new LoadImages(imageView, id ,type).execute();
  }
  /**
   * 加载歌曲图片
   * */
  private void loadSmallImage(ImageView imageView,Mp3Info mp3Info){
	     new LoadSmallImageUtil(imageView, mp3Info,this).execute();
   }
  /**
   * 重写返回键事件
   * */
  public void onBackPressed() {
	  Intent intent = new Intent();
	  intent.setClassName(this, "com.android.mplayer.MusicListActivity");
	  startActivity(intent);
      overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in );
      ExitApplicationService.getInstance().removeActivity(this);
      finish();
	}
  
  @Override
  public void onClick(View v) {
	  Intent intent = new Intent();
      switch (v.getId()) {
		  case R.id.track_play_back://返回
			    onBackPressed();
		    	break;
		  case R.id.audio_player_prev://上一曲
			    intent.setAction("PLAY_SERVICE_ACTION");
			    intent.putExtra("MSG", AppStateConstant.PRIVIOUS_MSG);
			    sendBroadcast(intent);
				break;
		  case R.id.audio_player_play://播放、暂停
			    intent.setAction("PLAY_SERVICE_ACTION");
			    if(MusicPlayService.IS_PLAYING_MSG){
			      intent.putExtra("MSG", AppStateConstant.PAUSE_MSG);
			    }else{
			      intent.putExtra("MSG", AppStateConstant.PLAY_CURRENT_MSG);
			    }
			    sendBroadcast(intent);
				break;
		  case R.id.audio_player_next://下一曲
			    intent.setAction("PLAY_SERVICE_ACTION");
			    intent.putExtra("MSG", AppStateConstant.NEXT_MSG);
			    sendBroadcast(intent);
				break;
		  case R.id.bottom_action_bar_song_art://底部控制栏专辑图片点击事件
			    intent.setClassName(this, "com.android.mplayer.MainPlayerActivity");
			    startActivity(intent);
		     	//activity切换动画
		    	overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in );
		    	ExitApplicationService.getInstance().removeActivity(this);
		    	finish();
		    	break;
		   default:
			    break;
      }
  }
  
  
  public class AlbumArtistReceiver extends BroadcastReceiver{

		public void onReceive(Context context, Intent intent) {
			//更新MusicListActivity的UI
			String msg = intent.getAction();
			if("FRESH_PLAY_LIST_ITEM_RECEIVER".equals(msg)){//当数据发生变动时刷新界面
				adapter = new PlayListSongItemAdapter(context,MusicDao.getMp3InfosByPlayListId(context, id),id);
				list_view.setAdapter(adapter);
			}else{
				//接受来自服务的广播来跟新UI
				Mp3Info mp3Info = (Mp3Info)intent.getSerializableExtra("mp3Info");
				bottom_action_bar_music_title.setText(mp3Info.getTitle());
				bottom_action_bar_artist_name.setText(mp3Info.getArtist());
				loadSmallImage(bottom_action_bar_song_art,mp3Info);
			    if(MusicPlayService.IS_PLAYING_MSG == false){
			    	audio_player_play.setImageResource(R.drawable.apollo_holo_light_play);
				}else if(MusicPlayService.IS_PLAYING_MSG == true){
				    audio_player_play.setImageResource(R.drawable.apollo_holo_light_pause);
				}
			}
		}
}
  
  /**
   * 图片异步加载类
   * 
   * **/
 class LoadImages extends AsyncTask<Void, Void, Bitmap>{
	    ImageView imageView;
	    int Id;
	    String type;
     public LoadImages(ImageView imageView,int Id,String type){
     this.imageView = imageView;
     this.Id = Id;
     this.type = type;
     }
 @Override
   protected Bitmap doInBackground(Void... params) {
	 Bitmap bitmap = null;
	    if(AppStateConstant.IS_ALBUM_ID.equals(type))
	    	bitmap = AlbumArtDao.getImageByAlbumId(AlbumArtistBroswerActivity.this,Id,false);
	    else if(AppStateConstant.IS_ARTIST_ID.equals(type))	
	    	bitmap = AlbumArtDao.getImageByArtistId(AlbumArtistBroswerActivity.this,Id,false);
	    return bitmap;
		}


	protected void onPostExecute(Bitmap result) {
     if(result!=null){
    	imageView.startAnimation(anim);
     	imageView.setImageBitmap(result);
     }
 }
}

}
