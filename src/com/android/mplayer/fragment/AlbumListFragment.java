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
import com.android.mplayer.adapter.AlbumListItemAdapter;
import com.android.mplayer.constants.AppStateConstant;

public class AlbumListFragment extends Fragment {
   private static AlbumListFragment fragment = null;
   
   public static AlbumListFragment newInstance(){
	   if(fragment == null){
		   fragment = new AlbumListFragment();
	   }
	   return fragment;
   }
   
   public void onCreate(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
   }
   
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View convertView = inflater.inflate(R.layout.music_album_artist_list_layout, null);
		GridView allAlbum = ((GridView) convertView.findViewById(R.id.all_album_gridview));
		allAlbum.setAdapter(new AlbumListItemAdapter(getActivity()));
		allAlbum.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v,
					int arg2, long arg3) {
				Intent intent = new Intent();
				String albumId = ((TextView)(v.findViewById(R.id.listview_item_album_id))).getText().toString().trim();
				String album = ((TextView)(v.findViewById(R.id.listview_item_album_name))).getText().toString().trim();
				String artist = ((TextView)(v.findViewById(R.id.listview_item_album_artist))).getText().toString().trim();
				intent.putExtra("albumId",albumId);
				intent.putExtra("album",album);
				intent.putExtra("artist",artist);
				intent.putExtra("type", AppStateConstant.IS_ALBUM_ID);
				intent.setClassName(getActivity(), "com.android.mplayer.AlbumArtistBroswerActivity");
				getActivity().startActivity(intent);	
				getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		return convertView;
   }
}
