package com.mo.adapter;

import java.util.ArrayList;

import com.mo.bean.Constant;
import com.mo.bean.Music;
import com.mo.db.Loc_MusicDao;
import com.mo.util.BroadUtil;
import com.mo.view.MusicActivity;
import com.mo.yoo.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LocalAdapter extends BaseAdapter {

	private ArrayList<Music> musics = new ArrayList<Music>();
	private Context context;
	private LayoutInflater inflater;
	Loc_MusicDao localDao;
	//CollectionDao2 collectionDao2;
	AlertDialog.Builder builder;
	public void updateOneView(ListView listView, int position){
		int firstVisiblePos = listView.getFirstVisiblePosition();
		View view = listView.getChildAt(position - firstVisiblePos);
		((TextView)view.findViewById(R.id.title)).setTextColor(context
				.getResources().getColor(R.color.black));
	}
	
	public LocalAdapter(Context context, ArrayList<Music> musics){
		this.context = context;
		this.musics = musics;
		inflater = LayoutInflater.from(context);
		localDao = new Loc_MusicDao(context);
	//	collectionDao2 = new CollectionDao2(context);
		builder = new AlertDialog.Builder(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return musics.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * showPos显示下拉按钮
	 * colorPos存放蓝色LinearLayout显示的position
	 * */
	public int showPos = -1;
	public int colorPos = -1;
	
	public void setShowPos(int showPos){
		this.showPos = showPos;
		notifyDataSetChanged();
	}
	public void setColorPos(int colorPos){
		this.colorPos = colorPos;
		notifyDataSetChanged();
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			
			convertView = inflater.inflate(R.layout.item_local_music, null);
			holder = new ViewHolder();
			holder.songName = (TextView) convertView.findViewById(R.id.title);
			holder.authorName = (TextView) convertView.findViewById(R.id.artist);
			holder.iv_show = (ImageView) convertView.findViewById(R.id.iv_show);
			holder.showLinear = (LinearLayout) convertView.findViewById(R.id.local_fenxiang);
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.delete = (TextView) convertView.findViewById(R.id.shanchu);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.songName.setText(musics.get(position).getTitle());
		holder.authorName.setText(musics.get(position).getArtist()+"-"+musics.get(position).getAlbum());
		
		if(position == colorPos){
			//当前音乐被选中，字体为绿色
			holder.songName.setTextColor(context.getResources().getColor(
					R.color.darkyellow));
			holder.authorName.setTextColor(context.getResources().getColor(
					R.color.darkGreen));
		}else{
			holder.songName.setTextColor(context.getResources().getColor(
					R.color.dark_hese));
			holder.authorName.setTextColor(context.getResources().getColor(
					R.color.qingZi));
		}
		
		//setMusicIcon(position, holder);
		if(position == showPos){
			holder.showLinear.setVisibility(View.VISIBLE);
			//产生从左出来的动画效果
			holder.showLinear.startAnimation(AnimationUtils.loadAnimation(
					context, R.anim.tran));
		}else{
			holder.showLinear.setVisibility(View.GONE);
		}
		holder.iv_show.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(position == showPos){
					showPos = -1;
					notifyDataSetChanged();
				}else{
					setShowPos(position);
				}
			}
		});
		holder.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				builder.setIcon(R.drawable.top6);
				builder.setTitle("Miracles happen every day!");
				builder.setMessage("确定从列表中删除此歌曲?");
				builder.setPositiveButton("确定", 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								localDao.delete(musics.get(position).getUrl());
								
								MusicActivity.localMusics.remove(position);
								Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
								showPos = -1;
								if (colorPos != -1) {
									if (colorPos == position) {
										BroadUtil.sendPauseBroad(context);
										colorPos = -1;
									} else if(position < colorPos){
										colorPos--;
										MusicActivity.currentPos--;
									} else if(position > colorPos){
										
									}
									MusicActivity.setCurrentList(Constant.LOCAL);
								}
								notifyDataSetChanged();
							}
				});
				builder.setNegativeButton("取消", 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
							}
				}).create().show();	
			}
		});
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
			holder.iv_icon.setImageResource(R.drawable.icon1);
			break;
		case 1:
			holder.iv_icon.setImageResource(R.drawable.icon2);
			break;
		case 2:
			holder.iv_icon.setImageResource(R.drawable.icon3);
			break;
		case 3:
			holder.iv_icon.setImageResource(R.drawable.icon4);
			break;
		case 4:
			holder.iv_icon.setImageResource(R.drawable.icon5);
			break;
		default:
			break;
		}
	}
	 class ViewHolder{
		TextView songName;
		TextView authorName;
		ImageView iv_show;
		LinearLayout showLinear, showItem;
		TextView  delete, fenxiang;
		ImageView iv_icon;
	}
}
