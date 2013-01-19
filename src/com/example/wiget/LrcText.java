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
      
    private Paint NotCurrentPaint; // �ǵ�ǰ��ʻ���  
    private Paint CurrentPaint; // ��ǰ��ʻ���  
    private int notCurrentPaintColor = Color.WHITE;// �ǵ�ǰ��ʻ��� ��ɫ  
    private int CurrentPaintColor = Color.RED; // ��ǰ��ʻ��� ��ɫ  
    private Typeface Texttypeface = Typeface.SERIF;  
    private Typeface CurrentTexttypeface = Typeface.DEFAULT_BOLD;  
    private float width;  
      
    private int brackgroundcolor = Color.BLACK; // ������ɫ  
    private float lrcTextSize = 22; // ��ʴ�С  
    private float CurrentTextSize = 24;  
    // private Align = Paint.Align.CENTER��  
    public float mTouchHistoryY;  
    private int height;  
    private long currentDunringTime; // ��ǰ�и�ʳ�����ʱ�䣬�ø�ʱ����sleep  
      
    private int TextHeight = 50; // ÿһ�еļ��  
    private boolean lrcInitDone = false;// �Ƿ��ʼ�������  
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
      
        // �Ǹ�������  
        NotCurrentPaint = new Paint();  
        NotCurrentPaint.setAntiAlias(true);  
        NotCurrentPaint.setTextAlign(Paint.Align.CENTER);  
        // �������� ��ǰ���  
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
        // ���Ϲ��� ����Ǹ��ݸ�ʵ�ʱ�䳤������������������  
        canvas.translate(0, -plus);  
        // �Ȼ���ǰ�У�֮���ٻ�����ǰ��ͺ��棬�����ͱ��ֵ�ǰ�����м��λ��  
        try {  
            canvas.drawText(lrclist.get(index).getLrcString(), width / 2,  
                    height / 2, CurrentPaint);  
            // canvas.translate(0, plus);  
            float tempY = height / 2;  
            // ��������֮ǰ�ľ���  
            for (int i = index - 1; i >= 0; i--) {  
                // Sentence sen = list.get(i);  
                // ��������  
                tempY = tempY - TextHeight;  
                if (tempY < 0) {  
                    break;  
                }  
                canvas.drawText(lrclist.get(i).getLrcString(), width / 2,  
                        tempY, NotCurrentPaint);  
                // canvas.translate(0, TextHeight);  
            }  
            tempY = height / 2;  
            // ��������֮��ľ���  
            for (int i = index + 1; i < lrclist.size(); i++) {  
                // ��������  
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
     *            ��ǰ��ʵ�ʱ���� 
     *  
     * @return null 
     */  
    public void SetNowPlayIndex(int i,int time) {  
        this.currentTime = time;  
//      // ������  
        index = i;  
        this.invalidate();  
        if (index != -1) {  
            sentenctTime = lrclist.get(index).getTimePoint();  
            currentDunringTime = lrclist.get(index).getSleepTime();  
//          Log.d(TAG,"sentenctTime = "+sentenctTime+",  currentDunringTime = "+currentDunringTime);  
        }  
    }  
}  