package com.android.mplayer.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mplayer.R;
import com.android.mplayer.dao.AlbumArtDao;
import com.android.mplayer.dao.AlbumDao;
import com.android.mplayer.domain.AlbumInfo;

public class AlbumListItemAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context context;
	private List<AlbumInfo> albumlist;
	
	public AlbumListItemAdapter(Context context) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        albumlist = AlbumDao.getAllAlbum(context);
	}

	@Override
	public int getCount() {
		return albumlist.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.music_album_list_items, null);
		}
		((TextView)convertView.findViewById(R.id.listview_item_album_name)).setText(albumlist.get(position).getAlbum());
		((TextView)convertView.findViewById(R.id.listview_item_album_artist)).setText(albumlist.get(position).getArtist()+context.getString(R.string.left_brackets)+albumlist.get(position).getCountSong()+context.getString(R.string.right_brackets));
		((TextView)convertView.findViewById(R.id.listview_item_album_id)).setText(albumlist.get(position).getAlbumId()+"");
		ImageView listview_item_album_artist_art = (ImageView)convertView.findViewById(R.id.listview_item_album_art);
		loadImage(listview_item_album_artist_art,position);
		return convertView;
	 }
	 private void loadImage(ImageView imageView,int position){
	     new LoadImages(imageView, position).execute();
     }
 
    class LoadImages extends AsyncTask<Void, Void, Bitmap>{
	    ImageView imageView;
	    int position;
        public LoadImages(ImageView imageView,int position){
        this.imageView = imageView;
        this.position = position;
        }

        @Override
		protected Bitmap doInBackground(Void... params) {
			// TODO Auto-generated method stub
    	    Bitmap album = AlbumArtDao.getImageByAlbumId(context, albumlist.get(position).getAlbumId(),true);
			return album;
		}

       protected void onPostExecute(Bitmap result) {
          if(result!=null){
        	imageView.setImageBitmap(result);
          }
    }
    
  }


}
