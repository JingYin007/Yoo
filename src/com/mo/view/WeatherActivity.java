package com.mo.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocalDayWeatherForecast;
import com.amap.api.location.AMapLocalWeatherForecast;
import com.amap.api.location.AMapLocalWeatherListener;
import com.amap.api.location.AMapLocalWeatherLive;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.mo.adapter.WeatherAdapter;
import com.mo.bean.WeatherInfo;
import com.mo.yoo.R;
 
/**
 * 实时天气示例
 * 获取当前地区天气情况
 * */
public class WeatherActivity extends Activity implements
		AMapLocalWeatherListener {
	private LocationManagerProxy mLocationManagerProxy;

	private ArrayList<WeatherInfo> weatherInfos;
	private WeatherAdapter weatherAdapter;
	private TextView tv_back;// 返回
	private TextView tv_diDian;// 地点
	private TextView tv_weather;// 天气
	private TextView tv_wenDu;// 气温
	private TextView tv_fengLi;// 风力
	private TextView tv_shiDu;// 空气湿度
	//private TextView tv_data;// 发布时间
	private ImageView iv_weather;//天气图片
	private RelativeLayout rl_weather;//天气背景

	private ListView lv_weather;//未来天气列表
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.push_right_in,R.anim.hold);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_view);
		init();
		initView();
 
		iv_weather.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 获取未来天气
				init2();
			}
		});
		tv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.hold,R.anim.push_right_out);
			}
		});
	}

	/**
	 * 获取实时天气预报
	 */
	private void init() {
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.requestWeatherUpdates(
				LocationManagerProxy.WEATHER_TYPE_LIVE, this);
	}
	/**
	 * 获取未来天气预报
	 */
	private void init2() {
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.requestWeatherUpdates(
				LocationManagerProxy.WEATHER_TYPE_FORECAST, this);
	}
	/**
	 * 初始化控件
	 */
	private void initView() {
		tv_back=(TextView) findViewById(R.id.tv_back);
		tv_diDian = (TextView) findViewById(R.id.tv_diDian);
		tv_weather = (TextView) findViewById(R.id.tv_weather);
		tv_wenDu = (TextView) findViewById(R.id.tv_wenDu);
		tv_fengLi = (TextView) findViewById(R.id.tv_fengLi);
		tv_shiDu = (TextView) findViewById(R.id.tv_shiDu);
		iv_weather=(ImageView) findViewById(R.id.iv_weather);
		rl_weather=(RelativeLayout) findViewById(R.id.rl_weather);
		//tv_data = (TextView) findViewById(R.id.tv_date);
		lv_weather=(ListView) findViewById(R.id.lv_weather);
	}

	@Override
	public void onWeatherForecaseSearched(AMapLocalWeatherForecast aMapLocalWeatherForecast) {
		// 未来天气预报回调
		if (aMapLocalWeatherForecast != null
				 &&aMapLocalWeatherForecast.getAMapException().getErrorCode() == 0) {
			weatherInfos=new ArrayList<WeatherInfo>();
			
			List<AMapLocalDayWeatherForecast> forcasts = aMapLocalWeatherForecast
					.getWeatherForecast();
			for (int i = 0; i < forcasts.size(); i++) {
				AMapLocalDayWeatherForecast forcast = forcasts.get(i);
				WeatherInfo weatherInfo =new WeatherInfo();
				weatherInfo.setData(forcast.getDate());
				weatherInfo.setFengLi(forcast.getDayWindDir()+"风"+forcast.getDayWindPower()+"级");
				weatherInfo.setWenDu(forcast.getDayTemp()+"℃");
				weatherInfo.setDay_weather(forcast.getDayWeather());
				weatherInfo.setWeather(forcast.getDayWeather()+"|"+forcast.getNightWeather());
				
				/*Log.d("motou", "-------weather: "+forcast.getDayTemp()+forcast.getDayWeather()+forcast.getDayWindDir()
						+forcast.getDayWindPower()+forcast.getNightTemp()+forcast.getNightWeather()
						+forcast.getNightWindDir()+forcast.getNightWindPower()+forcast.getProvince()
						+forcast.getWeek());*/
				weatherInfos.add(weatherInfo);
			}
		} else {

			// 获取天气预报失败
			Toast.makeText(
					this,
					"获取天气预报失败:"
							+ aMapLocalWeatherForecast.getAMapException()
									.getErrorMessage(), Toast.LENGTH_SHORT)
					.show();
		}
		weatherAdapter=new WeatherAdapter(WeatherActivity.this, weatherInfos);
		lv_weather.setAdapter(weatherAdapter);
	}

	@Override
	public void onWeatherLiveSearched(AMapLocalWeatherLive Tag) {

		if (Tag != null && Tag.getAMapException().getErrorCode() == 0) {
			// 天气预报成功回调 设置天气信息
			tv_diDian.setText(Tag.getCity());
			tv_weather.setText(Tag.getWeather());
			tv_wenDu.setText(Tag
					.getTemperature()+"℃");
			tv_fengLi.setText("风力状况："+Tag.getWindDir()+"风 "
					+Tag.getWindPower()+"级");
			tv_shiDu.setText("空气湿度："+Tag.getHumidity()+"%");
			//tv_data.setText(Tag.getReportTime());
			
			String img=tv_weather.getText().toString();
			if (img.equals("晴")) {
				iv_weather.setImageResource(R.drawable.weather_sun);
				rl_weather.setBackgroundResource(R.drawable.bg0_fine_day);
				
			}else if (img.equals("阴")) {
				iv_weather.setImageResource(R.drawable.weather_yin);
				rl_weather.setBackgroundResource(R.drawable.bg_fog_day);
			}
			else if (img.contains("雨")) {
				if (img.equals("雷阵雨") ){
					iv_weather.setImageResource(R.drawable.weather_leizhenyu);
					rl_weather.setBackgroundResource(R.drawable.bg_thunder_storm);
				}else {
					iv_weather.setImageResource(R.drawable.weather_clody);
					rl_weather.setBackgroundResource(R.drawable.bg_rain);
				}
				
			}
			else{
				iv_weather.setImageResource(R.drawable.weather_normal);
				rl_weather.setBackgroundResource(R.drawable.bg_na);
			}
			
		} else {

			// 获取天气预报失败
			Toast.makeText(
					this,
					"获取天气预报失败:"
							+ Tag.getAMapException()
									.getErrorMessage(), Toast.LENGTH_SHORT)
					.show();
		}
	}
 
	
	@Override
	protected void onPause() {
		super.onPause();
		init();
		// 销毁定位
		mLocationManagerProxy.destroy();
	}
	protected void onDestroy() {
		super.onDestroy();
	}
}
