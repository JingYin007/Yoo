package com.mo.adapter;

import java.util.ArrayList;

import com.amap.api.mapcore.util.bu;
import com.mo.bean.NoteInfo;
import com.mo.view.BookmarkActivity;
import com.mo.yoo.NoteFrag;
import com.mo.yoo.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Note_Adapter extends BaseAdapter{
	
	ArrayList<NoteInfo> note_Info = new ArrayList<NoteInfo>();
	private Context context;
	LayoutInflater inflater;
	AlertDialog.Builder builder;
	public Note_Adapter(Context context, ArrayList<NoteInfo> note_Info){
		this.context = context;
		this.note_Info = note_Info;
		builder=new AlertDialog.Builder(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return note_Info.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
		if(convertView == null){
			inflater = (LayoutInflater) context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_note, null);
			holder=new ViewHolder(); 
			holder.tv_title=(TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_date=(TextView) convertView.findViewById(R.id.tv_date);
			holder.iv_icon=(ImageView) convertView.findViewById(R.id.iv_icon);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv_title.setText(note_Info.get(position).getTitle());
		holder.tv_date.setText(note_Info.get(position).getTime());
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
		return convertView;
	}

	class ViewHolder{
		TextView tv_title,tv_date;
		ImageView iv_icon,iv_del;
	}
	
}
