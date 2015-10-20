package com.mo.music;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.mo.music.MusicLoader.MusicInfo;
import com.mo.music.MusicPlayService.NatureBinder;
import com.mo.yoo.R;

public class MusicView extends Activity implements OnClickListener{

	public static final String TAG = "com.example.nature.MAIN_ACTIVITY";
	
	private ListView lvSongs;	
	private SeekBar pbDuration;
	private TextView tvCurrentMusic,tv_back;
	private List<MusicInfo> musicList;
	private int currentMusic; // The music that is playing.
	private int currentPosition; //The position of the music is playing.
	private int currentMax;
	private Button btnStartStop;
//	private Button btnNext;
	private Button btn_MusicDetail;
	
	private ProgressReceiver progressReceiver;	
	public static NatureBinder natureBinder;	
	
	public static ServiceConnection serviceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			natureBinder = (NatureBinder) service;			
		}
	};
	
	private void connectToNatureService(){		
		Intent intent = new Intent(MusicView.this, MusicPlayService.class);				
		bindService(intent, serviceConnection, BIND_AUTO_CREATE);				
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "OnCreate");
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.push_right_in,R.anim.hold);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.music_list_view);		
		MusicLoader musicLoader = MusicLoader.instance(getContentResolver());		
		musicList = musicLoader.getMusicList();
		connectToNatureService();
		initComponents();		
	}
	
	public void onResume(){
		Log.v(TAG, "OnResume register Progress Receiver");
		super.onResume();										
		registerReceiver();
		if(natureBinder != null){
			if(natureBinder.isPlaying()){
				btnStartStop.setBackgroundResource(R.drawable.music_pause);
			}else{
				btnStartStop.setBackgroundResource(R.drawable.music_start);
			}
			natureBinder.notifyActivity();
		}
	}
	
	public void onPause(){
		Log.v(TAG, "OnPause unregister Progress Receiver");
		super.onPause();
		unregisterReceiver(progressReceiver);
	}
	
	public void onStop(){
		Log.v(TAG, "OnStop");
		super.onStop();				
	}
	
	public void onDestroy(){
		Log.v(TAG, "OnDestroy");
		super.onDestroy();
		if(natureBinder != null){
			unbindService(serviceConnection);
		}
	}
		
	private void initComponents(){		
		pbDuration = (SeekBar) findViewById(R.id.pbDuration);	
		pbDuration.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(fromUser){
					natureBinder.changeProgress(progress);
				}
			}
		});
		
		tvCurrentMusic = (TextView) findViewById(R.id.tvCurrentMusic);				
		tv_back=(TextView) findViewById(R.id.tv_back);
		tv_back.setOnClickListener(this);
		btnStartStop = (Button)findViewById(R.id.btnStartStop);
		btnStartStop.setOnClickListener(this);
		
		
		
		//btnNext = (Button)findViewById(R.id.btnNext);
		//btnNext.setOnClickListener(this);
		
		btn_MusicDetail = (Button) findViewById(R.id.btn_MusicDetail);
		btn_MusicDetail.setOnClickListener(this);
		
		MusicAdapter adapter = new MusicAdapter();
		lvSongs = (ListView) findViewById(R.id.lvSongs);		
		lvSongs.setAdapter(adapter);
		lvSongs.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentMusic = position;
				natureBinder.startPlay(currentMusic,0);
				if(natureBinder.isPlaying()){					
					btnStartStop.setBackgroundResource(R.drawable.music_pause);		
				}
			}
		});
	}
	
	private void registerReceiver(){
		progressReceiver = new ProgressReceiver();	
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MusicPlayService.ACTION_UPDATE_PROGRESS);
		intentFilter.addAction(MusicPlayService.ACTION_UPDATE_DURATION);
		intentFilter.addAction(MusicPlayService.ACTION_UPDATE_CURRENT_MUSIC);
		registerReceiver(progressReceiver, intentFilter);
	}
	
	class MusicAdapter extends BaseAdapter{

		@Override 
		public int getCount() {
			return musicList.size();
		}

		@Override
		public Object getItem(int position) {
			return musicList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return musicList.get(position).getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder; 
			if(convertView == null){
				convertView = LayoutInflater.from(MusicView.this).inflate(R.layout.item_music, null);
				ImageView pImageView = (ImageView) convertView.findViewById(R.id.iv_icon);
				TextView pTitle = (TextView) convertView.findViewById(R.id.title);
				TextView pDuration = (TextView) convertView.findViewById(R.id.tv_time);
				TextView pArtist = (TextView) convertView.findViewById(R.id.artist);
				viewHolder = new ViewHolder(pImageView, pTitle, pDuration, pArtist);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			//setMusicIcon(position, viewHolder);
			
				viewHolder.imageView.setImageResource(R.drawable.music);
				viewHolder.title.setText(musicList.get(position).getTitle());
				viewHolder.duration.setText(FormatHelper.formatDuration(musicList.get(position).getDuration()));
				viewHolder.artist.setText(musicList.get(position).getArtist());
			
			return convertView;
		}		
		/**
		 * 显示对应歌曲的图标，有趣
		 * 测试是否影响加载速度而去掉该功能...
		 * @param position
		 * @param holder
		 */
		private void setMusicIcon(final int position, ViewHolder holder) {
			switch (position%5) {
			case 0:
				holder.imageView.setImageResource(R.drawable.icon1);
				break;
			case 1:
				holder.imageView.setImageResource(R.drawable.icon2);
				break;
			case 2:
				holder.imageView.setImageResource(R.drawable.icon3);
				break;
			case 3:
				holder.imageView.setImageResource(R.drawable.icon4);
				break;
			case 4:
				holder.imageView.setImageResource(R.drawable.icon5);
				break;
			default:
				break;
			}
		}
	}
	
	class ViewHolder{
		public ViewHolder(ImageView pImageView, TextView pTitle, TextView pDuration, TextView pArtist){
			imageView = pImageView;
			title = pTitle;
			duration = pDuration;
			artist = pArtist;
		}
		
		ImageView imageView;
		TextView title;
		TextView duration;
		TextView artist;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {		
		switch (v.getId()) {		
		case R.id.btnStartStop:		
			play(currentMusic,R.id.btnStartStop);
			break;		
		case R.id.btnNext:
			natureBinder.toNext();
			break;		
		case R.id.btn_MusicDetail:						
			Intent intent = new Intent(MusicView.this,PlayerView.class);
			intent.putExtra(PlayerView.MUSIC_LENGTH, currentMax);
			intent.putExtra(PlayerView.CURRENT_MUSIC, currentMusic);
			intent.putExtra(PlayerView.CURRENT_POSITION, currentPosition);			
			startActivity(intent);			
			break;
		case R.id.tv_back:
			finish();
			overridePendingTransition(R.anim.hold, R.anim.push_right_out);
			break;
		}		
	}
	
	private void play(int position, int resId){		
		if(natureBinder.isPlaying()){
			natureBinder.stopPlay();
			btnStartStop.setBackgroundResource(R.drawable.music_start);
		}else{
			natureBinder.startPlay(position,currentPosition);
			btnStartStop.setBackgroundResource(R.drawable.music_pause);
		}
	}

	

	class ProgressReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(MusicPlayService.ACTION_UPDATE_PROGRESS.equals(action)){
				int progress = intent.getIntExtra(MusicPlayService.ACTION_UPDATE_PROGRESS, 0);
				if(progress > 0){
					currentPosition = progress; // Remember the current position
					pbDuration.setProgress(progress / 1000);
				}
			}else if(MusicPlayService.ACTION_UPDATE_CURRENT_MUSIC.equals(action)){
				//Retrive the current music and get the title to show on top of the screen.
				currentMusic = intent.getIntExtra(MusicPlayService.ACTION_UPDATE_CURRENT_MUSIC, 0);				
				tvCurrentMusic.setText(musicList.get(currentMusic).getTitle());
			}else if(MusicPlayService.ACTION_UPDATE_DURATION.equals(action)){
				//Receive the duration and show under the progress bar
				//Why do this ? because from the ContentResolver, the duration is zero.
				currentMax = intent.getIntExtra(MusicPlayService.ACTION_UPDATE_DURATION, 0);				
				int max = currentMax / 1000;
				Log.v(TAG, "[Main ProgressReciver] Receive duration : " + max);
				pbDuration.setMax(currentMax / 1000);						
			}
		}
		
	}
	
}
