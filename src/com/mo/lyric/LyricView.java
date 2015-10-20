package com.mo.lyric;

import java.util.ArrayList;
import java.util.List;




import com.mo.bean.Constant;
import com.mo.yoo.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.TextView;

public class LyricView extends TextView {
	private List<LyricBean> list = new ArrayList<LyricBean>();
	
	private float width ;
	private float high;
	private Paint currentPaint ;
	private Paint notCurrentPaint ;
	//private float textSize = 18;
	//private float textHigh = 28;
	
	public void setList(List<LyricBean> list) {
		this.list = list;
	}
	
	private void init(){
		setFocusable(true);
		
		currentPaint = new Paint();
		currentPaint.setAntiAlias(true);
		currentPaint.setColor(Color.YELLOW);
		currentPaint.setTextSize(Constant.TEXTSIZE);
		currentPaint.setTypeface(Typeface.SERIF);
		currentPaint.setTextAlign(Align.CENTER);
		
		notCurrentPaint = new Paint();
		notCurrentPaint.setAntiAlias(true);
		notCurrentPaint.setColor(Color.WHITE);
		notCurrentPaint.setTextSize(Constant.NOTTEXTSIZE);
		notCurrentPaint.setTypeface(Typeface.SERIF);
		notCurrentPaint.setTextAlign(Align.CENTER);
		
	}
	
	int index = 0 ;//当前正在唱的行,动态变化
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (canvas == null) {
			return ;
		}
		if (list.size() == 0) {
			return ;
		}
		canvas.drawText(list.get(index).getSentences(), width/2, high/2, currentPaint);
		
		//画上半部分
		float tempY = high/2;
		for (int i = index - 1; i >= 0; i--) {
			tempY = tempY - Constant.TEXTHIGH;
			canvas.drawText(list.get(i).getSentences(), width/2, tempY, notCurrentPaint);
		}
		
		//画下半部分
		tempY = high/2;
		for (int i = index+1; i < list.size(); i++) {
			tempY = tempY + Constant.TEXTHIGH;
			canvas.drawText(list.get(i).getSentences(), width/2, tempY, notCurrentPaint);
		}
	}
	
	int newIndex = 0;
	public void updateCurrentIndex(MediaPlayer mediaPlayer){
		int currentTime = 0;
		int time = 0;
		if (mediaPlayer.isPlaying()) {
			time = mediaPlayer.getDuration();
			currentTime = mediaPlayer.getCurrentPosition();
		}
		if (currentTime < time) {
			for (int i = 0; i < list.size(); i++) {
				if (i == 0 && currentTime < list.get(i).getTime()) {
					newIndex = i;
					break;
				}
				if (i == list.size() - 1&& currentTime > list.get(i).getTime()) {
					newIndex = i;
					break;
				}
				if (i < list.size() - 1) {
					if (currentTime > list.get(i).getTime()&& currentTime < list.get(i+1).getTime()) {
						newIndex = i;
						break;
					}
				}
			}
		}
		this.index = newIndex;
	}
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		this.width = w;
		this.high = h;
	}
	
	public LyricView(Context context) {
		super(context);
		init();
	}

	public LyricView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public LyricView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	
	
	

}
