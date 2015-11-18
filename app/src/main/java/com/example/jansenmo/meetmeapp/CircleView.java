package com.example.jansenmo.meetmeapp;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TableLayout;

public class CircleView extends View {

    private float rankRed=500;
    private float rankBlue=1000;


    private Paint circle1, circle2;
    private float width, height;

    public CircleView(Context context) {
        super(context);
        // create the Paint and set its color
        construct();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

       construct();
        // this constructor used when creating view through XML
    }

    public void construct(){

        Display disp = ((WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        width = disp.getWidth();
        height = disp.getHeight();


        //TODO get TeamRanks from server and save in rankRed and rankBlue

        circle1 = new Paint();
        circle2 = new Paint();
        circle1.setColor(Color.BLUE);
        circle2.setColor(Color.RED);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        float sizeBlue=0;
        float sizeRed=0;

        if(rankBlue>rankRed){
            sizeBlue = width/2 - 100;
            sizeRed = Math.round(sizeBlue * (rankRed/rankBlue));
        }
        else{
            sizeRed = width/2 - 100;
            sizeBlue = Math.round(sizeRed * (rankBlue/rankRed));
        }

        if(sizeBlue>sizeRed) {
            canvas.drawCircle(width/2, height/4, sizeBlue, circle1);
            canvas.drawCircle(width/3, height/2, sizeRed, circle2);
        }
        else {
            canvas.drawCircle(width/2, height/4, sizeRed, circle2);
            canvas.drawCircle(width/3, height/2, sizeBlue, circle1);
        }
    }

}