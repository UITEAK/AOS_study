package com.example.radialchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.View;
/**
 * Created by Administrator on 2017-04-15.
 */

public class RadialView extends View{
    private Context context;

    private int m_nHeight;
    private int m_nWidth;
    private float[] SCORES;
    private String[] NAMES;

    public RadialView(Context context){
        super(context);
        this.context = context;

        NAMES = new String[6];
        NAMES[0] = "R";
        NAMES[1] = "I";
        NAMES[2] = "A";
        NAMES[3] = "S";
        NAMES[4] = "E";
        NAMES[5] = "C";
    }

    private void InitializeRadial(Canvas canvas, float _relative, float trigowidth, float trigoheight){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4.0F);
        paint.setColor(Color.argb(255, 112, 146, 190));

        canvas.drawLine(this.m_nWidth / 2 - _relative, this.m_nHeight / 2, this.m_nWidth / 2 + _relative,  this.m_nHeight / 2, paint);
        canvas.drawLine(this.m_nWidth / 2 , this.m_nHeight / 2, this.m_nWidth / 2 + trigowidth,  this.m_nHeight / 2 - trigoheight, paint);
        canvas.drawLine(this.m_nWidth / 2 , this.m_nHeight / 2, this.m_nWidth / 2 - trigowidth,  this.m_nHeight / 2 - trigoheight, paint);
        canvas.drawLine(this.m_nWidth / 2 , this.m_nHeight / 2, this.m_nWidth / 2 + trigowidth,  this.m_nHeight / 2 + trigoheight, paint);
        canvas.drawLine(this.m_nWidth / 2 , this.m_nHeight / 2, this.m_nWidth / 2 - trigowidth,  this.m_nHeight / 2 + trigoheight, paint);

        Path path = new Path();

        for(int i = 1; i <= 5; i++){
            if( i == 5){
                paint.setColor(Color.argb(255, 112, 146, 190));
                paint.setStrokeWidth(4);
            } else {
                paint.setColor(Color.argb(255, 112, 146, 190));
                paint.setStrokeWidth(2.0f);
            }

            float _rwidth = _relative * 0.2f * i;
            float stepheight = CalcTrigonoSin(_rwidth, 60 );
            float stepwidth = CalcTrigonoCos(_rwidth, 60 );

            path.reset();
            path.moveTo(this.m_nWidth / 2 - _rwidth, this.m_nHeight / 2);
            path.lineTo(this.m_nWidth / 2 - stepwidth,  this.m_nHeight / 2 - stepheight);
            path.lineTo(this.m_nWidth / 2 + stepwidth,  this.m_nHeight / 2 - stepheight);
            path.lineTo(this.m_nWidth / 2 + _rwidth, this.m_nHeight / 2);
            path.lineTo(this.m_nWidth / 2 + stepwidth,  this.m_nHeight / 2 + stepheight);
            path.lineTo(this.m_nWidth / 2 - stepwidth,  this.m_nHeight / 2 + stepheight);
            path.lineTo(this.m_nWidth / 2 - _rwidth, this.m_nHeight / 2);
            canvas.drawPath(path, paint);
        }

