package com.android.mplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mplayer.R;

public class PlayerLrcShowFragment extends Fragment {
   private static PlayerLrcShowFragment fragment = null;
   public static PlayerLrcShowFragment newInstance(){
	   if(fragment == null){
		   fragment = new PlayerLrcShowFragment();
	   }
	   return fragment;
   }
   
   public void onCreate(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
   }
   
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View convertView =  inflater.inflate(R.layout.music_lyric , null);
       return convertView;
   }
}
