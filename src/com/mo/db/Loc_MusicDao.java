package com.mo.db;

import java.util.ArrayList;




import com.mo.bean.Music;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Loc_MusicDao {
	private Context context;
	private static DBHelper_Music dbHelper;
	
	public Loc_MusicDao(Context context){
		this.context = context;
		dbHelper = new DBHelper_Music(context, DBHelper_Music.DB_NAME, null, 1);
		//System.out.println("数据库调用成功");
	}
	
	public void insertMusics(ArrayList<Music> musics){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		for(int i=0; i<musics.size(); i++){
			Music music = musics.get(i);
			ContentValues values = new ContentValues();
			values.put("whichList", music.getWhichList());
			values.put("title", music.getTitle());
			values.put("artist", music.getArtist());
			values.put("album", music.getAlbum());
			values.put("duration", music.getDuration());
			values.put("url", music.getUrl());
			db.insert(DBHelper_Music.MUSIC_TABLE_NAME, null, values);
			Log.d("motou", "db.insert(DBHelper2.MUSIC_TABLE_NAME, null, values);");
		}
		db.close();
	}
	
	public  ArrayList<Music> getMusics(){
		ArrayList<Music> musics = new ArrayList<Music>();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query(DBHelper_Music.MUSIC_TABLE_NAME, null, null,
				null, null, null, null);
		while(cursor != null && cursor.moveToNext()){
			
			Music music = new Music();
			music.setWhichList(cursor.getInt(cursor.getColumnIndex("whichList")));
			music.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			music.setArtist(cursor.getString(cursor.getColumnIndex("artist")));
			music.setAblum(cursor.getString(cursor.getColumnIndex("album")));
			music.setDuration(cursor.getInt(cursor.getColumnIndex("duration")));
			music.setUrl(cursor.getString(cursor.getColumnIndex("url")));
		
			musics.add(music);
		}
		cursor.close();
		db.close();
		Log.d("motou","-----------getMusics");
		return musics;
		
	}
	public void delete(String url){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.delete(DBHelper_Music.MUSIC_TABLE_NAME, "url = ?", new String[]{url});
		db.close();
		//musics.remove(context);
	}
}
