package com.mo.view;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mo.adapter.LocalAdapter;
import com.mo.bean.Constant;
import com.mo.bean.Music;
import com.mo.db.Loc_MusicDao;
import com.mo.lyric.LyricReader;
import com.mo.lyric.LyricView;
import com.mo.util.BroadUtil;
import com.mo.util.MusicService;
import com.mo.util.MusicUtil;
import com.mo.view.PlayActivity.Receiver;
import com.mo.yoo.R;
import com.mo.yoo.R.id;
import com.mo.yoo.R.layout;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MusicActivity extends Activity implements OnClickListener
{
private TextView tv_total_time, tv_curr_time,back;
private ImageView preBtn, playBtn, nextBtn;
private SeekBar seekBar;
private LinearLayout ll_bg_player;
int whichList;
boolean isPlaying = true;
Receiver receiver = new Receiver();
//用来设置时间格式
private Date date;
private SimpleDateFormat sdf;
	Boolean isFirstRun;
	public static List<String> allLrc = new ArrayList<String>();
	/** 本地列表 */
	public static ArrayList<Music> localMusics;
	public static int currentPos = -1;
	
	public static int loadingTag = -1;
	private Loc_MusicDao localDao;
	public static Intent intent_service;
	View col_view;
	public static ArrayList<Music> currentMusics = new ArrayList<Music>();
	public static LocalAdapter localAdapter;
	ListView lv_localmusic;
	ImageView iv_localPlay;
	TextView tv_back;
	LinearLayout ll_loading;
	public static boolean isExit = false;
	private View rootView;
	
	//为当前播放音乐配置音乐信息
		public static void setCurrentList(int whichList) {
			switch (whichList) {
			case Constant.LOCAL:
				currentMusics.clear();
				currentMusics.addAll(localMusics);
				break;
			default:
				break;
			}
		}
		
		
		Handler handler=new Handler(){
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 3:
					//SharedPreferences存储该程序启动的次数，即状态
					
					/*SharedPreferences sp = getApplicationContext().getSharedPreferences("sp", Context.MODE_PRIVATE);
					isFirstRun = sp.getBoolean("isFirstRun", true);

					localMusics = MusicUtil.scanSdcard(getApplicationContext());
					if (isFirstRun) {
						// 程序第一次运行,扫描sd卡获取歌曲信息
						localMusics = MusicUtil.scanSdcard(getApplicationContext());
						localDao.insertMusics(localMusics);
						sp.edit().putBoolean("isFirstRun", false).commit();
					} else {
						localMusics = localDao.getMusics();
					}*/
				
					localMusics = MusicUtil.scanSdcard(getApplicationContext());
					localDao.insertMusics(localMusics);
					localAdapter=new LocalAdapter(MusicActivity.this, localMusics);
					lv_localmusic.setAdapter(localAdapter);
					break;
				default:
					break;
				}		
				//去掉加载页面
				ll_loading.setVisibility(View.GONE);
			};			
		};
		@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			overridePendingTransition(R.anim.up, R.anim.roll);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			setContentView(R.layout.local_music_view);
	
		lv_localmusic=(ListView)findViewById(R.id.local_list);
		iv_localPlay=(ImageView)findViewById(R.id.iv_playMusic);
		ll_loading=(LinearLayout)findViewById(R.id.loading);
		tv_back=(TextView) findViewById(R.id.tv_back);
		ll_loading.setVisibility(View.VISIBLE);
		localDao = new Loc_MusicDao(getApplicationContext());
		
		intent_service = new Intent(getApplicationContext(), MusicService.class);
		startService(intent_service);
		
		/*if (!MusicService.mediaPlayer.isPlaying()) {
			return;
		}*/
		init();
		//得到本地歌词
				new Thread() {
					public void run() {
						MusicUtil musicUtil = new MusicUtil();
						musicUtil.findAllLrc(new File(Environment
								.getExternalStorageDirectory()+"/"));
						
						allLrc = musicUtil.getAllLrc();
						Log.d("motou", "allLrc"+allLrc.toString());
 
				 Message msg = handler.obtainMessage();
			msg.obj = localMusics;
			msg.what = 3;
			handler.sendMessage(msg);};
				}.start();
				
		
				lv_localmusic.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
						localAdapter.showPos=-1;
						if (localAdapter.colorPos == position) {
							localAdapter.colorPos = -1;
							localAdapter.notifyDataSetChanged();
							BroadUtil.sendPauseBroad(getApplicationContext());
							
						} else {
							localAdapter.setColorPos(position);
							// 播放
							setCurrentList(Constant.LOCAL);
							currentPos = position;
							String path = localMusics.get(currentPos).getUrl();
							BroadUtil.sendPlayBroad(getApplicationContext(), path, false);
						}
					}
				});
				iv_localPlay.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//启动播放界面
						startActivity(new
								 Intent(MusicActivity.this,PlayActivity.class));
					}
				});
				tv_back.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						finish();
					    overridePendingTransition(R.anim.roll, R.anim.down);
					}
				});
	}
		
		
		boolean no_song = false;
		Handler handler2 = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				
				case Constant.LRC_PATH_LOAD_OK:
					String path = msg.obj.toString();
					handler2.post(r);
					break;
				default:
					handler2.post(r);
					break;
				
				}
			};
		};

		Runnable r = new Runnable(){

			@Override
			public void run() {
				handler2.postDelayed(r, 100);
			}
			
		};
	private void init() {
		tv_total_time = (TextView) findViewById(R.id.total_time);
		tv_curr_time = (TextView) findViewById(R.id.pre_time);

		back = (TextView) findViewById(R.id.tv_back);
		preBtn = (ImageView) findViewById(R.id.pre_play);
		playBtn = (ImageView) findViewById(R.id.play_pause);
		nextBtn = (ImageView) findViewById(R.id.next_play);
		ll_bg_player=(LinearLayout) findViewById(R.id.bg_player);
		seekBar = (SeekBar) findViewById(R.id.seekbar);
		preBtn.setOnClickListener(this);
		playBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		back.setOnClickListener(this);
		int times = 0;
if (MusicActivity.currentMusics.size() > 0 ) {
	whichList = MusicActivity.currentMusics.get(0).getWhichList();
	 times= MusicActivity.currentMusics.get(MusicActivity.currentPos).getDuration();
	}
		//whichList = MusicActivity.currentMusics.get(0).getWhichList();
		
		date = new Date(times);
		sdf = new SimpleDateFormat("mm:ss");
		tv_total_time.setText(sdf.format(date));
		handler2.post(r);
		seekBar.setMax(100);

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				MusicService.mediaPlayer.seekTo(seekBar.getProgress()
						* MusicService.mediaPlayer.getDuration() / 100);
				MusicService.progress = seekBar.getProgress()
						* MusicService.mediaPlayer.getDuration() / 100;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {

				}
			}
		});
		}
	@Override
	public void onResume() {
		super.onResume();
		//当前没有音乐播放
		if (currentMusics.size() == 0) {
			return;
		}
		int whichList = currentMusics.get(currentPos).getWhichList();
		switch (whichList) {
		case Constant.LOCAL:
			localAdapter.notifyDataSetChanged();
			break;
		}

		if (!MusicService.mediaPlayer.isPlaying()) {
			no_song = true;
			return;
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.UPDATE_UI);
		filter.addAction(Constant.UPDATE_SEEKBAR);
		registerReceiver(receiver, filter);
	
	}
	@Override
		protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.play_up, R.anim.play_down);
		if (no_song) {
			return;
		}
		unregisterReceiver(receiver);
		handler2.removeCallbacks(r);
	}
	class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constant.UPDATE_UI)) {
				updateUI();
			} else if (action.equals(Constant.UPDATE_SEEKBAR)) {
				seekBar.setProgress(intent.getIntExtra("progress", 0));
				int times = intent.getIntExtra("time", 0);
				date = new Date(times);
				sdf = new SimpleDateFormat("mm:ss");
				tv_curr_time.setText(sdf.format(date));
			}
		}

	}
	

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	//	getActivity().stopService(intent);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//stopService(intent_service);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pre_play:
			isPlaying = true;
			playBtn.setImageResource(R.drawable.pause_icon);
			MusicActivity.currentPos--;
			if (MusicActivity.currentPos < 0) {
				MusicActivity.currentPos = MusicActivity.currentMusics.size() - 1;
			}
			String path_pre = MusicActivity.currentMusics.get(
					MusicActivity.currentPos).getUrl();
			BroadUtil.sendPlayBroad(this, path_pre, false);
			// 列表更新
			switch (whichList) {
			case Constant.LOCAL:
				MusicActivity.localAdapter.colorPos = MusicActivity.currentPos;
				break;
			}
			updateUI();
			break;
		case R.id.play_pause:
			if (isPlaying) {
				// 暂停
				playBtn.setImageResource(R.drawable.play_icon);

				BroadUtil.sendPauseBroad(this);
				// 列表更新
				switch (whichList) {
				case Constant.LOCAL:
					MusicActivity.localAdapter.colorPos = -1;
					break;
				}
			} else {
				// 播放音乐
				playBtn.setImageResource(R.drawable.pause_icon);
				String path = MusicActivity.currentMusics.get(
						MusicActivity.currentPos).getUrl();
				BroadUtil.sendPlayBroad(this, path, true);
				// 列表更新
				switch (whichList) {
				case Constant.LOCAL:
					MusicActivity.localAdapter.colorPos = MusicActivity.currentPos;
					break;
				}
			}
			isPlaying = !isPlaying;
			break;
		case R.id.next_play:
			isPlaying = true;
			playBtn.setImageResource(R.drawable.pause_icon);
			MusicActivity.currentPos++;
			if (MusicActivity.currentPos > MusicActivity.currentMusics.size() - 1) {
				MusicActivity.currentPos = 0;
			}
			String path_next = MusicActivity.currentMusics.get(
					MusicActivity.currentPos).getUrl();
			BroadUtil.sendPlayBroad(this, path_next, false);
			// 列表的更新
			switch (whichList) {
			case Constant.LOCAL:
				MusicActivity.localAdapter.colorPos = MusicActivity.currentPos;
				break;
			}
			updateUI();
			break;
		case R.id.tv_back:
			finish();
		    overridePendingTransition(R.anim.roll, R.anim.down);
			break;
		}
	}
	private void updateUI() {
		int times = MusicActivity.currentMusics.get(MusicActivity.currentPos)
				.getDuration();
		date = new Date(times);
		sdf = new SimpleDateFormat("mm:ss");
		tv_total_time.setText(sdf.format(date));
		handler2.post(r);
	}
}
