package com.android.mplayer.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.android.mplayer.R;
import com.android.mplayer.dao.PlayListDao;

public class SongOuttoPlayListListener implements OnClickListener{
    private  Context context;
    private long mp3Id;
    private long playListId;
    public SongOuttoPlayListListener(Context context,long mp3Id,long playListId){
    	this.context = context;
    	this.mp3Id = mp3Id;
    	this.playListId = playListId;
    }
	@Override
	public void onClick(View v) {
		new AlertDialog.Builder(context)  
		.setTitle(context.getString(R.string.warn))  
		.setMessage(context.getString(R.string.remove_song_info))  
        .setPositiveButton(context.getString(R.string.bt_ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				int count = PlayListDao.deleteSongFromPlaylist(context, mp3Id, playListId);
				Intent intent = new Intent();
			    intent.setAction("FRESH_PLAY_LIST_ITEM_RECEIVER");
			    context.sendBroadcast(intent);
				Toast.makeText(context, count+context.getString(R.string.remove_song_success) , Toast.LENGTH_SHORT).show();
			}
		})
        .setNeutralButton(context.getString(R.string.bt_cancel), null)
		.show();  
	}

}