        path.close();
    }

    private void DrawCircleData(Canvas canvas, float _relative, float trigowidth, float trigoheight){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4.0F);
        paint.setColor(Color.argb(255, 112, 146, 190));

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(215, 52, 185, 202));
        canvas.drawCircle(this.m_nWidth / 2 - trigowidth,  this.m_nHeight / 2 - trigoheight, _relative * 0.21f, paint);
        canvas.drawCircle(this.m_nWidth / 2 + trigowidth,  this.m_nHeight / 2 - trigoheight, _relative * 0.21f, paint);
        canvas.drawCircle(this.m_nWidth / 2 - trigowidth,  this.m_nHeight / 2 + trigoheight, _relative * 0.21f, paint);
        canvas.drawCircle(this.m_nWidth / 2 + trigowidth,  this.m_nHeight / 2 + trigoheight, _relative * 0.21f, paint);

        canvas.drawCircle(this.m_nWidth / 2 - _relative,  this.m_nHeight / 2, _relative * 0.21f, paint);
        canvas.drawCircle(this.m_nWidth / 2 + _relative,  this.m_nHeight / 2, _relative * 0.21f, paint);

        paint.setColor(Color.argb(255, 255, 255, 255));
        //paint.setTextSize(Utils.DPFromPixel(this.context, 30));

        Rect bounds = new Rect();
        paint.getTextBounds(NAMES[0], 0, NAMES[0].length(), bounds);
        canvas.drawText(String.format("%s", this.NAMES[0]), this.m_nWidth / 2 - trigowidth - (bounds.width() / 2),  this.m_nHeight / 2 - trigoheight - (bounds.height() / 4.5f), paint);
        paint.getTextBounds(NAMES[1], 0, NAMES[1].length(), bounds);
        canvas.drawText(String.format("%s", this.NAMES[1]), this.m_nWidth / 2 + trigowidth - (bounds.width() / 2),  this.m_nHeight / 2 - trigoheight - (bounds.height() / 4.5f), paint);
        paint.getTextBounds(NAMES[2], 0, NAMES[2].length(), bounds);
        canvas.drawText(String.format("%s", this.NAMES[2]), this.m_nWidth / 2 + _relative - (bounds.width() / 2),  this.m_nHeight / 2 - (bounds.height() / 4.5f), paint);
        paint.getTextBounds(NAMES[3], 0, NAMES[3].length(), bounds);
        canvas.drawText(String.format("%s", this.NAMES[3]), this.m_nWidth / 2 + trigowidth - (bounds.width() / 2),  this.m_nHeight / 2 + trigoheight - (bounds.height() / 4.5f), paint);
        paint.getTextBounds(NAMES[4], 0, NAMES[4].length(), bounds);
        canvas.drawText(String.format("%s", this.NAMES[4]), this.m_nWidth / 2 - trigowidth - (bounds.width() / 2),  this.m_nHeight / 2 + trigoheight - (bounds.height() / 4.5f), paint);
        paint.getTextBounds(NAMES[5], 0, NAMES[5].length(), bounds);
        canvas.drawText(String.format("%s", this.NAMES[5]), this.m_nWidth / 2 - _relative - (bounds.width() / 2),  this.m_nHeight / 2 - (bounds.height() / 4.5f), paint);

    }

    private void DrawCirclePattern(Canvas canvas, float _relative, float trigowidth, float trigoheight){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(255, 255, 255, 255));
        //paint.setTextSize(Utils.DPFromPixel(this.context, 20));

        Rect bounds = new Rect();
        String score = String.format("%s", this.SCORES[0]);
        paint.getTextBounds(score, 0, score.length(), bounds);
        canvas.drawText(String.format("%s", score), this.m_nWidth / 2 - trigowidth - (bounds.width() / 2),  this.m_nHeight / 2 - trigoheight + (bounds.height() * 1.4f), paint);
        score = String.format("%s", this.SCORES[1]);
        paint.getTextBounds(score, 0, score.length(), bounds);
        canvas.drawText(String.format("%s", score), this.m_nWidth / 2 + trigowidth - (bounds.width() / 2),  this.m_nHeight / 2 - trigoheight + (bounds.height() * 1.4f), paint);
        score = String.format("%s", this.SCORES[2]);
        paint.getTextBounds(score, 0, score.length(), bounds);
        canvas.drawText(String.format("%s", score), this.m_nWidth / 2 + _relative - (bounds.width() / 2),  this.m_nHeight / 2 + (bounds.height() * 1.4f), paint);
        score = String.format("%s", this.SCORES[3]);
        paint.getTextBounds(score, 0, score.length(), bounds);
        canvas.drawText(String.format("%s", score), this.m_nWidth / 2 + trigowidth - (bounds.width() / 2),  this.m_nHeight / 2 + trigoheight + (bounds.height() * 1.4f), paint);
        score = String.format("%s", this.SCORES[4]);
        paint.getTextBounds(score, 0, score.length(), bounds);
        canvas.drawText(String.format("%s", score), this.m_nWidth / 2 - trigowidth - (bounds.width() / 2),  this.m_nHeight / 2 + trigoheight + (bounds.height() * 1.4f), paint);
        score = String.format("%s", this.SCORES[5]);
        paint.getTextBounds(score, 0, score.length(), bounds);
        canvas.drawText(String.format("%s", score), this.m_nWidth / 2 - _relative - (bounds.width() / 2),  this.m_nHeight / 2 + (bounds.height() * 1.4f), paint);

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(2.0F);
        paint.setColor(Color.argb(215, 255, 201, 14));

        Path path = new Path();
        path.reset();

        float _rwidth = _relative * this.SCORES[0] / 10;
        float stepheight = CalcTrigonoSin(_rwidth, 60 );
        float stepwidth = CalcTrigonoCos(_rwidth, 60 );
        path.moveTo(this.m_nWidth / 2 - stepwidth, this.m_nHeight / 2- stepheight);
        _rwidth = _relative * this.SCORES[1]/ 10;
        stepheight = CalcTrigonoSin(_rwidth, 60 );
        stepwidth = CalcTrigonoCos(_rwidth, 60 );
        path.lineTo(this.m_nWidth / 2 + stepwidth,  this.m_nHeight / 2 - stepheight);
        _rwidth = _relative * this.SCORES[2] / 10;
        stepheight = CalcTrigonoSin(_rwidth, 60 );
        stepwidth = CalcTrigonoCos(_rwidth, 60 );
        path.lineTo(this.m_nWidth / 2 + _rwidth,  this.m_nHeight / 2);
        _rwidth = _relative * this.SCORES[3]/ 10;
        stepheight = CalcTrigonoSin(_rwidth, 60 );
        stepwidth = CalcTrigonoCos(_rwidth, 60 );
        path.lineTo(this.m_nWidth / 2 + stepwidth,  this.m_nHeight / 2 + stepheight);
        _rwidth = _relative * this.SCORES[4]/ 10;
        stepheight = CalcTrigonoSin(_rwidth, 60 );
        stepwidth = CalcTrigonoCos(_rwidth, 60 );
        path.lineTo(this.m_nWidth / 2 - stepwidth,  this.m_nHeight / 2 + stepheight);
        _rwidth = _relative * this.SCORES[5] / 10;
        stepheight = CalcTrigonoSin(_rwidth, 60 );
        stepwidth = CalcTrigonoCos(_rwidth, 60 );
        path.lineTo(this.m_nWidth / 2 - _rwidth,  this.m_nHeight / 2);
        _rwidth = _relative * this.SCORES[0]/ 10;
        stepheight = CalcTrigonoSin(_rwidth, 60 );
        stepwidth = CalcTrigonoCos(_rwidth, 60 );
        path.lineTo(this.m_nWidth / 2 - stepwidth,  this.m_nHeight / 2 - stepheight);

        canvas.drawPath(path, paint);
        path.close();
    }

    private static float CalcTrigonoSin(float length, int degree){
        float result = 0;
        if(degree == 30){

        } else if(degree == 45){

        } else if(degree == 60){
            result = (float)((1.732 * length) / 2);
        }

        return result;
    }

    private static float CalcTrigonoCos(float length, int degree){
        float result = 0;
        if(degree == 30){

        } else if(degree == 45){

        } else if(degree == 60){
            result = (float)(length / 2);
        }

        return result;
    }

    public void SetTestScore(float[] scores){
        this.SCORES = scores;
    }
}

