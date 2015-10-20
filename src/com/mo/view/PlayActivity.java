package com.mo.view;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mo.bean.Constant;
import com.mo.lyric.LyricReader;
import com.mo.lyric.LyricView;
import com.mo.util.BroadUtil;
import com.mo.util.MusicService;
import com.mo.util.MusicUtil;
import com.mo.yoo.R;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class PlayActivity extends Activity implements OnClickListener {

	private TextView tv_musicName, tv_total_time, tv_curr_time,back;
	private ImageView preBtn, playBtn, nextBtn;
	private SeekBar seekBar;
	private LinearLayout ll_bg_player;
	int whichList;
	boolean isPlaying = true;
	Receiver receiver = new Receiver();
	//用来设置时间格式
	private Date date;
	private SimpleDateFormat sdf;
	/*
	 * 描绘歌词所用
	 * */
	private LyricView lyricView;
	LyricReader lyricReader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.up, R.anim.roll);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.music_player_view);

		if (!MusicService.mediaPlayer.isPlaying()) {
			return;
		}
		init();
	}

	
	boolean no_song = false;
	@Override
	protected void onResume() {
		super.onResume();
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

	Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			//设置歌曲图片，当对应图片不存在时，选择默认
			case Constant.IMAGE_LOAD_OK:
				if (msg.obj == null) {
					//image.setImageResource(R.drawable.psb);
				} else {
					//image.setImageBitmap((Bitmap) msg.obj);
				}
				break;
			case Constant.LRC_PATH_LOAD_OK:
				String path = msg.obj.toString();
	
				try {
					lyricReader.read(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
				lyricView.setList(lyricReader.getList());
				handler2.post(r);
				break;
			}
		};
	};

	Runnable r = new Runnable(){

		@Override
		public void run() {
			lyricView.updateCurrentIndex(MusicService.mediaPlayer);
			lyricView.invalidate();
			handler2.postDelayed(r, 100);
		}
		
	};
	
	private void init() {
		tv_musicName = (TextView) findViewById(R.id.musicName);
		tv_total_time = (TextView) findViewById(R.id.total_time);
		tv_curr_time = (TextView) findViewById(R.id.pre_time);

		back = (TextView) findViewById(R.id.tv_back);
		preBtn = (ImageView) findViewById(R.id.pre_play);
		playBtn = (ImageView) findViewById(R.id.play_pause);
		nextBtn = (ImageView) findViewById(R.id.next_play);
		
		ll_bg_player=(LinearLayout) findViewById(R.id.bg_player);
		seekBar = (SeekBar) findViewById(R.id.seekbar);
		lyricView = (LyricView) findViewById(R.id.lyricView);
		lyricReader = new LyricReader();
		
		
		preBtn.setOnClickListener(this);
		playBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		back.setOnClickListener(this);

		whichList = MusicActivity.currentMusics.get(0).getWhichList();
		tv_musicName.setText(MusicActivity.currentMusics.get(MusicActivity.currentPos)
				.getTitle());
		
		int times = MusicActivity.currentMusics.get(MusicActivity.currentPos).getDuration();
		date = new Date(times);
		sdf = new SimpleDateFormat("mm:ss");
		tv_total_time.setText(sdf.format(date));
		
		if(whichList == Constant.LOCAL){
			
			String path = new MusicUtil().getLocalLrc(MusicActivity.currentMusics.get(MusicActivity.currentPos)
					.getTitle(), MusicActivity.currentMusics.get(MusicActivity.currentPos)
					.getArtist(), MusicActivity.allLrc);
				try {
					lyricReader.read(path);
					lyricView.setList(lyricReader.getList());
					handler2.post(r);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
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
		//RreashBgPlayer();
	}
/**
 * 刷新播放界面背景
 */
	private void RreashBgPlayer() {
		int bg_tag = MusicActivity.currentPos;
		switch (bg_tag %5 ) {
		case 0:
			ll_bg_player.setBackgroundResource(R.drawable.player1);
			break;
		case 1:
			ll_bg_player.setBackgroundResource(R.drawable.player2);
			break;
		case 2:
			ll_bg_player.setBackgroundResource(R.drawable.player3);
			break;
		default:
			break;
		}
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

	private void overridePendingTransition(int setActivity) {
		// TODO Auto-generated method stub
		
	}

	private void updateUI() {
		tv_musicName.setText(MusicActivity.currentMusics.get(MusicActivity.currentPos)
				.getTitle());
		int times = MusicActivity.currentMusics.get(MusicActivity.currentPos)
				.getDuration();
		date = new Date(times);
		sdf = new SimpleDateFormat("mm:ss");
		tv_total_time.setText(sdf.format(date));
		
		if(whichList == Constant.NET){
			new MusicUtil().loadImage(
					MusicActivity.currentMusics.get(MusicActivity.currentPos)
							.getImg_url(), handler2);
			new MusicUtil().loadLrcPath(
					MusicActivity.currentMusics.get(MusicActivity.currentPos)
					.getLrc_url(), handler2);
		}else{
			String path = new MusicUtil().getLocalLrc(MusicActivity.currentMusics.get(MusicActivity.currentPos)
					.getTitle(), MusicActivity.currentMusics.get(MusicActivity.currentPos)
					.getArtist(), MusicActivity.allLrc);
				try {
					lyricReader.read(path);
					lyricView.setList(lyricReader.getList());
					handler2.post(r);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
