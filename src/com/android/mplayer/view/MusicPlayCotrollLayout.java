package com.android.mplayer.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.mplayer.R;

/**
 * 主播放器界面 控制台布局类
 * @author JIANGPENG
 * */
public class MusicPlayCotrollLayout extends LinearLayout {
	private LinearLayout ll_controll;
	private Button bt_play_mode;
	private Button bt_play;
	private Button bt_play_previous;
	private Button bt_play_next;
	private Button bt_play_meun;
	private int mode = 3;

//	public MusicPlayCotrollLayout(Context context, AttributeSet attrs,
//			int defStyle) {
//		super(context, attrs, defStyle);
//		// TODO Auto-generated constructor stub
//	}

	public MusicPlayCotrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.music_play_controll,
				this, true);
		init(context);
	}

	public MusicPlayCotrollLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * 对布局文件中的空间进行初始化
	 * @author JIANGPENG
	 * */
	public void init(Context context) {
		ll_controll = (LinearLayout) findViewById(R.id.ll_controll);
		bt_play_mode = (Button) findViewById(R.id.bt_playmode);
		bt_play = (Button) findViewById(R.id.bt_play);
		bt_play_previous = (Button) findViewById(R.id.bt_playprevious);
		bt_play_next = (Button) findViewById(R.id.bt_playnext);
		bt_play_meun = (Button) findViewById(R.id.bt_play_to_meun);

		DisplayMetrics metric = new DisplayMetrics();
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(metric);
		int windowsHeight = metric.heightPixels;
		int windowsWight = metric.widthPixels;

		LayoutParams params = (LayoutParams) ll_controll.getLayoutParams();
		params.height = windowsHeight / 8;
		params.weight = windowsWight;
		ll_controll.setLayoutParams(params);

		Drawable drawable = getResources().getDrawable(
				R.drawable.selector_play_previous);
		bt_play_previous.setBackgroundDrawable(drawable);
		drawable = getResources().getDrawable(R.drawable.selector_play_normal);
		bt_play.setBackgroundDrawable(drawable);
		drawable = getResources().getDrawable(R.drawable.selector_play_next);
		bt_play_next.setBackgroundDrawable(drawable);
		drawable = getResources().getDrawable(R.drawable.selector_play_meun);
		bt_play_meun.setBackgroundDrawable(drawable);
		drawable = getResources().getDrawable(R.drawable.selector_play_mode_normal);
		bt_play_mode.setBackgroundDrawable(drawable);

		
		
	}

}
