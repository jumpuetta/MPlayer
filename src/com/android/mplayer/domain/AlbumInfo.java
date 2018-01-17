package com.android.mplayer.domain;


public class AlbumInfo{
	private String album; // 专辑 7
	private long albumId;//专辑ID 6
	private String artist; // 歌手名称 2
    private String album_art;
    private String last_year;
    private String countSong;
	public AlbumInfo() {
		super();
	}
	
	
	public AlbumInfo(String album, long albumId, String artist,
			String album_art, String last_year, String countSong) {
		super();
		this.album = album;
		this.albumId = albumId;
		this.artist = artist;
		this.album_art = album_art;
		this.last_year = last_year;
		this.countSong = countSong;
	}


	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public long getAlbumId() {
		return albumId;
	}
	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getAlbum_art() {
		return album_art;
	}
	public void setAlbum_art(String album_art) {
		this.album_art = album_art;
	}
	public String getLast_year() {
		return last_year;
	}
	public void setLast_year(String last_year) {
		this.last_year = last_year;
	}
	public String getCountSong() {
		return countSong;
	}
	public void setCountSong(String countSong) {
		this.countSong = countSong;
	}
	
	
}