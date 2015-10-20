package com.mo.view;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.mo.bean.NewsContent;
import com.mo.parse.XmlPulltoParser;
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
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class NewsContentActivity extends Activity{

	WebView wv_NewsContent;
	MarqueTextView tv_Title;
	TextView tv_SorceName,tv_published,tv_CommentCount,tv_id,tv_back;
	 private List<NewsContent> newsContent;  
	Handler handler;
	ArrayList<NewsContent> newsContents2;
	public String NEWSid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.push_right_in,R.anim.hold);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_content);
		init();
		
		 /*��ȡIntent�е�Bundle����*/
		//  Bundle bundle = this.getIntent().getExtras();
        /*��ȡBundle�е����ݣ�ע�����ͺ�key*/
        final String NEWSid =getIntent().getStringExtra("NEWSID");

		new Thread(){
			public void run() {

				try {
					URL url = new URL("http://wcf.open.cnblogs.com/news/item/"+NEWSid);
				
					URLConnection connection = url.openConnection();  
					InputStream inputStream = connection.getInputStream();  
					newsContent = XmlPulltoParser.ParseNewsContent(inputStream);  
					newsContents2=new ArrayList<NewsContent>();
						
					newsContents2.addAll(newsContent); 
					        Message msg = handler.obtainMessage();
							msg.obj = newsContent;
							handler.sendMessage(msg);
							
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
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
				// TODO Auto-generated method stub
			tv_id.setText(NEWSid);
			tv_Title.setText(newsContent.get(0).getTitle());
			tv_published.setText("����ʱ�䣺"+newsContent.get(0).getSubmitDate());
			tv_SorceName.setText("FROM��"+newsContent.get(0).getSourceName());
			tv_CommentCount.setText(String.valueOf(newsContent.get(0).getCommentCount()));
	       //������������
			wv_NewsContent.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			wv_NewsContent.loadDataWithBaseURL(null,newsContent.get(0).getContent(),"text/html", "utf-8",null) ;
				
			}
		};		
		   //����WebView���ԣ��ܹ�ִ��Javascript�ű� 
	    WebSettings settings = wv_NewsContent.getSettings();
        settings.setSupportZoom(true);   //֧������
        settings.setBuiltInZoomControls(true); //������������װ��
        settings.setJavaScriptEnabled(true); //����JS�ű�
	
	
		tv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.hold,R.anim.push_right_out);
				
			}
		});
		
		tv_CommentCount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				 String NewsID2=NEWSid;
				 String NewsTitle2=tv_Title.getText().toString();
				 String Comment2=tv_CommentCount.getText().toString();
				 Bundle bundle = new Bundle();
				  //�ַ����ַ������������ֽ����顢�������ȵȣ������Դ�
				 bundle.putString("NewsID2", NewsID2);
				 bundle.putString("NewsTitle2", NewsTitle2);
				 bundle.putString("Comment2", Comment2);
				 Intent comment_intent=new Intent(NewsContentActivity.this, NewsCommentActivity.class);
					
				 comment_intent.putExtras(bundle);
				
				startActivity(comment_intent);
			}
		});
	}
	private void init(){
		tv_id=(TextView) findViewById(R.id.tv_id_News);
		tv_Title=(MarqueTextView) findViewById(R.id.tv_newTitle);
		tv_SorceName=(TextView) findViewById(R.id.tv_sourceName);
		tv_published=(TextView) findViewById(R.id.tv_published);
		tv_CommentCount=(TextView) findViewById(R.id.tv_Comments);
		tv_back=(TextView) findViewById(R.id.tv_back);
		wv_NewsContent=(WebView) findViewById(R.id.wv_Content);
		
		
	}
}
