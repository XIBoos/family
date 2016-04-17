package com.will.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.will.family.R;
import com.will.utils.DensityUtil;


public class main_ScrollView extends HorizontalScrollView {


    private LinearLayout linearLayout;

    private ViewGroup settingView;
    private ViewGroup mainView;

    private int contactWidth;
    private int contactPadding;

    static public int settingWidth;

    private int ScreenWidth;

    public main_ScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager manager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        ScreenWidth=displayMetrics.widthPixels;
        contactPadding=DensityUtil.dpTOpx(context,100);
        contactWidth=ScreenWidth-contactPadding;
        settingWidth=DensityUtil.dpTOpx(context,40);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("family", "onmeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        linearLayout=(LinearLayout)getChildAt(0);
        settingView=(ViewGroup)linearLayout.getChildAt(0);
        settingView.getLayoutParams().width=settingWidth;
        mainView=(ViewGroup)linearLayout.getChildAt(1);
        mainView.getLayoutParams().width=ScreenWidth;
    }

    private void InitSettingButton(){
        Button button1=(Button)settingView.findViewById(R.id.btn_setting);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Button button2=(Button)settingView.findViewById(R.id.btn_unloging);
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d("family", "onlayout");
        scrollTo(settingWidth,0);
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Log.d("family", "draw");
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //把控件的滑动动作拦截
        return false;

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.d("family", "onScrollchanged");
    }

}
