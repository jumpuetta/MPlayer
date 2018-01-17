package com.android.mplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.mplayer.R;

public class MusicOnlineFragment extends Fragment {
   private static MusicOnlineFragment fragment = null;
   private WebView web;
   public static MusicOnlineFragment newInstance(){
	   if(fragment == null){
		   fragment = new MusicOnlineFragment();
	   }
	   return fragment;
   }
   
   public void onCreate(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
   }
   
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View convertView = inflater.inflate(R.layout.music_online, null);
	        web = (WebView)convertView.findViewById(R.id.web);
			WebSettings s = web.getSettings();

			s.setSaveFormData(false);
			s.setSavePassword(false);
			s.setUseWideViewPort(true);
			s.setJavaScriptEnabled(true);
			s.setLightTouchEnabled(true);

			web.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
			  //Activity和Webview根据加载程度决定进度条的进度大小
			  //当加载到100%的时候 进度条自动消失
			  getActivity().setProgress(progress * 100);
			}

			});
			web.loadUrl("http://m.ttpod.com/");
	 		return convertView;
   }
}
