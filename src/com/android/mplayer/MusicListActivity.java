package com.android.mplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mplayer.adapter.MusicListAdapter;
import com.android.mplayer.constants.AppStateConstant;
import com.android.mplayer.domain.Mp3Info;
import com.android.mplayer.service.ExitApplicationService;
import com.android.mplayer.service.MusicPlayService;
import com.android.mplayer.utils.LoadSmallImageUtil;
import com.viewpagerindicator.TabPageIndicator;

public class MusicListActivity extends FragmentActivity implements OnClickListener{

	private ViewPager viewPager;
	private ImageButton bt_meunlist_to_play;
	private ImageButton audio_player_prev;
	private ImageButton audio_player_play;
	private ImageButton audio_player_next;
	private TextView bottom_action_bar_music_title;
	private TextView bottom_action_bar_artist_name;
	private ImageView bottom_action_bar_song_art;
	private MusicListReceiver myReceiver;
    private LinearLayout miniControll;
	private Animation anim;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.music_list_main);
        initShowActivity();
        attachListeners();

		myReceiver = new MusicListReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("MUSIC_SERVICE_UPDATEUI_RECEIVER");
		filter.addAction("MUSIC_SERVICE_INIT_BOTTOM_RECEIVER");
		registerReceiver(myReceiver, filter);
		
		ExitApplicationService.getInstance().addActivity(this);
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	/**
	 * 初始化小控制台
	 * */
	@Override
    protected void onResume() {
	    super.onResume();
	    Intent intent = new Intent();
        intent.setAction("PLAY_SERVICE_ACTION");
        intent.putExtra("MSG", AppStateConstant.INIT_BOTTOM_CALL_BACK_INFO);
        sendBroadcast(intent);
    }
    
	/**
	 * 初始化控件
	 * */
    private void initShowActivity(){
    	bt_meunlist_to_play = (ImageButton)findViewById(R.id.bt_meunlist_to_play);
		
    	miniControll = (LinearLayout)findViewById(R.id.mini_controll);
    	audio_player_prev = (ImageButton)findViewById(R.id.audio_player_prev);
		audio_player_play = (ImageButton)findViewById(R.id.audio_player_play);
		audio_player_next = (ImageButton)findViewById(R.id.audio_player_next);
		bottom_action_bar_music_title = (TextView)findViewById(R.id.bottom_action_bar_music_title);
		bottom_action_bar_artist_name = (TextView)findViewById(R.id.bottom_action_bar_artist_name);
		bottom_action_bar_song_art = (ImageView)findViewById(R.id.bottom_action_bar_song_art);
		
		MusicListAdapter adapter = new MusicListAdapter(getSupportFragmentManager(),this);
		viewPager = (ViewPager)findViewById(R.id.list_viewpager);
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(2);
		
	    TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.list_indicator);
	    indicator.setViewPager(viewPager);
	    indicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int arg0) {
				if(arg0 == 4){
					anim = AnimationUtils.loadAnimation(MusicListActivity.this, R.anim.trans_out_bottom);
					miniControll.startAnimation(anim);
					miniControll.setVisibility(View.GONE);
				}else if(miniControll.getVisibility() == View.GONE){
					anim = AnimationUtils.loadAnimation(MusicListActivity.this, R.anim.trans_in_bottom);
					miniControll.startAnimation(anim);
					miniControll.setVisibility(View.VISIBLE);
				}
			}
		});
    }
	 
    /**
     * 给按钮添加事件
     * */
    private void attachListeners(){
    	bt_meunlist_to_play.setOnClickListener(this);
		audio_player_prev.setOnClickListener(this);
		audio_player_play.setOnClickListener(this);
		audio_player_next.setOnClickListener(this);
		bottom_action_bar_song_art.setOnClickListener(this);
    }
	
    
    
    
	public LinearLayout getMiniControll() {
		return miniControll;
	}

	@Override
	public void onClick(View v) {
		 Intent intent = new Intent();
         switch (v.getId()) {
		  case R.id.bt_meunlist_to_play:
			    onBackPressed();
			    break;
		  case R.id.audio_player_prev:
			    intent.setAction("PLAY_SERVICE_ACTION");
			    intent.putExtra("MSG", AppStateConstant.PRIVIOUS_MSG);
			    sendBroadcast(intent);
				break;
		  case R.id.audio_player_play:
			    intent.setAction("PLAY_SERVICE_ACTION");
			    if(MusicPlayService.IS_PLAYING_MSG){
			      intent.putExtra("MSG", AppStateConstant.PAUSE_MSG);
			    }else{
			      intent.putExtra("MSG", AppStateConstant.PLAY_CURRENT_MSG);
			    }
			    sendBroadcast(intent);
				break;
		  case R.id.audio_player_next:
			    intent.setAction("PLAY_SERVICE_ACTION");
			    intent.putExtra("MSG", AppStateConstant.NEXT_MSG);
			    sendBroadcast(intent);
				break;
		  case R.id.bottom_action_bar_song_art:
			    intent.setClassName(this, "com.android.mplayer.MainPlayerActivity");
			    startActivity(intent);
		     	//activity切换动画
		    	overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in );
			    break;
		default:
			break;
		}	
         
	}
	 
	  public void onBackPressed() {
		  Intent intent = new Intent();
		  intent.setClassName(this, "com.android.mplayer.MainPlayerActivity");
		  startActivity(intent);
	      overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in );
	  }
	
	  /**
	   * 接受来自服务返回过来的广播，初始化界面内容
	   * */
	 public class MusicListReceiver extends BroadcastReceiver{

			public void onReceive(Context context, Intent intent) {
				//更新MusicListActivity的UI
		        	Mp3Info mp3Info = (Mp3Info)intent.getSerializableExtra("mp3Info");
					bottom_action_bar_music_title.setText(mp3Info.getTitle());
					bottom_action_bar_artist_name.setText(mp3Info.getArtist());
					loadImage(bottom_action_bar_song_art,mp3Info);
				    if(MusicPlayService.IS_PLAYING_MSG == false){
				    	audio_player_play.setImageResource(R.drawable.apollo_holo_light_play);
					}else if(MusicPlayService.IS_PLAYING_MSG == true){
					    audio_player_play.setImageResource(R.drawable.apollo_holo_light_pause);
					}
			}
     	 
      }
	
	  private void loadImage(ImageView imageView,Mp3Info mp3Info){
	     new LoadSmallImageUtil(imageView, mp3Info,this).execute();
      }


}