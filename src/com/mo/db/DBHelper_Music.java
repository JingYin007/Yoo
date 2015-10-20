package com.mo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper_Music extends SQLiteOpenHelper {

	public static final String DB_NAME="music_db";
	public static final String MUSIC_TABLE_NAME="music_table_name";
	public static final String COL_TABLE_NAME="col_table_name";
	public DBHelper_Music(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase sd) {
		String sql = "create table "+MUSIC_TABLE_NAME+"(" +
				"id integer primary key autoincrement," +
				"whichList integer,"+
				"title String," +
				"artist String," +
				"album String," +
				"duration String," +
				"url String)";
		sd.execSQL(sql);
		Log.d("motou", "!!!-------create table +MUSIC_TABLE_NAME");
		String sql2 = "create table "+COL_TABLE_NAME+"(" +
				"id integer primary key autoincrement," +
				"whichList integer,"+
				"title String," +
				"artist String," +
				"album String," +
				"duration String," +
				"url String)";
		sd.execSQL(sql2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
