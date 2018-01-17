package com.android.mplayer.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.mplayer.R;
import com.android.mplayer.fragment.AlbumListFragment;
import com.android.mplayer.fragment.ArtistListFragment;
import com.android.mplayer.fragment.MusicOnlineFragment;
import com.android.mplayer.fragment.PlayListListFragment;
import com.android.mplayer.fragment.SongListFragment;

public class MusicListAdapter extends FragmentPagerAdapter  {

	private Context context;
	private final String[] names;
	public MusicListAdapter(FragmentManager fm,Context context) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.context = context;
		names = this.context.getResources().
					getStringArray(R.array.list_show_array);
	}
	

	

	@Override
	public int getCount() {
		return names.length;
	}

	@Override
	public Fragment getItem(int position) {
		 Fragment f = null;
		 if (position == 0 ) {
			 f = SongListFragment.newInstance();
		 }else if (position == 1 ) {
			 f = AlbumListFragment.newInstance();
		 }else if (position == 2 ) {
			 f = ArtistListFragment.newInstance();
		 }else if (position == 3 ) {
			 f = PlayListListFragment.newInstance();
		 }else if (position == 4 ) {
			 f = MusicOnlineFragment.newInstance();
		 }
		 return f;
	}

	 public CharSequence getPageTitle(int position) {
         return names[position];
     }

}
