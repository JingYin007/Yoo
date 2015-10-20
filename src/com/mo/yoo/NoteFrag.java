package com.mo.yoo;

import java.util.ArrayList;
import java.util.HashMap;

import com.mo.adapter.Note_Adapter;
import com.mo.bean.NoteInfo;
import com.mo.db.SQLiteOpener_Note;
import com.mo.db.SQLite_Lan;
import com.mo.music.MusicView;
import com.mo.view.CompassActivity;
import com.mo.view.MuTouActivity;
import com.mo.view.MusicActivity;
import com.mo.view.WelcomeActivity;
import com.mo.view.NoteEditView;
import com.mo.view.WeatherActivity;
import com.mo.yoo.ArcMenu.OnMenuItemClickListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class NoteFrag extends Fragment {
	private ArcMenu mArcMenu;
	public static Context context;
	public static ListView lv_Note;
	private TextView tv_Edit;
	private Cursor cursor;
	public static  SQLite_Lan s_lan;
	private AlertDialog.Builder builder_del,builder_exit;
	private SQLiteDatabase sdb;
	public static SQLiteOpener_Note dbOpener_Note;
	private SimpleAdapter sa_adapter;
	private Intent intent;
	private int count=0;
	public static Note_Adapter adapter;
	private String sql="select * from Note_File";
	private ArrayList<HashMap<String, Object>> listData =new ArrayList<HashMap<String,Object>>();
	public static ArrayList<NoteInfo> note_Info;
	private View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if(rootView==null){  
            rootView=inflater.inflate(R.layout.tab_note, null);  
        }  
		//缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。  
        ViewGroup parent = (ViewGroup) rootView.getParent();  
        if (parent != null) {  
            parent.removeView(rootView);  
        }   
        return rootView;  
		//	return inflater.inflate(R.layout.tab_news, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		context = getActivity();
		
		note_Info=new ArrayList<NoteInfo>();
		dbOpener_Note=new SQLiteOpener_Note(getActivity());
		 builder_del = new AlertDialog.Builder(getActivity());
		 builder_exit=new AlertDialog.Builder(getActivity());
		lv_Note=(ListView) getActivity().findViewById(R.id.lv_note);	
		s_lan=new SQLite_Lan();
		mArcMenu = (ArcMenu)getActivity().findViewById(R.id.id_menu);
		listView_Refresh();//初始化界面刷新
		initArcEvent();
		lv_Note.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?>  parent, View view, int position,
					long id) {
				++count;
				intent = new Intent(getActivity(), NoteEditView.class);
				intent.putExtra("id", note_Info.get(position).getId() );
				intent.putExtra("title", note_Info.get(position).getTitle() );
				intent.putExtra("content", note_Info.get(position).getContent());
				startActivity(intent);
				
			}
		});
		/**
		 * 长按ListView的点击事件
		 */
		
		lv_Note.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					 final int position,  long id) {
				builder_del.setIcon(R.drawable.top6);
				builder_del.setTitle("Miracles happen every day!");
				builder_del.setMessage("确定要删除笔记吗?");
				builder_del.setPositiveButton("确定", 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								int id=note_Info.get(position).getId();
								s_lan.Delete(dbOpener_Note.getWritableDatabase(),id);
								listView_Refresh();
						}
				});
				builder_del.setNegativeButton("取消", 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}
				});
				builder_del.create().show();
				return true;
			}
		});
		/**
		 * “写标签”功能按钮的实现
		 */
		tv_Edit=(TextView) getActivity().findViewById(R.id.tv_edit);
		tv_Edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			Intent in_write=new Intent(getActivity(),NoteEditView.class);
			startActivity(in_write);
			}
		});
		
	
	}

	/**
	 * 设计效果
	 * 
	 */
	private void initArcEvent() {
		
		mArcMenu.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public void onClick(View view, int pos)
			{
				switch (pos) {
				case 1:
					Intent intent_music = new Intent(getActivity(),MusicView.class);
					startActivity(intent_music);
					break;
				case 2:
					Intent intent_weather = new Intent(getActivity(),WeatherActivity.class);
					startActivity(intent_weather);
					break;
				case 3:
					Intent intent_roboat = new Intent(getActivity(),MuTouActivity.class);
					startActivity(intent_roboat);
					break;
				case 4:
					//Toast.makeText(getActivity(), ""+view.getTag(), Toast.LENGTH_SHORT).show();
					Intent intent_set = new Intent(getActivity(),CompassActivity.class);
					startActivity(intent_set);
					break;
				case 5:
					builder_exit.setIcon(R.drawable.yoo);
					builder_exit.setTitle("Miracles happen every day!");
					builder_exit.setMessage("确定要退出程序吗?");
					builder_exit.setPositiveButton("确定", 
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									getActivity().finish();
									//android.os.Process.killProcess(android.os.Process.myPid());
									System.exit(0);
								}
					});
					builder_exit.setNegativeButton("取消", 
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
								}
					}).create().show();
				
					break;
				}
				//Toast.makeText(getActivity(), ""+view.getTag(), Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public ArrayList<HashMap<String, Object>> rawQuery(){
		sdb= dbOpener_Note.getWritableDatabase();
		cursor=sdb.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String title=cursor.getString(cursor.getColumnIndex("title"));
			cursor.getString(cursor.getColumnIndex("content"));
			cursor.getString(cursor.getColumnIndex("time"));
			HashMap<String, Object> map=new HashMap<String, Object>();
			map.put("_id", cursor.getString(0));
			map.put("title", cursor.getString(1));
			map.put("content",cursor.getString(2));
			map.put("time", cursor.getString(3));
			listData.add(map);
			cursor.moveToNext();	
		}
		sdb.close();
		return listData;
	
	}
		/**
		 * ListView 界面刷新功能方法
		 */
	public static  void listView_Refresh() {
		note_Info = s_lan.select(dbOpener_Note.getWritableDatabase());
		  adapter = new Note_Adapter(context, note_Info);
		lv_Note.setAdapter(adapter);
	}

	
}
