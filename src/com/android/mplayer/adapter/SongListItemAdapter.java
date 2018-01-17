package com.android.mplayer.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.mplayer.R;
import com.android.mplayer.domain.Mp3Info;
import com.android.mplayer.listener.SongIntoPlayListListener;
import com.android.mplayer.utils.LoadSmallImageUtil;

public class SongListItemAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private Context context;
	private List<Mp3Info> musiclist;
    private ListView listView;
	
	public SongListItemAdapter(Context context,List<Mp3Info> musiclist) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.musiclist = musiclist;
	}

	@Override
	public int getCount() {
		return musiclist.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.music_song_list_items, null);
		}
		Mp3Info mp3Info = musiclist.get(position);
		((TextView)convertView.findViewById(R.id.listview_item_song_name)).setText(mp3Info.getTitle());
		((TextView)convertView.findViewById(R.id.listview_item_singer)).setText(mp3Info.getArtist());
		ImageView listview_item_album = (ImageView)convertView.findViewById(R.id.listview_item_song_art);
		loadImage(listview_item_album,mp3Info);
		((TextView)convertView.findViewById(R.id.listview_item_position)).setText(position+"");
		ImageView quick_context_tip = (ImageView)convertView.findViewById(R.id.quick_context_tip);
		
		quick_context_tip.setOnClickListener(new SongIntoPlayListListener(context,mp3Info.getId()));
		return convertView;
	}
	
	 private void loadImage(ImageView imageView,Mp3Info mp3Info){
		 new LoadSmallImageUtil(imageView, mp3Info,context).execute();
    }

}
