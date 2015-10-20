package com.mo.util;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BroadUtil {
	/**
	 * ���Ͳ��ŵĹ㲥
	 * */
	public static void sendPlayBroad(Context context, String path, boolean isContinue){
		Intent intent = new Intent(MusicService.PLAY);
		intent.putExtra("path", path);
		
	
		intent.putExtra("isContinue", isContinue);
		context.sendBroadcast(intent);
		Log.d("motou","���Ͳ��Ź㲥����path:"+path);
	}
	/**
	 * ������ͣ�Ĺ㲥
	 * */
	public static void sendPauseBroad(Context context){
		Intent intent = new Intent(MusicService.PAUSE);
		context.sendBroadcast(intent);
	}
}
