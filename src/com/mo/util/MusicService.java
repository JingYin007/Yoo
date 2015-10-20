package com.mo.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.mo.bean.Constant;
import com.mo.view.MusicActivity;
import com.mo.yoo.R;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;
import android.widget.Toast;

public class MusicService extends Service {

	public static final String PLAY = "com.anjoyo.playmusic";
	public static final String PAUSE = "com.anjoyo.pausemusic";
	public static final String DOWNLOAD = "com.anjoyo.download";

	MusicReceiver receiver = new MusicReceiver();
	public static MediaPlayer mediaPlayer;

	Handler handler = new Handler();
	MyRunnable r = new MyRunnable();
	NotificationManager manager;
	Notification notification;
	RemoteViews remoteViews;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.d("motou","MusicService——onBind()");
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.d("motou","MusicService——onCreate()");
		mediaPlayer = new MediaPlayer();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mediaPlayer.reset();
		mediaPlayer.release();
		unregisterReceiver(receiver);
		handler.removeCallbacks(r);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initNotification();
		IntentFilter filter = new IntentFilter();
		filter.addAction(PLAY);
		filter.addAction(PAUSE);
		filter.addAction(DOWNLOAD);
		registerReceiver(receiver, filter);
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				//歌曲默认循环播放
				MusicActivity.currentPos++;
				if (MusicActivity.currentPos > MusicActivity.currentMusics.size() - 1) {
					MusicActivity.currentPos = 0;
				}
				String path = MusicActivity.currentMusics.get(
						MusicActivity.currentPos).getUrl();
				playMusic(path, false);
				int whichList = MusicActivity.currentMusics.get(0)
						.getWhichList();
				switch (whichList) {
				case Constant.LOCAL:
					MusicActivity.localAdapter
							.setColorPos(MusicActivity.currentPos);
					break;
				default:
					break;
				}
				sendBroadcast(new Intent(Constant.UPDATE_UI));
			}
		});

		return super.onStartCommand(intent, flags, startId);
	}

	class MyRunnable implements Runnable {

		@Override
		public void run() {
			if (mediaPlayer.isPlaying() && mediaPlayer.getDuration() > 0) {
				Intent intent = new Intent(Constant.UPDATE_SEEKBAR);
				intent.putExtra("progress", mediaPlayer.getCurrentPosition()
						* 100 / mediaPlayer.getDuration());
				intent.putExtra("time", mediaPlayer.getCurrentPosition());
				sendBroadcast(intent);
				handler.postDelayed(r, 500);
			}
		}
	}

	class MusicReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			
			Log.d("motou", "接收广播：onReceive()");
			String action = intent.getAction();
			if (action.equals(PLAY)) {
				
				Log.d("motou", "接收广播：action.equals(PLAY)");
				playMusic(intent.getStringExtra("path"),
						intent.getBooleanExtra("isContinue", false));
			} else if (action.equals(PAUSE)) {
				pauseMusic();
			} else if (action.equals(DOWNLOAD)) {
				String url = intent.getStringExtra("url");
				String title = intent.getStringExtra("title");
				int id = intent.getIntExtra("position", 0);
				// 下载任务
				downLoad(url, title, id);
			}
		}
	}

	private void initNotification() {}

	public void downLoad(String url, final String title, final int id) {
		// Environment.getExternalStorageDirectory()相当于mnt/sdcord
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/MusicWork/downMusic/" + title + ".mp3");
		if (file.exists()) {
			Toast.makeText(this, "文件已下载", Toast.LENGTH_SHORT).show();
			return;
		} else {
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FinalHttp finalHttp = new FinalHttp();
		url = encodeMp3Url(url, "utf-8");
		finalHttp.download(url, file.getAbsolutePath(),
				new AjaxCallBack<File>() {

					@Override
					public void onSuccess(File t) {
						super.onSuccess(t);
						Toast.makeText(getApplicationContext(),
								title + ".mp3下载完成", Toast.LENGTH_SHORT).show();
						updateRemoteViews(100, title, id);
					}

					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);
						updateRemoteViews((int) (current * 100 / count), title,
								id);
					}

					@Override
					public void onStart() {
						super.onStart();
					}
				});
	}

	private String encodeMp3Url(String str, String charset) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			int v = (int) c;
			if ((v >= 19968 && v <= 171941)) {
				try {
					str = str.replace(c + "",
							URLEncoder.encode(c + "", charset));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return str.replace(" ", "%20").replace("(", "%28").replace(")", "%29")
				.replace("&", "%26");
	}

	protected void updateRemoteViews(int progress, String title, int id) {}

	public void playMusic(String path, boolean isContinue) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
			mediaPlayer.start();
			if (isContinue) {
				mediaPlayer.seekTo(progress);
			}
			handler.post(r);
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int progress = 0;

	public void pauseMusic() {
		if (mediaPlayer.isPlaying()) {
			progress = mediaPlayer.getCurrentPosition();
			mediaPlayer.pause();
		}
	}

}
