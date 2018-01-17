package com.android.mplayer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.android.mplayer.dao.AlbumArtDao;
import com.android.mplayer.domain.Mp3Info;
/**
 * 一步图片加载类 用于异步加载专辑图片
 * @author JIANGPENG
 * */
public class LoadSmallImageUtil extends AsyncTask<Void, Void, Bitmap>{
    ImageView imageView;
    Mp3Info mp3Info;
    Context context;
 public LoadSmallImageUtil(ImageView imageView,Mp3Info mp3Info,Context context){
	this.context = context;
    this.imageView = imageView;
    this.mp3Info = mp3Info;
 }
@Override
protected Bitmap doInBackground(Void... params) {
		// TODO Auto-generated method stub
	        Bitmap album = AlbumArtDao.getArtwork(context , mp3Info.getId() , mp3Info.getAlbumId() , true , true);
		return album;
	}



protected void onPostExecute(Bitmap result) {
   if(result!=null){
 	 imageView.setImageBitmap(result);
   }
}
}
