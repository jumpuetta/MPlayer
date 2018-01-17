package com.android.mplayer.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.android.mplayer.domain.Mp3Info;
import com.android.mplayer.domain.PlayListInfo;
import com.android.mplayer.utils.CursorUtils;

public class PlayListDao {
   private static ContentValues[] sContentValuesCache;
   public static List<PlayListInfo> getAllPlayList(Context context){
	   Cursor cursor = CursorUtils.getInstance(context).getAllPlayListsCursor();
	   List<PlayListInfo> playListInfos = new ArrayList<PlayListInfo>();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			PlayListInfo playListInfo = new PlayListInfo();
			long id = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Playlists._ID));	
			String playList = cursor.getString((cursor	
					.getColumnIndex(MediaStore.Audio.Playlists.NAME))); 
			playListInfo.setListId(id);
			playListInfo.setPlayList(playList);
			playListInfos.add(playListInfo);
		}
		return playListInfos;
   }
   
   public static PlayListInfo getPlayListByListId(Context context,int id){
	   Cursor cursor = CursorUtils.getInstance(context).getPlayListsCursorByListId(id);
			cursor.moveToNext();
			PlayListInfo playListInfo = new PlayListInfo();
			String playList = cursor.getString((cursor	
					.getColumnIndex(MediaStore.Audio.Playlists.NAME))); 
			playListInfo.setListId(id);
			playListInfo.setPlayList(playList);
		return playListInfo;
   }
   
   
   private static void makeInsertItems(long[] ids, int offset, int len, int base) {
       if (offset + len > ids.length) {
           len = ids.length - offset;
       }
       if (sContentValuesCache == null || sContentValuesCache.length != len) {
           sContentValuesCache = new ContentValues[len];
       }
       for (int i = 0; i < len; i++) {
           if (sContentValuesCache[i] == null) {
               sContentValuesCache[i] = new ContentValues();
           }

           sContentValuesCache[i].put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, base + offset + i);
           sContentValuesCache[i].put(MediaStore.Audio.Playlists.Members.AUDIO_ID, ids[offset + i]);
       }
   }
   
   public static int addToPlaylist(Context context, long [] ids, long playlistid) {
       List<Mp3Info> mp3Infos = MusicDao.getMp3InfosByPlayListId(context, playlistid);
	   for (Mp3Info mp3Info : mp3Infos) {
		   if(ids[0] == mp3Info.getId())
		      return 0;
	   }
	   if (ids == null) {
    	   return -1;
       } else {
           int size = ids.length;
           ContentResolver resolver = context.getContentResolver();
           String[] cols = new String[] {
                   "count(*)"
           };
           Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistid);
           Cursor cur = resolver.query(uri, cols, null, null, null);
           cur.moveToFirst();
           int base = cur.getInt(0);
           cur.close();
           int numinserted = 0;
           for (int i = 0; i < size; i += 1000) {
               makeInsertItems(ids, i, 1000, base);
               numinserted += resolver.bulkInsert(uri, sContentValuesCache);
           }
           return numinserted;
       }
   }
   
   public static int deleteSongFromPlaylist(Context context, long  ids, long playlistid) {
           ContentResolver resolver = context.getContentResolver();
           Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistid);
           return  resolver.delete(uri, MediaStore.Audio.Playlists.Members.AUDIO_ID + "=?", new String[]{ids+""});
   }
   
   public static boolean clearPlaylist(Context context,long plid) {
	   return CursorUtils.getInstance(context).clearPlaylist(plid);
   }
   
   public static boolean createPlayList(Context context,String name){
	   return CursorUtils.getInstance(context).createPlaylist(name);
   }
   
   public static boolean deletePlayList(Context context,long playlistId){
	   return CursorUtils.getInstance(context).deletePlaylist(playlistId);
   }
   
}
