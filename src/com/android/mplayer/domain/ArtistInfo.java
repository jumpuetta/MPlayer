package com.android.mplayer.domain;


public class ArtistInfo{
	private long artistId;
	private String artist;
	public ArtistInfo() {
		super();
	}
	public ArtistInfo(long artistId, String artist) {
		super();
		this.artistId = artistId;
		this.artist = artist;
	}
	public long getArtistId() {
		return artistId;
	}
	public void setArtistId(long artistId) {
		this.artistId = artistId;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	
	
}