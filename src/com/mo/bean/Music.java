package com.mo.bean;

public class Music {
	/**
	 * whichList��ѡ����·��:���أ�0�������磨1�� 
	 * title ������
	 * artist ����
	 * album ר��
	 * url ����·��
	 * duration ����ʱ��
	 * img_url ͼƬ·��
	 * lrc_url ���·��
	 * */
	private int whichList;
	private String title;
	private String artist;
	private String album;
	private String url;
	private int duration;
	private String img_url;
	private String lrc_url;
	
	public int getWhichList() {
		return whichList;
	}
	public void setWhichList(int whichList) {
		this.whichList = whichList;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getAlbum() {
		return album;
	}
	public void setAblum(String ablum) {
		this.album = ablum;
	}
	public String getUrl() {
		if (whichList == Constant.NET) {
			return Constant.SERVER_IP + url;
		}
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getLrc_url() {
		return lrc_url;
	}
	public void setLrc_url(String lrc_url) {
		this.lrc_url = lrc_url;
	}
	@Override
	public String toString() {
		return "Music [whichList=" + whichList + ", title=" + title
				+ ", artist=" + artist + ", album=" + album + ", url=" + url
				+ ", duration=" + duration + ", img_url=" + img_url
				+ ", lrc_url=" + lrc_url + "]";
	}
	
}
