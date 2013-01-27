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
    private Paint NotCurrrentPaint; // �ǵ�ǰ��ʻ���  
    private Paint CurrentPaint; // ��ǰ��ʻ���  
    private int notCurrrentPaintColor = Color.WHITE;// �ǵ�ǰ��ʻ��� ��ɫ  
    private int CurrrentPaintColor = Color.RED; // ��ǰ��ʻ��� ��ɫ  
    private Typeface Texttypeface = Typeface.SERIF;  
    private float width;  
    private static Lyric mLyric;  
    private int brackgroundcolor = 0xff000000; // ������ɫ  
    private float lrcTextSize = 20; // ��ʴ�С  
    // private Align = Paint.Align.CENTER��  
    public float mTouchHistoryY;  
    private int height;  
    private long currentDunringTime; // ��ǰ�и�ʳ�����ʱ�䣬�ø�ʱ����sleep  
    // private float middleY;// y���м�  
    private int TextHeight = 50; // ÿһ�еļ��  
    public int index = 0;  
    private List<Sentence> Sentencelist; // ����б�  
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
        // �Ǹ�������  
        NotCurrrentPaint = new Paint();  
        NotCurrrentPaint.setAntiAlias(true);  
        NotCurrrentPaint.setTextSize(lrcTextSize);  
        // NotCurrrentPaint.setColor(notCurrrentPaintColor);  
        NotCurrrentPaint.setTypeface(Texttypeface);  
        NotCurrrentPaint.setTextAlign(Paint.Align.CENTER);  
        // �������� ��ǰ���  
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
        // �Ȼ���ǰ�У�֮���ٻ�����ǰ��ͺ��棬�����ͱ��ֵ�ǰ�����м��λ��  
        try {  
            canvas.drawText(Sentencelist.get(index).getContent(), width / 2,  
                    height / 2, CurrentPaint);  
            float tempY = height / 2;  
            // ��������֮ǰ�ľ���  
            for (int i = index - 1; i >= 0; i--) {  
                // Sentence sen = list.get(i);  
                // ��������  
                tempY = tempY - TextHeight;  
                if (tempY < 0) {  
                    break;  
                }  
                canvas.drawText(Sentencelist.get(i).getContent(), width / 2,  
                        tempY, NotCurrrentPaint);  
                // canvas.translate(0, DY);  
            }  
            tempY = height / 2;  
            // ��������֮��ľ���  
            for (int i = index + 1; i < Sentencelist.size(); i++) {  
                // ��������  
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
     *            ��ǰ��ʵ�ʱ���� 
     *  
     * @return currentDunringTime ���ֻ���ʱ�� 
     */  
    public long updateIndex(long time) {  
        // ������  
        index = mLyric.getNowSentenceIndex(time);  
        if (index == -1)  
            return -1;  
        Sentence sen = Sentencelist.get(index);  
        // ���ظ�ʳ�����ʱ�䣬�����ʱ����sleep  
        return currentDunringTime = sen.getDuring();  
    }  
} 
  
}  