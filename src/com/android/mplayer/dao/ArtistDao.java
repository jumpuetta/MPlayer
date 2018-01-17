package com.android.mplayer.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.android.mplayer.domain.ArtistInfo;
import com.android.mplayer.utils.CursorUtils;

public class ArtistDao {
   public static List<ArtistInfo> getAllArtist(Context context){
	   Cursor cursor = CursorUtils.getInstance(context).getAllArtistCursor();
	   List<ArtistInfo> artistInfos = new ArrayList<ArtistInfo>();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			ArtistInfo artistInfo = new ArtistInfo();
			long id = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Artists._ID));	
			String artist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Artists.ARTIST)); 
			artistInfo.setArtistId(id);
			artistInfo.setArtist(artist);
			artistInfos.add(artistInfo);
		}
		return artistInfos;
   }
   
   public static ArtistInfo getArtistByArtistId(Context context,int id){
	        Cursor cursor = CursorUtils.getInstance(context).getArtistCursorByArtistId(id);
			cursor.moveToNext();
			ArtistInfo artistInfo = new ArtistInfo();
			String artist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Artists.ARTIST)); 
			artistInfo.setArtistId(id);
			artistInfo.setArtist(artist);
		    return artistInfo;
   }
   
}
