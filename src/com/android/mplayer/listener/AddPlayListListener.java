package com.android.mplayer.listener;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.android.mplayer.R;
import com.android.mplayer.dao.PlayListDao;
import com.android.mplayer.domain.PlayListInfo;
import com.android.mplayer.utils.CursorUtils;

public class AddPlayListListener implements DialogInterface.OnClickListener{
    private Context context;
    private EditText  listName;
    private String songId;
    public AddPlayListListener(Context context,EditText listName,String songId){
    	this.context = context;
    	this.listName = listName;
    	this.songId =songId; 
    }
	@Override
	public void onClick(DialogInterface dialog, int which) {
		String name = listName.getText().toString().trim();
		List<PlayListInfo> playListInfos = PlayListDao.getAllPlayList(context);
		if(name == null||"".equals(name)){
			Toast.makeText(context, context.getString(R.string.list_name_is_null) , Toast.LENGTH_SHORT).show();
			return;
		}else{
		   for (PlayListInfo playListInfo : playListInfos) {
			  if(name.equals(playListInfo.getPlayList())){
				  Toast.makeText(context, context.getString(R.string.create_list_success) , Toast.LENGTH_SHORT).show();	
			      if(!(songId+"").equals("")){
			    	  int count = PlayListDao.addToPlaylist(context, new long[]{Long.parseLong(songId)} , playListInfo.getListId());
			    	  Toast.makeText(context, count + context.getString(R.string.add_song_into_list_success) , Toast.LENGTH_SHORT).show();
			      }
				  return;
			  }
		   }
		   if(PlayListDao.createPlayList(context, name)){
			   Toast.makeText(context, context.getString(R.string.create_list_success) , Toast.LENGTH_SHORT).show();
			   if(!(songId==null)&&!"".equals(songId)){
				      long playListId = CursorUtils.getInstance(context).getPlaylistIdFromName(name); 
			    	  int count = PlayListDao.addToPlaylist(context, new long[]{Long.parseLong(songId)} , playListId);
			    	  Toast.makeText(context, count + context.getString(R.string.add_song_into_list_success) , Toast.LENGTH_SHORT).show();
			     }else{
			    	 Intent intent = new Intent();
				     intent.setAction("FRESH_PLAYLIST_RECEIVER");
				     context.sendBroadcast(intent);
			     }
		   }else
			   Toast.makeText(context, context.getString(R.string.create_list_error) , Toast.LENGTH_SHORT).show();
		       return;
		} 
		
	}
}
