package com.mo.view;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.mo.bean.NoteInfo;
import com.mo.db.SQLiteOpener_Note;
import com.mo.db.SQLite_Lan;
import com.mo.yoo.NoteFrag;
import com.mo.yoo.R;
import com.mo.yoo.R.id;
import com.mo.yoo.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager.Request;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NoteEditView extends Activity{
	private AlertDialog.Builder builder_edit,builder_edit2;
	private Button btn_Save;
	private EditText et_Title, et_Content;
	private SQLiteOpener_Note dbOpener_Note;
	private SQLiteDatabase sdb;
	private SQLite_Lan sqLite_Lan;
	private int id;
	private ArrayList<NoteInfo> note_Info2;
	private SimpleDateFormat formatter;
	private Date curDate;
	private String str;
	private TextView tv_title;
	/**
	 * OnCreate 进行
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.push_right_in,R.anim.hold);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_note_view);
		
		btn_Save=(Button) findViewById(R.id.btn_save);
		tv_title=(TextView) findViewById(R.id.tv_title);
		et_Title=(EditText) findViewById(R.id.et_title);
		et_Content=(EditText) findViewById(R.id.et_content);
		dbOpener_Note = new SQLiteOpener_Note(getApplicationContext());
		note_Info2=new ArrayList<NoteInfo>();
		sqLite_Lan = new SQLite_Lan();
		sdb = dbOpener_Note.getWritableDatabase();
		/**
		 * 获取系统时间，设置时间格式..
		 */
		builder_edit = new AlertDialog.Builder(this);
		builder_edit2 = new AlertDialog.Builder(this);
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		curDate = new Date(System.currentTimeMillis());
		str = formatter.format(curDate);
		/*
		 * 带参数启动
		 * */
		Intent intent=this.getIntent();
		id=intent.getIntExtra("id", 0);
		if(id!=0){
			//不是新建的时候，点击listview里面的一项得到id.也就是修改的时候id!=0
			et_Title.setText(intent.getStringExtra("title"));
			et_Content.setText(intent.getStringExtra("content"));
		}	
		/**
		 * 保存按钮的功能设置
		 * 根据是编辑笔记本还是修改笔记本的各种情况做出判断
		 */
			btn_Save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (id!=0) {
					final String Title_1 = et_Title.getText().toString();
					final String Content_1 = et_Content.getText().toString();
					builder_edit.setIcon(R.drawable.icon3);
					builder_edit.setTitle("保存");
					builder_edit.setMessage("内容已修改，确定要保存吗？");
					builder_edit.setPositiveButton("确定", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							sqLite_Lan.Update(sdb, id, Title_1, Content_1, str);
							Toast.makeText(getApplicationContext(), "修改完成，已保存。", Toast.LENGTH_SHORT).show();
							NoteFrag.listView_Refresh();
							finish();
						}
						
					});
					builder_edit.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					builder_edit.create();
					builder_edit.show();
					
					if (Title_1.equals(note_Info2.getClass().getName() )) {
						Toast.makeText(getApplicationContext(), "biaotiweibian!", Toast.LENGTH_SHORT).show();
					}
					
					
				}else
				{	final String Content=et_Content.getText().toString();
					final String Title=et_Title.getText().toString();
					
					if(Title.equals("") && Content.equals("")){
					Toast.makeText(getApplicationContext(), "内容为空，记录不保存！", Toast.LENGTH_SHORT).show();
					NoteFrag.listView_Refresh();
					finish();
				}else if(Title.equals("") && !Content.equals("")){
					Toast.makeText(getApplicationContext(), "请添加标题！", 
							Toast.LENGTH_SHORT).show();
				}else if(!Title.equals("") && Content.equals("")){
					Toast.makeText(getApplicationContext(), "内容不能为空！", 
							Toast.LENGTH_SHORT).show();
				}else{
					builder_edit.setIcon(R.drawable.icon3);
					builder_edit.setTitle("保存");
					builder_edit.setMessage("确认要保存吗？");
					builder_edit.setPositiveButton("确定", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							insertSQLite(Title, Content, str);
							Toast.makeText(getApplicationContext(), "保存成功！", 
									Toast.LENGTH_SHORT).show();
						}
						
					});
					builder_edit.setNegativeButton("取消", new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}
							
						});
						builder_edit.create().show();
				}}
			}
		});
		tv_title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.hold,R.anim.push_right_out);
				
			}
		});
	}
	/**
	 * 插入信息功能的实现
	 * @param noteTitle
	 * @param noteContent
	 * @param str
	 */
	private void insertSQLite(String noteTitle, String noteContent, final String str) {
		sqLite_Lan.Insert(sdb, noteTitle, noteContent, str);
		NoteFrag.listView_Refresh();
		finish();
	}
	/**
	 * 菜单栏，分享键的功能设置(non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	/**
	 * 返回键的功能分类设置(non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		final String str = formatter.format(curDate);
		/**
		 * 判断，编辑界面是更改以前的内容，还是新建笔记
		 */
		if (id!=0) {
			final String Title_1 = et_Title.getText().toString();
			final String Content_1 = et_Content.getText().toString();
			builder_edit2.setIcon(R.drawable.icon4);
			builder_edit2.setTitle("保存");
			builder_edit2.setMessage("内容已修改，确定要保存吗？");
			builder_edit2.setPositiveButton("确定", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					sqLite_Lan.Update(sdb, id, Title_1, Content_1, str);
					NoteFrag.listView_Refresh();
					finish();
				}
				
			});
			builder_edit2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			builder_edit2.create();
			builder_edit2.show();
		}else
			/**
			 * 新建笔记的情况判断
			 */
		{	final String Content=et_Content.getText().toString();
			final String Title=et_Title.getText().toString();
			
			if(Title.equals("") && Content.equals("")){
			Toast.makeText(getApplicationContext(), "内容为空，记录不保存！", Toast.LENGTH_SHORT).show();
			NoteFrag.listView_Refresh();
			finish();
		}else if(Title.equals("") && !Content.equals("")){
			Toast.makeText(getApplicationContext(), "请添加标题！", 
					Toast.LENGTH_SHORT).show();
		}else if(!Title.equals("") && Content.equals("")){
			Toast.makeText(getApplicationContext(), "内容不能为空！", 
					Toast.LENGTH_SHORT).show();
		}else{
			builder_edit2.setIcon(R.drawable.icon4);
			builder_edit2.setTitle("保存");
			builder_edit2.setMessage("确定要保存吗？");
			builder_edit2.setPositiveButton("确定", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					insertSQLite(Title, Content, str);
					Toast.makeText(getApplicationContext(), "保存成功！", 
							Toast.LENGTH_SHORT).show();
				}
				
			});
			builder_edit2.setNegativeButton("取消", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
				builder_edit2.create().show();
		}}
	
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add("分享");
	}
}
