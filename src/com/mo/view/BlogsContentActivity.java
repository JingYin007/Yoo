package com.mo.view;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.mo.bean.Blogs;
import com.mo.db.Col_BlogsDao;import com.mo.parse.XmlPulltoParser;
import com.mo.yoo.R;

import android.app.Activity;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.TextView;
import android.widget.Toast;

public class BlogsContentActivity extends Activity{

	WebView wv_BlogsContent;
	MarqueTextView tv_Title;
	TextView tv_Comment,tv_name,tv_BlogsID,tv_saved,tv_back;
	private Blogs data;
	private String blogsContent=""; 
	private String blogId,blogTitle,blogComment,blogAuthorname,blogUpdated;
	Handler handler;
	private Col_BlogsDao collectionDao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.push_right_in,R.anim.hold);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.blogs_content);
		init();
		data = (Blogs) getIntent().getSerializableExtra("BlogBean");
		blogId=data.getId();
		blogTitle=data.getTitle();
		blogAuthorname=data.getAuthorName();
		blogComment=String.valueOf(data.getComments());
		
        tv_BlogsID.setText(blogId);
		tv_Title.setText(blogTitle);
		tv_Comment.setText(blogComment);
		tv_name.setText(blogAuthorname);
		new Thread(){
			public void run() {

				try {
					URL url = new URL("http://wcf.open.cnblogs.com/blog/post/body/"+blogId);
					
					URLConnection connection = url.openConnection();  
					InputStream inputStream = connection.getInputStream();  
					blogsContent = XmlPulltoParser.ParseBlogsContent(inputStream);  
					        Message msg = handler.obtainMessage();
							msg.obj = blogsContent;
							handler.sendMessage(msg);
							
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.d("node","�޷�����");  
					e.printStackTrace();
				} catch (Throwable e) {
					e.printStackTrace();
				}  
			};
		}.start();
		handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
	       //������������
			wv_BlogsContent.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			wv_BlogsContent.loadDataWithBaseURL(null,blogsContent,"text/html", "utf-8",null) ;
	        	
			}
		};		
		
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
				
				 String NewsID2=blogId;
				 String NewsTitle2=tv_Title.getText().toString();
				 String Comment2=tv_Comment.getText().toString();
				// Log.d("node", "---"+NewsID2+"---"+NewsTitle2);
				 Bundle bundle = new Bundle();
				  //�ַ����ַ������������ֽ����顢�������ȵȣ������Դ�
				 bundle.putString("NewsID2", NewsID2);
				 bundle.putString("NewsTitle2", NewsTitle2);
				 bundle.putString("Comment2", Comment2);
				 Intent comment_intent=new Intent(BlogsContentActivity.this, BlogsCommentActivity.class);
				 comment_intent.putExtras(bundle);
				startActivity(comment_intent);
			
			}
		});
		tv_saved.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				
				if (collectionDao.queryByUrl(Integer.valueOf(blogId))) {
					Toast.makeText(getApplicationContext(), "�������ѱ��ղ�",
							Toast.LENGTH_SHORT).show();
				} else {
					Blogs outline = new Blogs();
					outline.setTitle(blogTitle);
					outline.setId(blogId);
					outline.setComments(Integer.valueOf(blogComment));
					outline.setContent(blogsContent);
					outline.setAuthorName(blogAuthorname.substring(4));
					outline.setUpdated(blogUpdated);
					collectionDao.addDetail(outline);
					Toast.makeText(getApplicationContext(), "�ղسɹ�",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	private void init() {
		tv_Title=(MarqueTextView) findViewById(R.id.tv_blogTitle2);
		tv_Comment=(TextView) findViewById(R.id.tv_contentCount2);
		wv_BlogsContent=(WebView) findViewById(R.id.wv_Content2);
		tv_name= (TextView) findViewById(R.id.tv_name2);
		tv_BlogsID=(TextView) findViewById(R.id.tv_id_Blogs);
		tv_saved=(TextView) findViewById(R.id.tv_saved);
		tv_back=(TextView) findViewById(R.id.tv_back);
		collectionDao = new Col_BlogsDao(getApplicationContext());
	}
}
