package com.mo.bean;

import java.util.Arrays;

public class WeatherInfo {

	private String data;
	private String wenDu;
	private String shiDSu;
	private String fengLi;
	private String weather;
	private String day_weather;
	
	public String getDay_weather() {
		return day_weather;
	}
	public void setDay_weather(String day_weather) {
		this.day_weather = day_weather;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getWenDu() {
		return wenDu;
	}
	public void setWenDu(String wenDu) {
		this.wenDu = wenDu;
	}
	public String getShiDSu() {
		return shiDSu;
	}
	public void setShiDSu(String shiDSu) {
		this.shiDSu = shiDSu;
	}
	public String getFengLi() {
		return fengLi;
	}
	public void setFengLi(String fengLi) {
		this.fengLi = fengLi;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	@Override
	public String toString() {
		return "WeatherInfo [data=" + data + ", wenDu=" + wenDu + ", shiDSu="
				+ shiDSu + ", fengLi=" + fengLi + ", weather=" + weather
				+ ", day_weather=" + day_weather + "]";
	}
}
