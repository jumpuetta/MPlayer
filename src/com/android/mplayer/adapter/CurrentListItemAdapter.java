package com.android.mplayer.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mplayer.R;
import com.android.mplayer.domain.Mp3Info;
import com.android.mplayer.utils.LoadSmallImageUtil;

public class CurrentListItemAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private Context context;
	private List<Mp3Info> musiclist;

	public CurrentListItemAdapter(Context context,List<Mp3Info> musiclist) {
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
			convertView = mInflater.inflate(R.layout.current_song_list_items, null);
		}
		Mp3Info mp3Info = musiclist.get(position);
		((TextView)convertView.findViewById(R.id.current_item_song_name)).setText(mp3Info.getTitle());
		((TextView)convertView.findViewById(R.id.current_item_singer)).setText(mp3Info.getArtist());
		ImageView listview_item_album = (ImageView)convertView.findViewById(R.id.current_item_song_art);
		loadImage(listview_item_album,mp3Info);
		((TextView)convertView.findViewById(R.id.current_item_position)).setText(position+"");
		return convertView;
	}
	
	 private void loadImage(ImageView imageView,Mp3Info mp3Info){
		 new LoadSmallImageUtil(imageView, mp3Info,context).execute();
    }
	 
}
