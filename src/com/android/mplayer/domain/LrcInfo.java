package com.android.mplayer.domain;

import java.io.Serializable;


public class LrcInfo implements Serializable{
	private String lrcStr; // 歌词内容
	private int lrcTime; // 歌词当前时间

	public String getLrcStr() {
		return lrcStr;
	}

	public void setLrcStr(String lrcStr) {
		this.lrcStr = lrcStr;
	}

	public int getLrcTime() {
		return lrcTime;
	}

	public void setLrcTime(int lrcTime) {
		this.lrcTime = lrcTime;
	}
}
