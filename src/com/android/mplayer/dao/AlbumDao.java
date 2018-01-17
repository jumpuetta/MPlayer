package com.android.mplayer.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.android.mplayer.domain.AlbumInfo;
import com.android.mplayer.utils.CursorUtils;

public class AlbumDao {
   public static List<AlbumInfo> getAllAlbum(Context context){
	   Cursor cursor = CursorUtils.getInstance(context).getAllAlbumCursor();
	   List<AlbumInfo> albumInfos = new ArrayList<AlbumInfo>();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			AlbumInfo albumInfo = new AlbumInfo();
			long id = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Albums._ID));	
			String album = cursor.getString((cursor	
					.getColumnIndex(MediaStore.Audio.Albums.ALBUM))); 
			String artist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Albums.ARTIST)); 
			String album_art = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));	
			String last_year = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Albums.LAST_YEAR));
			String countSong = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
			albumInfo.setAlbumId(id);
			albumInfo.setAlbum(album);
			albumInfo.setArtist(artist);
			albumInfo.setAlbum_art(album_art);
			albumInfo.setCountSong(countSong);
			albumInfo.setLast_year(last_year);
			albumInfos.add(albumInfo);
		}
		return albumInfos;
   }
   
   public static List<AlbumInfo> getAlbumByArtistId(Context context,int artistId){
	   Cursor cursor = CursorUtils.getInstance(context).getAlbumListFromArtistId(artistId);
	   List<AlbumInfo> albumInfos = new ArrayList<AlbumInfo>();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			AlbumInfo albumInfo = new AlbumInfo();
			long id = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Albums._ID));	
			String album = cursor.getString((cursor	
					.getColumnIndex(MediaStore.Audio.Albums.ALBUM))); 
			String artist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Albums.ARTIST)); 
			String album_art = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));	
			String last_year = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Albums.LAST_YEAR));
			String countSong = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
			albumInfo.setAlbumId(id);
			albumInfo.setAlbum(album);
			albumInfo.setArtist(artist);
			albumInfo.setAlbum_art(album_art);
			albumInfo.setCountSong(countSong);
			albumInfo.setLast_year(last_year);
			albumInfos.add(albumInfo);
		}
		return albumInfos;
   }
   
   public static AlbumInfo getAlbumByAlbumId(Context context,int id){
	   Cursor cursor = CursorUtils.getInstance(context).getAlbumCursorByAlbumId(id);
			cursor.moveToNext();
			AlbumInfo albumInfo = new AlbumInfo();
			String album = cursor.getString((cursor	
					.getColumnIndex(MediaStore.Audio.Albums.ALBUM))); 
			String artist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Albums.ARTIST)); 
			String album_art = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));	
			String last_year = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Albums.LAST_YEAR));
			String countSong = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
			albumInfo.setAlbumId(id);
			albumInfo.setAlbum(album);
			albumInfo.setArtist(artist);
			albumInfo.setAlbum_art(album_art);
			albumInfo.setCountSong(countSong);
			albumInfo.setLast_year(last_year);
		    return albumInfo;
   }
   


}
