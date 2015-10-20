package com.mo.view;

import java.util.ArrayList;

import com.mo.adapter.BookmarkAdapter;
import com.mo.bean.Blogs;
import com.mo.db.Col_BlogsDao;
import com.mo.yoo.R;

import android.app.Activity;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class BookmarkActivity extends Activity{

	TextView tv_back;;
	ListView lv_mark;
	public static ArrayList<Blogs> BlogCol_List;
	private Col_BlogsDao collectionDao;
	public static BookmarkAdapter bookmarkAdapter;
	Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.push_right_in,R.anim.hold);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.bookmark_view);
	    tv_back=(TextView) findViewById(R.id.tv_back);
	    lv_mark=(ListView) findViewById(R.id.lv_mark);
	    collectionDao = new Col_BlogsDao(this);
	   	
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BlogCol_List = collectionDao.getDetail();
				Message msg = handler.obtainMessage();
				msg.obj = BlogCol_List;
				msg.what = 1;
				handler.sendMessage(msg);
				}
		}.start();
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 1:
					bookmarkAdapter = new BookmarkAdapter(BookmarkActivity.this,BlogCol_List);
					lv_mark.setAdapter(bookmarkAdapter);
					break;
				default:
					break;
				}
			}
		};
		tv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.hold,R.anim.push_right_out);
			}
		});
	    
	    lv_mark.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Blogs out = new Blogs();
				out = BlogCol_List.get(position);
				RedirectOutLine(out);
			}
		});
	}
	private void RedirectOutLine(Blogs out) {
		// TODO Auto-generated method stub
		
		String BlogID2=String.valueOf(out.getId());
		String BlogTitle2=out.getTitle();
		String BlogUpdated2=out.getUpdated();
		String BlogCommen2=String.valueOf(out.getComments());
		String BlogContent2=out.getContent();
		String BloggerName2=out.getAuthorName();
		Intent blogsContent_intent=new Intent(BookmarkActivity.this, BookMark_BlogsContentActivity.class);
		 Bundle bundle = new Bundle();
		  //字符、字符串、布尔、字节数组、浮点数等等，都可以传
		 bundle.putString("BlogID2", BlogID2);
		 bundle.putString("BlogTitle2", BlogTitle2);
		 bundle.putString("BlogUpdated2", BlogUpdated2);
		 bundle.putString("BlogCommen2", BlogCommen2);
		 bundle.putString("BlogContent2", BlogContent2);
		 bundle.putString("BloggerName2", BloggerName2);
		  
		  blogsContent_intent.putExtras(bundle);
		  startActivity(blogsContent_intent);
	}
}
