package com.mo.adapter;
import java.util.ArrayList;





import com.mo.bean.WeatherInfo;
import com.mo.yoo.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherAdapter extends BaseAdapter{
	
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<WeatherInfo> weatherInfo;
	
	/**
	 * listView  ≈‰¿‡
	 */
	public WeatherAdapter(Context context, ArrayList<WeatherInfo> weatherInfo) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.weatherInfo = weatherInfo;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return weatherInfo.size();
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		
		
		if (convertView == null) {
			LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_weather , null);
			viewholder = new ViewHolder();
			viewholder.iv = (ImageView) convertView.findViewById(R.id.iv_weather);
			viewholder.tv_weather=(TextView) convertView.findViewById(R.id.tv_weather);
			viewholder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			viewholder.tv_wenDu = (TextView) convertView.findViewById(R.id.tv_wenDu);
			viewholder.tv_fengLi = (TextView) convertView.findViewById(R.id.tv_fengLi);
			convertView.setTag(viewholder);

		}
		viewholder = (ViewHolder) convertView.getTag();
		
		//viewholder.iv.setImageResource(weatherInfo.get(position).getData()); 
		viewholder.tv_weather.setText(weatherInfo.get(position).getWeather());
		viewholder.tv_date.setText(weatherInfo.get(position).getData());
		viewholder.tv_fengLi.setText(weatherInfo.get(position).getFengLi());
		viewholder.tv_wenDu.setText(weatherInfo.get(position).getWenDu());
		
		String img=weatherInfo.get(position).getDay_weather().toString();
		if (img.equals("«Á")) {
			viewholder.iv.setImageResource(R.drawable.weather_sun);
		}else if (img.equals("“ı")) {
			viewholder.iv.setImageResource(R.drawable.weather_yin);
		}
		else if (img.contains("”Í")) {
			if (img.equals("¿◊’Û”Í")) {
				viewholder.iv.setImageResource(R.drawable.weather_leizhenyu);
			}else {
				viewholder.iv.setImageResource(R.drawable.weather_rain);
			}
		}
		else{
			viewholder.iv.setImageResource(R.drawable.weather_normal);
		}
		
		return convertView;

	}
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		if (observer != null) {
	        super.unregisterDataSetObserver(observer);
	    }
	}
	class ViewHolder {
		public ImageView iv;
		public TextView tv_date, tv_weather, tv_wenDu,tv_fengLi;

	}	

}
