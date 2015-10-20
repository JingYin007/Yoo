package com.mo.lyric;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;


public class LyricReader {
	private List<LyricBean> list = new ArrayList<LyricBean>();
	
	public void read(String path) throws IOException{
		list.clear();
		if (path == null) {
			return;
		}
		FileInputStream fis = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		
		String line = null;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			line = line.replace("[", "");
			line = line.replace("]", "@");
			String[] strs = line.split("@");
			if(strs.length == 2){
				LyricBean lyricBean = new LyricBean();
				//String str1 = strs[0];
				//String str2 = strs[1];
				
				lyricBean.setTime(getTime(strs[0]));
				lyricBean.setSentences(strs[1]);
				list.add(lyricBean);
			}
			Log.d("motou", "LyricReader---music:"+line);
		}
	}
	
	private int getTime(String t){
		int time = 0;
		
		t = t.replace(":", "@");
		t = t.replace(".", "@");
		String[] ss = t.split("@");
		if (ss.length == 2) {
			int minute = Integer.parseInt(ss[0]);
			int second = Integer.parseInt(ss[1]);
			time = 1000*(minute*60 + second);
		}else if(ss.length == 3){
			int minute = Integer.parseInt(ss[0]);
			int second = Integer.parseInt(ss[1]);
			int mSecond = Integer.parseInt(ss[2]);
			time = 1000*(minute*60 + second) + mSecond*10;
		}
		return time;
	}
	
	public List<LyricBean> getList(){
		return list;
	}
}
