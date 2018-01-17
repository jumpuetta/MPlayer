package com.android.mplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.mplayer.fragment.PlayerAlbumFragment;
import com.android.mplayer.fragment.PlayerLrcShowFragment;

public class PlayerContentAdapter extends FragmentPagerAdapter {
	public PlayerContentAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}



	@Override
	public int getCount() {
		return 2;
	}

	

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		if(position == 0){
			fragment = PlayerAlbumFragment.newInstance();
		}else if(position == 1){
			fragment = PlayerLrcShowFragment.newInstance();
		}
		return fragment;
	}

}
