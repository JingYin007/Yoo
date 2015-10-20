package com.mo.db;

import java.util.ArrayList;

import com.mo.bean.NoteInfo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLite_Lan {

ArrayList<NoteInfo> note_Info = new ArrayList<NoteInfo>();
	
	public ArrayList<NoteInfo> select(SQLiteDatabase sdb) {
		Cursor cursor = sdb.rawQuery("select * from Note_File", new String[]{});
		cursor.moveToFirst();
		note_Info.clear();
		while(!cursor.isAfterLast()){
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String content = cursor.getString(cursor.getColumnIndex("content"));
			String create_time = cursor.getString(cursor.getColumnIndex("time"));
			NoteInfo note =new NoteInfo(id, title, content, create_time);
			note_Info.add(note);
			cursor.moveToNext();
		}
		cursor.close();
		sdb.close();
		return note_Info;
	}
	//插入方法。
	public void Insert(SQLiteDatabase sdb, String title, String content,
			String time) {
		sdb.execSQL("insert into Note_File(title, content, time)values('" + title
				+ "','" + content + "','" + time + "')");
		sdb.close();
	}
    //删除方法。
	public void Delete(SQLiteDatabase sdb, int id) {

		sdb.execSQL("delete from Note_File where id=" + id + "");
		sdb.close();
	}
	
    //修改方法。
	public void Update(SQLiteDatabase sdb, int id, String title,
			String content, String time) {
		sdb.execSQL("update Note_File set title='" + title + "', content='" + content
				+ "', time='" + time + "' where id=" + id + "");
		sdb.close();
	}

	
}
