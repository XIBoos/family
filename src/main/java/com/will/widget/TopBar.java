package com.will.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.will.family.R;
import com.will.utils.DensityUtil;

/**
 * Created by will on 2016/2/1.
 */
public class TopBar extends LinearLayout {

    private Button LeftButton,RightButton;
    private TextView Title;
    private LinearLayout linearLayout;

    Context context;
    private TopbarClickListener listener;

    public interface TopbarClickListener{
        void LeftOnClick();
        void RightOnClick();
    }

    public void setTopBarClickListener(TopbarClickListener listener){
        this.listener=listener;
    }


    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        linearLayout= (LinearLayout)LayoutInflater.from(context)
                .inflate(R.layout.universal_topbar_layout, null);
        LeftButton=(Button)linearLayout.findViewById(R.id.topbar_leftbutton);
        RightButton=(Button)linearLayout.findViewById(R.id.topbar_rightbutton);
        Title=(TextView)linearLayout.findViewById(R.id.topbar_titleview);

        LeftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.LeftOnClick();
            }
        });

        RightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.RightOnClick();
            }
        });
        ViewGroup.LayoutParams pa=new ViewGroup
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(linearLayout,pa);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setTopbarWithoutRight(Boolean flag){
        if(flag){
            RightButton.setVisibility(INVISIBLE);
        }
    }

    public Button getLeftButton(){return LeftButton;}


    public void setLeftText(String leftText) {
        LeftButton.setText(leftText);
    }

    public void setRightText(String rightText) {
        RightButton.setText(rightText);
    }

    public void setTitleText(String titleText) {
        Title.setText(titleText);
    }

    public void setLeftOnClickListener(OnClickListener l){
        LeftButton.setOnClickListener(l);
    }

    public void setRightOnClickLisener(OnClickListener l){
        RightButton.setOnClickListener(l);
    }
}
