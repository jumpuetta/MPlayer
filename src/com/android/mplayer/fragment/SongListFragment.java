package com.android.mplayer.fragment;

import android.content.Intent;
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
import com.android.mplayer.adapter.SongListItemAdapter;
import com.android.mplayer.constants.AppStateConstant;
import com.android.mplayer.dao.MusicDao;

public class SongListFragment extends Fragment {
   private static SongListFragment fragment = null;
   
   public static SongListFragment newInstance(){
	   if(fragment == null){
		   fragment = new SongListFragment();
	   }
	   return fragment;
   }
   
   public void onCreate(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
   }
   
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	   View convertView = inflater.inflate(R.layout.music_song_list_layout, null);
	 		ListView allMusic = ((ListView) convertView.findViewById(R.id.allmusic_list));
	 		allMusic.setAdapter(new SongListItemAdapter(getActivity(),MusicDao.getAllMp3Infos(getActivity())));
	 		allMusic.setOnItemClickListener(new OnItemClickListener() {
	 			public void onItemClick(AdapterView<?> arg0, View v,
	 					int arg2, long arg3) {
	 				int position = Integer.parseInt(((TextView)v.findViewById(R.id.listview_item_position)).getText().toString().trim());
	 				Intent intent = new Intent();
	 				intent.setAction("PLAY_SERVICE_ACTION");
	 				intent.putExtra("listPosition", position);
	 				intent.putExtra("collectType", AppStateConstant.IS_ALLSONG_ID);
	 				intent.putExtra("collectId", -1000);
	 				intent.putExtra("MSG", AppStateConstant.PLAY_MSG);
	 				getActivity().sendBroadcast(intent);					
	 			}
	 		});
	 		return convertView;
   }
}
