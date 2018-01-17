package com.android.mplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.mplayer.R;

public class PlayerAlbumFragment extends Fragment {
   private static PlayerAlbumFragment fragment = null;
   private ImageView iv_music_ablum;
   public static PlayerAlbumFragment newInstance(){
	   if(fragment == null){
		   fragment = new PlayerAlbumFragment();
	   }
	   return fragment;
   }
   
   public void onCreate(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
   }
   
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View convertView = inflater.inflate(R.layout.music_album, null);
		iv_music_ablum = (ImageView) convertView
				.findViewById(R.id.iv_music_ablum);
		iv_music_ablum
				.setImageResource(R.drawable.bg_ablumlayout_0);
       return convertView;
   }
}
