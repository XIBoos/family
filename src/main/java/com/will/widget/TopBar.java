package com.will.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.will.family.R;

/**
 * Created by will on 2016/2/1.
 */
public class TopBar extends RelativeLayout {

    private Button LeftButton,RightButton;
    private TextView Title;

    private String LeftText,TitleText,RightText;
    private int LeftColor,TitleTextColor,RightColor;
    private Drawable LeftBackground,RightBackground;
    private float TitleTextSize;

    private LayoutParams LeftParams,TitleParams,RightParams;

    private TopbarClickListener listener;

    public interface TopbarClickListener{
        void LeftOnClick();
        void RightOnClick();
    };

    public void setTopBarClickListener(TopbarClickListener listener){
        this.listener=listener;
    }


    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        LeftText=ta.getString(R.styleable.TopBar_LeftText);
        LeftColor=ta.getColor(R.styleable.TopBar_LeftColor, 0);
        LeftBackground=ta.getDrawable(R.styleable.TopBar_LeftBackground);
        TitleText=ta.getString(R.styleable.TopBar_Title);
        TitleTextColor=ta.getColor(R.styleable.TopBar_TitleTextColor, 0);
        TitleTextSize=ta.getDimension(R.styleable.TopBar_TitleTextSize, 0);
        RightText=ta.getString(R.styleable.TopBar_RightText);
        RightColor=ta.getColor(R.styleable.TopBar_RightColor, 0);
        RightBackground=ta.getDrawable(R.styleable.TopBar_RightBackground);
        ta.recycle();

        LeftButton=new Button(context);
        RightButton=new Button(context);
        Title=new TextView(context);
        LeftButton.setText(LeftText);
        LeftButton.setTextColor(LeftColor);
        LeftButton.setBackgroundDrawable(LeftBackground);//等待更新

        RightButton.setText(RightText);
        RightButton.setTextColor(RightColor);
        RightButton.setBackgroundDrawable(RightBackground);//等待更新

        Title.setText(TitleText);
        Title.setTextColor(TitleTextColor);
        Title.setTextSize(TitleTextSize);

        LeftParams=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        LeftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        addView(LeftButton, LeftParams);

        RightParams=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        RightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
        addView(RightButton, RightParams);

        TitleParams=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        TitleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
        addView(Title, TitleParams);

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
        
    }

    public void setTopbarWithoutRight(Boolean flag){
        if(flag){
            RightButton.setVisibility(GONE);
        }
    }


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
