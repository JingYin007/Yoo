package com.mo.view;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.mo.bean.Blogs;
import com.mo.db.Col_BlogsDao;
import com.mo.yoo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.TextView;
import android.widget.Toast;

public class BookMark_BlogsContentActivity extends Activity{

	WebView wv_BlogsContent;
	TextView tv_Title,tv_Comment,tv_name,tv_BlogsID,tv_saved,tv_back;
	
	private String blogsContent="";  
	Handler handler;
	private Col_BlogsDao collectionDao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.push_right_in,R.anim.hold);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.blogs_content);
		init();
        final String BlogID22=getIntent().getStringExtra("BlogID2");
        final String BlogTitle22 =getIntent().getStringExtra("BlogTitle2");
        final String BlogUpdated22 =getIntent().getStringExtra("BlogUpdated2");
        final String BlogCommen22 =getIntent().getStringExtra("BlogCommen2");
        final String tv_AvathorName22 =getIntent().getStringExtra("BloggerName2");
        final String BlogContent22 =getIntent().getStringExtra("BlogContent2");
         
        tv_BlogsID.setText(BlogID22);
		tv_Title.setText(BlogTitle22);
		tv_Comment.setText(BlogCommen22);
		tv_name.setText(tv_AvathorName22);
		wv_BlogsContent.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		wv_BlogsContent.loadDataWithBaseURL(null,BlogContent22,"text/html", "utf-8",null) ;
		
		tv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.hold,R.anim.push_right_out);
				
			}
		});
		tv_Comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				 String NewsID2=BlogID22;
				 String NewsTitle2=tv_Title.getText().toString();
				 String Comment2=tv_Comment.getText().toString();
				 Bundle bundle = new Bundle();
				  //字符、字符串、布尔、字节数组、浮点数等等，都可以传
				 bundle.putString("NewsID2", NewsID2);
				 bundle.putString("NewsTitle2", NewsTitle2);
				 bundle.putString("Comment2", Comment2);
				 Intent comment_intent=new Intent(BookMark_BlogsContentActivity.this, BlogsCommentActivity.class);
					
				 comment_intent.putExtras(bundle);
				 startActivity(comment_intent);
			
			}
		});
		tv_saved.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (collectionDao.queryByUrl(Integer.valueOf(BlogID22))) {
					Toast.makeText(getApplicationContext(), "该文章已被收藏",
							Toast.LENGTH_SHORT).show();
				} else {
					Blogs outline = new Blogs();
					outline.setTitle(BlogTitle22);
					outline.setId(BlogID22);
					outline.setComments(Integer.valueOf(BlogCommen22));
					outline.setContent(blogsContent);
					outline.setAuthorName(tv_AvathorName22);
					outline.setUpdated(BlogUpdated22);
					collectionDao.addDetail(outline);
					Toast.makeText(getApplicationContext(), "收藏成功",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	private void init() {
		tv_Title=(TextView) findViewById(R.id.tv_blogTitle2);
		tv_Comment=(TextView) findViewById(R.id.tv_contentCount2);
		wv_BlogsContent=(WebView) findViewById(R.id.wv_Content2);
		tv_name= (TextView) findViewById(R.id.tv_name2);
		tv_BlogsID=(TextView) findViewById(R.id.tv_id_Blogs);
		tv_saved=(TextView) findViewById(R.id.tv_saved);
		tv_back=(TextView) findViewById(R.id.tv_back);
		collectionDao = new Col_BlogsDao(getApplicationContext());
	}
}
