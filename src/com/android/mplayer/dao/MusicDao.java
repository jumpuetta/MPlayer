package com.android.mplayer.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.android.mplayer.domain.Mp3Info;
import com.android.mplayer.utils.CursorUtils;



public class MusicDao {
	
	
	/**
	 * 用于从数据库中查询歌曲的信息，保存在List当中
	 * 
	 * @return
	 */
	public static List<Mp3Info> getAllMp3Infos(Context context) {
		
		Cursor cursor = CursorUtils.getInstance(context).getAllSongsListOrderedBySongTitle();
		
		List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			Mp3Info mp3Info = new Mp3Info();
			long id = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media._ID));	//音乐id
			String title = cursor.getString((cursor	
					.getColumnIndex(MediaStore.Audio.Media.TITLE))); // 音乐标题
			String artist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST)); // 艺术家
			String album = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ALBUM));	//专辑
			String artistId = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
			String displayName = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
			long albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
			long duration = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.DURATION)); // 时长
			long size = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
			String url = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
			int isMusic = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)); // 是否为音乐
			if (isMusic != 0) { // 只把音乐添加到集合当中
				mp3Info.setId(id);
				mp3Info.setTitle(title);
				mp3Info.setArtist(artist);
				mp3Info.setAlbum(album);
				mp3Info.setDisplayName(displayName);
				mp3Info.setAlbumId(albumId);
				mp3Info.setDuration(duration);
				mp3Info.setSize(size);
				mp3Info.setUrl(url);
				mp3Info.setArtistId(artistId);
				mp3Infos.add(mp3Info);
			}
		}
		return mp3Infos;
	}
	
	/**
	 * 通过专辑ID获取专辑里面的歌曲
	 * */
public static List<Mp3Info> getMp3InfosByAlbumID(Context context,long albumId) {
		
		Cursor cursor = CursorUtils.getInstance(context).getSongListCursorFromAlbumId(albumId);
		
		List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			Mp3Info mp3Info = new Mp3Info();
			long id = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media._ID));	//音乐id
			String title = cursor.getString((cursor	
					.getColumnIndex(MediaStore.Audio.Media.TITLE))); // 音乐标题
			String artist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST)); // 艺术家
			String album = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ALBUM));	//专辑
			String artistId = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
			String displayName = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
			long duration = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.DURATION)); // 时长
			long size = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
			String url = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
			int isMusic = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)); // 是否为音乐
			if (isMusic != 0) { // 只把音乐添加到集合当中
				mp3Info.setId(id);
				mp3Info.setTitle(title);
				mp3Info.setArtist(artist);
				mp3Info.setAlbum(album);
				mp3Info.setDisplayName(displayName);
				mp3Info.setAlbumId(albumId);
				mp3Info.setDuration(duration);
				mp3Info.setSize(size);
				mp3Info.setUrl(url);
				mp3Info.setArtistId(artistId);
				mp3Infos.add(mp3Info);
			}
		}
		return mp3Infos;
	}

/**
 * 通过艺术家ID获取该艺术家的歌曲
 * */
public static List<Mp3Info> getMp3InfosByArtistID(Context context,long artistId) {
	
	Cursor cursor = CursorUtils.getInstance(context).getSongListCursorFromArtistId(artistId);
	
	List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
	for (int i = 0; i < cursor.getCount(); i++) {
		cursor.moveToNext();
		Mp3Info mp3Info = new Mp3Info();
		long id = cursor.getLong(cursor
				.getColumnIndex(MediaStore.Audio.Media._ID));	//音乐id
		String title = cursor.getString((cursor	
				.getColumnIndex(MediaStore.Audio.Media.TITLE))); // 音乐标题
		String artist = cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Media.ARTIST)); // 艺术家
		String album = cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Media.ALBUM));	//专辑
		String albumId = cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
		String displayName = cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
		long duration = cursor.getLong(cursor
				.getColumnIndex(MediaStore.Audio.Media.DURATION)); // 时长
		long size = cursor.getLong(cursor
				.getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
		String url = cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
		int isMusic = cursor.getInt(cursor
				.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)); // 是否为音乐
		if (isMusic != 0) { // 只把音乐添加到集合当中
			mp3Info.setId(id);
			mp3Info.setTitle(title);
			mp3Info.setArtist(artist);
			mp3Info.setAlbum(album);
			mp3Info.setDisplayName(displayName);
			mp3Info.setAlbumId(Long.parseLong(albumId));
			mp3Info.setDuration(duration);
			mp3Info.setSize(size);
			mp3Info.setUrl(url);
			mp3Info.setArtistId(Long.toString(artistId));
			mp3Infos.add(mp3Info);
		}
	}
	return mp3Infos;
}

/**
 * 通过播放列表ID获取该播放列表的歌曲
 * */
public static List<Mp3Info> getMp3InfosByPlayListId(Context context,long playListId) {
	
	Cursor cursor = CursorUtils.getInstance(context).getSongListCursorFromPlayListId(playListId);
	
	List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
	for (int i = 0; i < cursor.getCount(); i++) {
		cursor.moveToNext();
		Mp3Info mp3Info = new Mp3Info();
		long id = cursor.getLong(cursor
				.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID));	//音乐id
		String title = cursor.getString((cursor	
				.getColumnIndex(MediaStore.Audio.Playlists.Members.TITLE))); // 音乐标题
		String artist = cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Playlists.Members.ARTIST)); // 艺术家
		String artistId = cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Playlists.Members.ARTIST_ID));
		String album = cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Playlists.Members.ALBUM));	//专辑
		String albumId = cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Playlists.Members.ALBUM_ID));
		long duration = cursor.getLong(cursor
				.getColumnIndex(MediaStore.Audio.Playlists.Members.DURATION)); // 时长
		long size = cursor.getLong(cursor
				.getColumnIndex(MediaStore.Audio.Playlists.Members.SIZE)); // 文件大小
		String displayName = cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Playlists.Members.DISPLAY_NAME));
		String url = cursor.getString(cursor
				.getColumnIndex(MediaStore.Audio.Playlists.Members.DATA));// 文件路径
		int isMusic = cursor.getInt(cursor
				.getColumnIndex(MediaStore.Audio.Playlists.Members.IS_MUSIC)); // 是否为音乐
		if (isMusic != 0) { // 只把音乐添加到集合当中
			mp3Info.setId(id);
			mp3Info.setTitle(title);
			mp3Info.setArtist(artist);
			mp3Info.setAlbum(album);
			mp3Info.setDisplayName(displayName);
			mp3Info.setAlbumId(Long.parseLong(albumId));
			mp3Info.setDuration(duration);
			mp3Info.setSize(size);
			mp3Info.setUrl(url);
			mp3Info.setArtistId(artistId);
			mp3Infos.add(mp3Info);
		}
	}
	return mp3Infos;
}
	
	
	/**
	 * 格式化时间，将毫秒转换为分:秒格式
	 * @param time
	 * @return
	 */
	public static String formatTime(long time) {
		String min = time / (1000 * 60) + "";
		String sec = time % (1000 * 60) + "";
		if (min.length() < 2) {
			min = "0" + time / (1000 * 60) + "";
		} else {
			min = time / (1000 * 60) + "";
		}
		if (sec.length() == 4) {
			sec = "0" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 3) {
			sec = "00" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 2) {
			sec = "000" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 1) {
			sec = "0000" + (time % (1000 * 60)) + "";
		}
		return min + ":" + sec.trim().substring(0, 2);
	}
	
	
	
	
}
