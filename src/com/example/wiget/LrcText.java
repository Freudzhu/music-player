package com.example.wiget;

import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import com.example.util.LrcProcessor.timelrc;
public class LrcText extends TextView {  
    private static final String TAG = "LyricView";  
      
    private Paint NotCurrentPaint; // 非当前歌词画笔  
    private Paint CurrentPaint; // 当前歌词画笔  
    private int notCurrentPaintColor = Color.WHITE;// 非当前歌词画笔 颜色  
    private int CurrentPaintColor = Color.RED; // 当前歌词画笔 颜色  
    private Typeface Texttypeface = Typeface.SERIF;  
    private Typeface CurrentTexttypeface = Typeface.DEFAULT_BOLD;  
    private float width;  
      
    private int brackgroundcolor = Color.BLACK; // 背景颜色  
    private float lrcTextSize = 22; // 歌词大小  
    private float CurrentTextSize = 24;  
    // private Align = Paint.Align.CENTER；  
    public float mTouchHistoryY;  
    private int height;  
    private long currentDunringTime; // 当前行歌词持续的时间，用该时间来sleep  
      
    private int TextHeight = 50; // 每一行的间隔  
    private boolean lrcInitDone = false;// 是否初始化完毕了  
    public int index = 0;  
      
    private static Vector<timelrc> lrclist;  
    private long currentTime;  
    private long sentenctTime;  
      
    public void SetTimeLrc(Vector<timelrc> list){  
        lrclist = list;       
    }  
    public Paint getNotCurrentPaint() {  
        return NotCurrentPaint;  
    }  
    public void setNotCurrentPaint(Paint notCurrentPaint) {  
        NotCurrentPaint = notCurrentPaint;  
    }  
    public boolean isLrcInitDone() {  
        return lrcInitDone;  
    }  
    public Typeface getCurrentTexttypeface() {  
        return CurrentTexttypeface;  
    }  
    public void setCurrentTexttypeface(Typeface currrentTexttypeface) {  
        CurrentTexttypeface = currrentTexttypeface;  
    }  
    public void setLrcInitDone(boolean lrcInitDone) {  
        this.lrcInitDone = lrcInitDone;  
    }  
    public float getLrcTextSize() {  
        return lrcTextSize;  
    }  
    public void setLrcTextSize(float lrcTextSize) {  
        this.lrcTextSize = lrcTextSize;  
    }  
    public float getCurrentTextSize() {  
        return CurrentTextSize;  
    }  
    public void setCurrentTextSize(float currentTextSize) {  
        CurrentTextSize = currentTextSize;  
    }  
    public Paint getCurrentPaint() {  
        return CurrentPaint;  
    }  
    public void setCurrentPaint(Paint currentPaint) {  
        CurrentPaint = currentPaint;  
    }  
    public int getNotCurrentPaintColor() {  
        return notCurrentPaintColor;  
    }  
    public void setNotCurrentPaintColor(int notCurrentPaintColor) {  
        this.notCurrentPaintColor = notCurrentPaintColor;  
    }  
    public int getCurrentPaintColor() {  
        return CurrentPaintColor;  
    }  
    public void setCurrentPaintColor(int currrentPaintColor) {  
        CurrentPaintColor = currrentPaintColor;  
    }  
    public Typeface getTexttypeface() {  
        return Texttypeface;  
    }  
    public void setTexttypeface(Typeface texttypeface) {  
        Texttypeface = texttypeface;  
    }  
    public int getBrackgroundcolor() {  
        return brackgroundcolor;  
    }  
    public void setBrackgroundcolor(int brackgroundcolor) {  
        this.brackgroundcolor = brackgroundcolor;  
    }  
    public int getTextHeight() {  
        return TextHeight;  
    }  
    public void setTextHeight(int textHeight) {  
        TextHeight = textHeight;  
    }  
    public LrcText(Context context) {  
        super(context);  
        init();  
    }  
    public LrcText(Context context, AttributeSet attr) {  
        super(context, attr);  
        init();  
    }  
    public LrcText(Context context, AttributeSet attr, int i) {  
        super(context, attr, i);  
        init();  
    }  
    private void init() {  
        setFocusable(true);  
      
        // 非高亮部分  
        NotCurrentPaint = new Paint();  
        NotCurrentPaint.setAntiAlias(true);  
        NotCurrentPaint.setTextAlign(Paint.Align.CENTER);  
        // 高亮部分 当前歌词  
        CurrentPaint = new Paint();  
        CurrentPaint.setAntiAlias(true);  
        // CurrentPaint.setColor(CurrentPaintColor);  
        CurrentPaint.setTextAlign(Paint.Align.CENTER);  
                  
    }  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
          
        canvas.drawColor(brackgroundcolor);  
        NotCurrentPaint.setColor(notCurrentPaintColor);  
        CurrentPaint.setColor(CurrentPaintColor);  
        NotCurrentPaint.setTextSize(lrcTextSize);  
        // NotCurrentPaint.setColor(notCurrentPaintColor);  
        NotCurrentPaint.setTypeface(Texttypeface);  
        CurrentPaint.setTextSize(lrcTextSize);  
        CurrentPaint.setTypeface(CurrentTexttypeface);  
        if (index == -1)  
            return;  
          
//      float plus = 5;  
        float plus = currentDunringTime == 0 ? 20  
                : 20  
                        + (((float) currentTime - (float) sentenctTime) / (float) currentDunringTime)  
                        * (float) 20;  
        // 向上滚动 这个是根据歌词的时间长短来滚动，整体上移  
        canvas.translate(0, -plus);  
        // 先画当前行，之后再画他的前面和后面，这样就保持当前行在中间的位置  
        try {  
            canvas.drawText(lrclist.get(index).getLrcString(), width / 2,  
                    height / 2, CurrentPaint);  
            // canvas.translate(0, plus);  
            float tempY = height / 2;  
            // 画出本句之前的句子  
            for (int i = index - 1; i >= 0; i--) {  
                // Sentence sen = list.get(i);  
                // 向上推移  
                tempY = tempY - TextHeight;  
                if (tempY < 0) {  
                    break;  
                }  
                canvas.drawText(lrclist.get(i).getLrcString(), width / 2,  
                        tempY, NotCurrentPaint);  
                // canvas.translate(0, TextHeight);  
            }  
            tempY = height / 2;  
            // 画出本句之后的句子  
            for (int i = index + 1; i < lrclist.size(); i++) {  
                // 往下推移  
                tempY = tempY + TextHeight;  
                if (tempY > height) {  
                    break;  
                }  
                canvas.drawText(lrclist.get(i).getLrcString(), width / 2,  
                        tempY, NotCurrentPaint);  
                // canvas.translate(0, TextHeight);  
            }  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
    }  
    protected void onSizeChanged(int w, int h, int ow, int oh) {  
        super.onSizeChanged(w, h, ow, oh);  
        width = w; // remember the center of the screen  
        height = h;  
        // middleY = h * 0.5f;  
    }  
    //  
    /** 
     * @param time 
     *            当前歌词的时间轴 
     *  
     * @return null 
     */  
    public void SetNowPlayIndex(int i,int time) {  
        this.currentTime = time;  
//      // 歌词序号  
        index = i;  
        this.invalidate();  
        if (index != -1) {  
            sentenctTime = lrclist.get(index).getTimePoint();  
            currentDunringTime = lrclist.get(index).getSleepTime();  
//          Log.d(TAG,"sentenctTime = "+sentenctTime+",  currentDunringTime = "+currentDunringTime);  
        }  
    }  
}  