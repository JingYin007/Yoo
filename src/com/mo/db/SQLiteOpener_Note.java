package com.mo.db;
import java.util.ArrayList;
import java.util.HashMap;

import com.mo.bean.NoteInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteOpener_Note extends SQLiteOpenHelper{

	SQLiteDatabase sdb;
	SQLiteOpener_Note dbOpener_Note;
	Cursor cursor;
	String sql="select * from Note_File";
	ArrayList<HashMap<String, Object>> listData =new ArrayList<HashMap<String,Object>>();
	NoteInfo note_info;
	private static String DB_NAME="Note_db";
	//建立数据库
	public SQLiteOpener_Note(Context context) {
		super(context, DB_NAME, null, 1);
		// TODO Auto-generated constructor stub
	}
	//在其中建表
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table Note_File (id integer primary key autoincrement," +
				"title text,content text," +
				"time text)");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exit Note_File");
		onCreate(db);
	}
	
}
