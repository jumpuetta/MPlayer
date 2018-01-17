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
import com.android.mplayer.dao.PlayListDao;
import com.android.mplayer.domain.PlayListInfo;
import com.android.mplayer.listener.ControlPlayListListener;

public class PlayListItemAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context context;
	private List<PlayListInfo> palyListList;

	public PlayListItemAdapter(Context context) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        palyListList = PlayListDao.getAllPlayList(context);
	}

	@Override
	public int getCount() {
		return palyListList.size();
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
			convertView = mInflater.inflate(R.layout.music_play_list_items, null);
		}
		((TextView)convertView.findViewById(R.id.listview_item_playlist_name)).setText(palyListList.get(position).getPlayList());
		((TextView)convertView.findViewById(R.id.listview_item_playlist_position)).setText(palyListList.get(position).getListId()+"");
        
		ImageView quick_context_tip = (ImageView)convertView.findViewById(R.id.quick_context_tip);
		quick_context_tip.setOnClickListener(new ControlPlayListListener(context,palyListList.get(position).getListId()));
		return convertView;
	 }
  }
