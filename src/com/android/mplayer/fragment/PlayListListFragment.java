package com.android.mplayer.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.android.mplayer.R;
import com.android.mplayer.adapter.PlayListItemAdapter;
import com.android.mplayer.constants.AppStateConstant;

public class PlayListListFragment extends Fragment {
   private static PlayListListFragment fragment = null;
   private  PlayListItemAdapter adaper;
   private MyPlayListReceiver myReceiver;
   private ListView allPlayList;
   public static PlayListListFragment newInstance(){
	   if(fragment == null){
		   fragment = new PlayListListFragment();
	   }
	   return fragment;
   }
   
   
   public void onCreate(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    myReceiver = new MyPlayListReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("FRESH_PLAYLIST_RECEIVER");
		getActivity().registerReceiver(myReceiver, filter);
   }
   
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View convertView = inflater.inflate(R.layout.music_play_list_layout, null);
		allPlayList = ((ListView) convertView.findViewById(R.id.all_playlist_list));
		adaper = new PlayListItemAdapter(getActivity());
		allPlayList.setAdapter(adaper);
		allPlayList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v,
					int arg2, long arg3) {
				Intent intent = new Intent();
				String playList = ((TextView)(v.findViewById(R.id.listview_item_playlist_name))).getText().toString().trim();
				String playListId = ((TextView)(v.findViewById(R.id.listview_item_playlist_position))).getText().toString().trim();
				intent.putExtra("playListId",playListId);
				intent.putExtra("playList",playList);
				intent.putExtra("type", AppStateConstant.IS_PLAYLIST_ID);
				intent.setClassName(getActivity(), "com.android.mplayer.AlbumArtistBroswerActivity");
				getActivity().startActivity(intent);	
				getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		return convertView;
   }
   
   
   
   public class MyPlayListReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		adaper = new PlayListItemAdapter(getActivity());
		allPlayList.setAdapter(adaper);
	  }
	   
   }
}
