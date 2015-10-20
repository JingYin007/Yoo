package com.mo.view;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.mo.adapter.BlogsAdapter;
import com.mo.bean.Blogs;
import com.mo.db.Col_BlogsDao;
import com.mo.parse.XmlPulltoParser;
import com.mo.util.NetUtil;
import com.mo.yoo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class BlogsView extends Activity{
	 private ArrayList<Blogs> blogs;
	 private ArrayList<Blogs> tendayBlogs2;
	 private ListView lv_topBlogs;
	 private BlogsAdapter adapter5;
	 private Col_BlogsDao collectionDao;
	 private TextView tv_back,tv_mark;
	 private LinearLayout ll_loadingView;
	 private XmlPulltoParser xpb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.push_right_in,R.anim.hold);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tab_blogs);
		lv_topBlogs=(ListView) findViewById(R.id.lv_tendayblogs);
		tv_back = (TextView) findViewById(R.id.tv_back);
		tv_mark = (TextView) findViewById(R.id.tv_mark);
		ll_loadingView=(LinearLayout) findViewById(R.id.loading);
		collectionDao=new Col_BlogsDao(this);
		xpb=new XmlPulltoParser();
		blogs=new ArrayList<Blogs>();
		InitData();
	
		lv_topBlogs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				lv_BlogsToCentent(view,position,tendayBlogs2);
			}

			private void lv_BlogsToCentent(View view, int position,ArrayList<Blogs> blog) {
	
				
				
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("BlogBean", blog.get(position));
				intent.putExtras(bundle);
				
				intent.setClass(getApplicationContext(), BlogsContentActivity.class);
				
				startActivity(intent);
			}
		});
		tv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.hold,R.anim.push_right_out);
				
			}
		});
		tv_mark.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				Intent mark=new Intent(getApplicationContext(),BookmarkActivity.class);
				startActivity(mark);
			}
		});
		
	}
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				adapter5 = new BlogsAdapter(BlogsView.this, tendayBlogs2);
	            lv_topBlogs.setAdapter(adapter5);  
	            if (tendayBlogs2.size()!=0) {
	            	ll_loadingView.setVisibility(View.GONE);
				}
				break;
			default:
				break;
			}		
		};			
	};
	private void InitData() {
		// TODO Auto-generated method stub
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (NetUtil.isNetOk(BlogsView.this)) {
					try {
						URL url = new URL("http://wcf.open.cnblogs.com/blog/TenDaysTopDiggPosts/10");
						URLConnection connection5 = url.openConnection();  
						InputStream inputStream = connection5.getInputStream();  
						
						blogs = xpb.ParseBlogs(inputStream);  
						tendayBlogs2=new ArrayList<Blogs>();
						
							tendayBlogs2.addAll(blogs);
						       	Message msg = handler.obtainMessage();
								msg.obj = blogs;
								msg.what=2;
								handler.sendMessage(msg);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  		
				}
			}
		}.start();
	}
}
