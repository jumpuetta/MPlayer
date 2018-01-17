package com.android.mplayer.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.android.mplayer.R;
import com.android.mplayer.dao.PlayListDao;

public class ControlPlayListListener implements OnClickListener{
    private Context context;
    private long playListId;
    public ControlPlayListListener(Context context,long playListId){
    	this.context = context;
    	this.playListId = playListId;
    }
	@Override
	public void onClick(View v) {
		String []item =  new String[]{
				context.getString(R.string.clear_playlist),
				context.getString(R.string.delete_playlist),
				context.getString(R.string.create_playlist)
		};
		new AlertDialog.Builder(context)  
		.setTitle(context.getString(R.string.manipulate))  
		.setItems(item, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					if(PlayListDao.clearPlaylist(context, playListId))
					   Toast.makeText(context,  context.getString(R.string.clear_list_success) , Toast.LENGTH_SHORT).show();
					else
					   Toast.makeText(context,  context.getString(R.string.clear_list_error) , Toast.LENGTH_SHORT).show();
					break;
				case 1:
					if(PlayListDao.deletePlayList(context, playListId)){
					   Toast.makeText(context, context.getString(R.string.delete_list_success) , Toast.LENGTH_SHORT).show();
				       Intent intent = new Intent();
				       intent.setAction("FRESH_PLAYLIST_RECEIVER");
				       context.sendBroadcast(intent);
					}else
					   Toast.makeText(context, context.getString(R.string.delete_list_error) , Toast.LENGTH_SHORT).show();
					break;
				case 2:
					dialog.cancel();
					EditText listName = new EditText(context);
					new AlertDialog.Builder(context)  
					.setTitle(context.getString(R.string.input_new_list_name))  
					.setIcon(android.R.drawable.ic_dialog_info)  
					.setView(listName)  
					.setPositiveButton(context.getString(R.string.bt_ok), new AddPlayListListener(context, listName,null))  
					.setNegativeButton(context.getString(R.string.bt_cancel), null)  
					.show();  
					break;

				default:
					dialog.cancel();
					break;
				}
				
				dialog.cancel();
			}
		}) 
		.show();  
	}

}
