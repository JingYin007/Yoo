package com.mo.bean;

public class Constant {
	
	public static final int LOCAL = 0;
	public static final int NET = 1;	
	public static final int COL = 2;
	public static final int TEXTHIGH = 45;
	public static final int NOTTEXTSIZE = 30;
	public static final int TEXTSIZE = 41;
	public static final int NETMUSIC_LOAD_OK = 100;
	public static final int IMAGE_LOAD_OK = 200;
	public static final int LRC_PATH_LOAD_OK = 300;
	public static final int LOCAL_PATH_LOAD_OK = 400;
	
	public static final String SERVER_IP = "http://192.168.1.116:8080/MusicServer";
	public static final String NET_MUSIC_URL = SERVER_IP+"/searchsongs";
	public static final String IMAGE_URL = SERVER_IP + "/singerImage";
	public static final String LRC_URL = SERVER_IP +"/lrc";
	
	public static final String UPDATE_UI = "updateMusic";
	public static final String UPDATE_SEEKBAR = "com.anjoyo.seekbar";
}
