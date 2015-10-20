package com.mo.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;





import com.mo.bean.Constant;
import com.mo.bean.Music;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

public class MusicUtil {
	public static ArrayList<Music> scanSdcard(Context context) {
		ArrayList<Music> musicList = new ArrayList<Music>();
		/**
		 * 以下方法为扫描sd卡*/
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

		while (cursor.moveToNext()) {
			Music music = new Music();

			music.setTitle(cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
			music.setArtist(cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
			music.setAblum(cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
			music.setDuration(cursor.getInt(cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
			music.setUrl(cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
			music.setWhichList(Constant.LOCAL);

		//Log.d("motou", "MusicUtil---music:"+music.toString());
			musicList.add(music);

		}
		return musicList;
	}

	public static void loadNetMusic(final int size, final String language,
			final Handler handler) {
		new Thread() {
			public void run() {/*
				HttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(Constant.NET_MUSIC_URL);
				// 设置请求参数
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("size", size + ""));
				params.add(new BasicNameValuePair("language", language));
				String result = null;
				try {
					httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
					HttpResponse response = client.execute(httpPost);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						result = EntityUtils.toString(response.getEntity());
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

//				System.out.println("json:"+result);
//				System.out.println("msg.obj:"+JSONParser.parser(result));
				handler.sendMessage(handler.obtainMessage(
					Constant.NETMUSIC_LOAD_OK, JSONParser.parser(result)));
			*/};
		}.start();
	}

	public void loadImage(final String image_url, final Handler handler) {
		new Thread() {
			public void run() {
				Bitmap bitmap = null;
				String path = Environment.getExternalStorageDirectory()
						+ "/MusicWork/image/" + image_url + ".jpg";
				File file = new File(path);
				if (!file.exists()) {
					try {	
						URL url = new URL(encodeImageUrl(image_url, "utf-8"));
						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();
						conn.connect();
						InputStream in = conn.getInputStream();
						saveToSdcard(in, path);
						conn.disconnect();
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
				bitmap = BitmapFactory.decodeFile(path);
				handler.sendMessage(handler.obtainMessage(
						Constant.IMAGE_LOAD_OK, bitmap));
			};
		}.start();
	}

	public void loadLrcPath(final String lrc_url, final Handler handler) {
		new Thread() {
			public void run() {
				String path = Environment.getExternalStorageDirectory()
						+ "/MusicWork/lrc/" + lrc_url + ".lrc";
				File file = new File(path);
				if (!file.exists()) {
					try {	
						URL url = new URL(encodeLrcUrl(lrc_url, "utf-8"));
						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();
						conn.connect();
						InputStream in = conn.getInputStream();
						saveToSdcard(in, path);
						conn.disconnect();
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
				handler.sendMessage(handler.obtainMessage(
						Constant.LRC_PATH_LOAD_OK, path));
			};
		}.start();
	}

	
	
	public void saveToSdcard(InputStream in, String path) {
		File file = new File(path);
		file.getParentFile().mkdirs();
		try {
			OutputStream out = new FileOutputStream(path);
			byte[] b = new byte[20];
			int c = 0;
			while ((c = in.read(b, 0, b.length)) != -1) {
				out.write(b, 0, c);
			}
			out.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String encodeImageUrl(String str, String charset) {
		str = Constant.IMAGE_URL + "/" + str + ".jpg";
		
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			int v = (int) c;
			if ((v >= 19968 && v <= 171941)) {
				try {
					str = str.replace(c + "",
							URLEncoder.encode(c + "", charset));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return str.replace(" ", "%20").replace("(", "%28").replace(")", "%29")
				.replace("&", "%26");
	}

	private String encodeLrcUrl(String str, String charset) {
		str = Constant.LRC_URL + "/" + str;
		
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			int v = (int) c;
			if ((v >= 19968 && v <= 171941)) {
				try {
					str = str.replace(c + "",
							URLEncoder.encode(c + "", charset));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return str.replace(" ", "%20").replace("(", "%28").replace(")", "%29")
				.replace("&", "%26");
	}
	
	/**
	 * 查找本地歌曲，以便获取其歌词
	 * */
	
	List<String> allLrc = new ArrayList<String>();
	public void findAllLrc(File file){
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				for (File file1 : files) {
					findAllLrc(file1);
				}
			}
		} else {
			if (file.getName().endsWith(".lrc")) {
				allLrc.add(file.getAbsolutePath());
			}

		}
	}
	
	public List<String> getAllLrc(){
		return allLrc;
	}
	
	public String getLocalLrc(String title, String artist, List<String> list){
		String path =  null;
		//Log.d("motou", "title:"+title+" artist:"+artist);
		for (int i = 0; i < list.size(); i++) {
			
			//if(list.get(i).contains(title) && list.get(i).contains(artist))
			if(list.get(i).contains(title)){
				path = list.get(i);
			//	Log.d("motou", "lrc--path:"+path);
				break;
			}
		}
		return path;
	}
}
