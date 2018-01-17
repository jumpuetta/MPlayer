package com.android.mplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.android.mplayer.R;
import com.android.mplayer.adapter.ArtistListItemAdapter;
import com.android.mplayer.constants.AppStateConstant;

public class ArtistListFragment extends Fragment {
   private static ArtistListFragment fragment = null;
   
   public static ArtistListFragment newInstance(){
	   if(fragment == null){
		   fragment = new ArtistListFragment();
	   }
	   return fragment;
   }
   
   public void onCreate(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
   }
   
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View convertView = inflater.inflate(R.layout.music_album_artist_list_layout, null);
		GridView allArtist = ((GridView) convertView.findViewById(R.id.all_album_gridview));
		allArtist.setAdapter(new ArtistListItemAdapter(getActivity()));
		allArtist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v,
					int arg2, long arg3) {
				Intent intent = new Intent();
				String artist = ((TextView)(v.findViewById(R.id.listview_item_artist_name))).getText().toString().trim();
				String artistId = ((TextView)(v.findViewById(R.id.listview_item_artist_id))).getText().toString().trim();
				intent.putExtra("artistId",artistId);
				intent.putExtra("artist",artist);
				intent.putExtra("type", AppStateConstant.IS_ARTIST_ID);
				intent.setClassName(getActivity(), "com.android.mplayer.AlbumArtistBroswerActivity");
				getActivity().startActivity(intent);	
				 getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		return convertView;
   }
}
