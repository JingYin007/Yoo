package com.mo.bean;

/**
 * �ʼ���Ϣ�࣬��Ӧ��������Ե�����
 * @author zhanghuajian
 *
 */
public class NoteInfo {
	int id;
	String title;
	String content;
	String time;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public NoteInfo(int id,String title,String content,String time) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.title=title;
		this.content=content;
		this.time=time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
