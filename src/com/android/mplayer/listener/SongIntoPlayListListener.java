package com.android.mplayer.listener;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.android.mplayer.R;
import com.android.mplayer.dao.PlayListDao;
import com.android.mplayer.domain.PlayListInfo;

public class SongIntoPlayListListener implements OnClickListener{
    private List<PlayListInfo>  playListInfos;
    private Context context;
    private long mp3Id;
    public SongIntoPlayListListener(Context context,long mp3Id){
    	this.context = context;
    	this.mp3Id = mp3Id;
    }
	@Override
	public void onClick(View v) {
		playListInfos = PlayListDao.getAllPlayList(context);
		String [] itemsTitle = new String[playListInfos.size()+1]; 
		for (int i=0;i<playListInfos.size();i++) {
			itemsTitle[i] = playListInfos.get(i).getPlayList();
		}
		itemsTitle[playListInfos.size()] = context.getString(R.string.add_new_playlist);
		new AlertDialog.Builder(context)  
		.setTitle(context.getString(R.string.where_add_song))  
		.setItems(itemsTitle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				long  songId =  mp3Id;
				if(which == playListInfos.size()){
					dialog.cancel();
					EditText listName = new EditText(context);
					new AlertDialog.Builder(context)  
					.setTitle(context.getString(R.string.input_new_list_name))  
					.setIcon(android.R.drawable.ic_dialog_info)  
					.setView(listName)  
					.setPositiveButton(context.getString(R.string.bt_ok), new AddPlayListListener(context, listName,mp3Id+""))  
					.setNegativeButton(context.getString(R.string.bt_cancel), null)  
					.show();  
				}else{
					int  playlistId = (int) playListInfos.get(which).getListId();
					int count = PlayListDao.addToPlaylist(context, new long[]{songId} , playlistId);
					switch (count) {
					case 0:
						Toast.makeText(context,  context.getString(R.string.add_repeat_song_info) , Toast.LENGTH_SHORT).show();
						break;
					case 1:
						Toast.makeText(context, count + context.getString(R.string.add_song_into_list_success) , Toast.LENGTH_SHORT).show();
						break;
					case -1:
						Toast.makeText(context, context.getString(R.string.add__song_error) , Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
				  }
					dialog.cancel();
				}
			}
		}) 
		.show();  
	}

}
