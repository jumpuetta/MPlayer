package com.android.mplayer.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;

import com.android.mplayer.R;
import com.android.mplayer.constants.AppStateConstant;

/**
 * 用于在网络上下载歌词并保存到本地
 * */
public class SearchLrcUtil {  
    private URL url;  
    public static final String DEFAULT_LOCAL = "GB2312";  
    StringBuffer sb = new StringBuffer();  
    private boolean findNumber = false;  
    private Context context;
    /** 
     * 初期化，根据参数取得lrc的地址
     * @author JIANGPENG
     * @param musicName 歌曲名
     * @param singerName 歌手名
     * @param context 上下文
     */  
    public SearchLrcUtil(String musicName, String singerName,Context context) {  
    	this.context = context;
    	
        try {  
            musicName = URLEncoder.encode(musicName, "utf-8");  
            singerName = URLEncoder.encode(singerName, "utf-8");  
        } catch (UnsupportedEncodingException e2) {  
            // TODO Auto-generated catch block  
            e2.printStackTrace();  
        }         
          
//      String strUrl = "http://box.zhangmen.baidu.com/x?op=12&title="  
//              + musicName + "$$" + singerName + "$$$$";  
          
        String strUrl = "http://box.zhangmen.baidu.com/x?op=12&count=1&title=" +  
        musicName + "$$"+ singerName +"$$$$";  
        try {  
            url = new URL(strUrl);  
        } catch (Exception e1) {  
            e1.printStackTrace();  
        }  
        BufferedReader br = null;  
        String s;  
        try {  
            HttpURLConnection   httpConn   =   (HttpURLConnection)url.openConnection();  
            httpConn.connect();  
            InputStreamReader inReader = new InputStreamReader(httpConn.getInputStream());  
            br = new BufferedReader(inReader);  
        } catch (IOException e1) {  
            Toast.makeText(context, context.getString(R.string.net_exception),Toast.LENGTH_SHORT).show();  
        }  
        try {  
            while ((s = br.readLine()) != null) {  
                sb.append(s + "/r/n");                
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }finally{  
            try {  
                br.close();  
            } catch (IOException e) {                 
                e.printStackTrace();  
            }  
        }  
    }  
    /**
     * 根据lrc的地址，读取lrc文件流 
     * 生成歌词的ArryList 
     * 每句歌词是一个String 
     * @author JIANGPENG
     * @return ArrayList 歌词信息
     */  
    public ArrayList<String> fetchLyric() {  
        int begin = 0, end = 0, number = 0;// number=0表示暂无歌词  
        String strid = "";  
        begin = sb.indexOf("<lrcid>");  
        if (begin == -1) {  //无歌词信息返回null
            return null;
        }  
        end = sb.indexOf("</lrcid>", begin);  
        strid = sb.substring(begin + 7, end);  
        number = Integer.parseInt(strid); 
        if(number <= 0){  //有歌词信息但无歌词文件返回null
        	return null;
        }
        String geciURL = "http://box.zhangmen.baidu.com/bdlrc/" + number / 100  
                + "/" + number + ".lrc";  
        SetFindLRC(number);  
        ArrayList<String>  gcContent =new ArrayList<String> ();  
        String s = new String();  
        try {  
            url = new URL(geciURL);  
        } catch (MalformedURLException e2) {  
            e2.printStackTrace();  
        }  
        BufferedReader br = null;  
        try {  
            br = new BufferedReader(new InputStreamReader(url.openStream(), "GB2312"));  
        } catch (IOException e1) {  
        	return null; 
        }  
        if (br == null) {
              System.out.print("stream is null");  
        } else {  
            try {  
                while ((s = br.readLine()) != null) {  
                    gcContent.add(s);  
                }  
                br.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return gcContent;  
    }  
    private void SetFindLRC(int number) {  
        if(number == 0)  
            findNumber = false;  
        else   
            findNumber = true;  
    }  
    
    public boolean GetFindLRC(){  
        return findNumber;  
    }  
    
    /**
     * 将下载的歌词内容写到SDCARD中
     * @author JIANGPENG 
     * @return boolean
     * */
    public boolean saveLrcInScard(String fileName){
    	ArrayList<String> LrcContent =  fetchLyric();
    	if(LrcContent != null){
    	  String path = AppStateConstant.APP_PATH+"lrc";
    	  File filePath = new File(path);
    	  File f = new File(AppStateConstant.APP_PATH+"lrc/"+fileName);
    	  if(!filePath.exists()){//项目目录不存在则创建
    		  filePath.mkdirs();
    	  }
	       try {
		     FileOutputStream out = new FileOutputStream(f);
		    for (String string : LrcContent) {
		    	//用空格取代歌词中的@符号，方便解析
				  out.write((string.replace("@", " ")+"\n").getBytes());
				}
				out.close();
				return true;
			  } catch (Exception e) {
				e.printStackTrace();
				return false;
			  }
    	  }
    	return false;
    }
    
    
    
}  