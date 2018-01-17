package com.android.mplayer.domain;

public class PlayListInfo {
    private long listId;
    private String playList;
	public PlayListInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PlayListInfo(long listId, String playList) {
		super();
		this.listId = listId;
		this.playList = playList;
	}
	public long getListId() {
		return listId;
	}
	public void setListId(long listId) {
		this.listId = listId;
	}
	public String getPlayList() {
		return playList;
	}
	public void setPlayList(String playList) {
		this.playList = playList;
	}
    
}
