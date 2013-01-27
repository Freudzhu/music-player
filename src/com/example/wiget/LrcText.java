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
    private Paint NotCurrrentPaint; // 非当前歌词画笔  
    private Paint CurrentPaint; // 当前歌词画笔  
    private int notCurrrentPaintColor = Color.WHITE;// 非当前歌词画笔 颜色  
    private int CurrrentPaintColor = Color.RED; // 当前歌词画笔 颜色  
    private Typeface Texttypeface = Typeface.SERIF;  
    private float width;  
    private static Lyric mLyric;  
    private int brackgroundcolor = 0xff000000; // 背景颜色  
    private float lrcTextSize = 20; // 歌词大小  
    // private Align = Paint.Align.CENTER；  
    public float mTouchHistoryY;  
    private int height;  
    private long currentDunringTime; // 当前行歌词持续的时间，用该时间来sleep  
    // private float middleY;// y轴中间  
    private int TextHeight = 50; // 每一行的间隔  
    public int index = 0;  
    private List<Sentence> Sentencelist; // 歌词列表  
    public Paint getNotCurrrentPaint() {  
        return NotCurrrentPaint;  
    }  
    public void setNotCurrrentPaint(Paint notCurrrentPaint) {  
        NotCurrrentPaint = notCurrrentPaint;  
    }  
    public float getLrcTextSize() {  
        return lrcTextSize;  
    }  
    public void setLrcTextSize(float lrcTextSize) {  
        this.lrcTextSize = lrcTextSize;  
    }  
    public static Lyric getmLyric() {  
        return mLyric;  
    }  
    public void setmLyric(Lyric mLyric) {  
        LyricView.mLyric = mLyric;  
    }  
    public Paint getCurrentPaint() {  
        return CurrentPaint;  
    }  
    public void setCurrentPaint(Paint currentPaint) {  
        CurrentPaint = currentPaint;  
    }  
    public List<Sentence> getSentencelist() {  
        return Sentencelist;  
    }  
    public void setSentencelist(List<Sentence> sentencelist) {  
        Sentencelist = sentencelist;  
    }  
    public int getNotCurrrentPaintColor() {  
        return notCurrrentPaintColor;  
    }  
    public void setNotCurrrentPaintColor(int notCurrrentPaintColor) {  
        this.notCurrrentPaintColor = notCurrrentPaintColor;  
    }  
    public int getCurrrentPaintColor() {  
        return CurrrentPaintColor;  
    }  
    public void setCurrrentPaintColor(int currrentPaintColor) {  
        CurrrentPaintColor = currrentPaintColor;  
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
    public LyricView(Context context) {  
        super(context);  
        init();  
    }  
    public LyricView(Context context, AttributeSet attr) {  
        super(context, attr);  
        init();  
    }  
    public LyricView(Context context, AttributeSet attr, int i) {  
        super(context, attr, i);  
        init();  
    }  
    private void init() {  
        setFocusable(true);  
        // PlayListItem pli = new PlayListItem("Because Of You",  
        // "/sdcard/MP3/Because Of You.mp3", 0L, true);  
        // mLyric = new Lyric(new File("/sdcard/MP3/Because Of You.lrc"), pli);  
        // 非高亮部分  
        NotCurrrentPaint = new Paint();  
        NotCurrrentPaint.setAntiAlias(true);  
        NotCurrrentPaint.setTextSize(lrcTextSize);  
        // NotCurrrentPaint.setColor(notCurrrentPaintColor);  
        NotCurrrentPaint.setTypeface(Texttypeface);  
        NotCurrrentPaint.setTextAlign(Paint.Align.CENTER);  
        // 高亮部分 当前歌词  
        CurrentPaint = new Paint();  
        CurrentPaint.setAntiAlias(true);  
        // CurrentPaint.setColor(CurrrentPaintColor);  
        CurrentPaint.setTextSize(lrcTextSize);  
        CurrentPaint.setTypeface(Texttypeface);  
        CurrentPaint.setTextAlign(Paint.Align.CENTER);  
        // list = mLyric.list;  
    }  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        canvas.drawColor(brackgroundcolor);  
        NotCurrrentPaint.setColor(notCurrrentPaintColor);  
        CurrentPaint.setColor(CurrrentPaintColor);  
        if (index == -1)  
            return;  
        // 先画当前行，之后再画他的前面和后面，这样就保持当前行在中间的位置  
        try {  
            canvas.drawText(Sentencelist.get(index).getContent(), width / 2,  
                    height / 2, CurrentPaint);  
            float tempY = height / 2;  
            // 画出本句之前的句子  
            for (int i = index - 1; i >= 0; i--) {  
                // Sentence sen = list.get(i);  
                // 向上推移  
                tempY = tempY - TextHeight;  
                if (tempY < 0) {  
                    break;  
                }  
                canvas.drawText(Sentencelist.get(i).getContent(), width / 2,  
                        tempY, NotCurrrentPaint);  
                // canvas.translate(0, DY);  
            }  
            tempY = height / 2;  
            // 画出本句之后的句子  
            for (int i = index + 1; i < Sentencelist.size(); i++) {  
                // 往下推移  
                tempY = tempY + TextHeight;  
                if (tempY > height) {  
                    break;  
                }  
                canvas.drawText(Sentencelist.get(i).getContent(), width / 2,  
                        tempY, NotCurrrentPaint);  
                // canvas.translate(0, DY);  
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
     * @return currentDunringTime 歌词只需的时间 
     */  
    public long updateIndex(long time) {  
        // 歌词序号  
        index = mLyric.getNowSentenceIndex(time);  
        if (index == -1)  
            return -1;  
        Sentence sen = Sentencelist.get(index);  
        // 返回歌词持续的时间，在这段时间内sleep  
        return currentDunringTime = sen.getDuring();  
    }  
} 
  
}  