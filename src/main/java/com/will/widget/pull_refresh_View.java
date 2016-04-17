package com.will.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.text.format.Time;

import com.will.family.R;



/**
 * Created by will on 2016/2/4.
 */
public class pull_refresh_View extends LinearLayout {

    private TextView HintView,TimeView;
    private ImageView arrow;
    private ProgressBar progressBar;
    private LinearLayout mlinearlayout;
    private LayoutParams params;

    //动画
    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;
    private final int ANIMATION_DURATION=180;

    //状态
    private int STATE;
    public static final int STATE_INVISABLE=0;
    public static final int STATE_PULL=1;
    public static final int STATE_READY=2;
    public static final int STATE_REFRESHING=3;

    //拉动使能
    private boolean PULL_ENABLE=true;


    public pull_refresh_View(Context context) {
        super(context);
        init(context);
    }

    public pull_refresh_View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        mlinearlayout=(LinearLayout)LayoutInflater.from(context).inflate(R.layout.chat_pull_refresh, null);
        HintView=(TextView)mlinearlayout.findViewById(R.id.HintView);
        TimeView=(TextView)mlinearlayout.findViewById(R.id.TimeView);
        progressBar=(ProgressBar)mlinearlayout.findViewById(R.id.progressBar);
        arrow=(ImageView)mlinearlayout.findViewById(R.id.arrow);
        params=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        addView(mlinearlayout, params);

        //箭头方向改变的动画
        mRotateUpAnim=new RotateAnimation(0.0f,-180.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        mRotateUpAnim.setDuration(ANIMATION_DURATION);
        mRotateUpAnim.setFillAfter(true);

        mRotateDownAnim=new RotateAnimation(-180.0f,0.0F,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5F);
        mRotateDownAnim.setDuration(ANIMATION_DURATION);
        mRotateDownAnim.setFillAfter(true);

        //设置状态
        STATE=STATE_INVISABLE;

    }

    public void setVisableHeight(int height){
        if(PULL_ENABLE) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mlinearlayout.getLayoutParams();
            lp.height = height;
            mlinearlayout.setLayoutParams(lp);
        }
    }

    public int getVisableHeight(){
        return mlinearlayout.getHeight();
    }


    public void setRefreshTime(String time){
        TimeView.setText(time);
    }

    public void setState(int state){
        if(STATE==state) return;

        //Set Visibility
        if(state==STATE_REFRESHING){
            progressBar.setVisibility(VISIBLE);
            arrow.setVisibility(INVISIBLE);
            arrow.clearAnimation();
        }else {
            progressBar.setVisibility(INVISIBLE);
            arrow.setVisibility(VISIBLE);
        }

        switch (state){
            case STATE_PULL:
                if (STATE==STATE_READY) {
                    arrow.clearAnimation();
                    arrow.startAnimation(mRotateDownAnim);
                }
                if(STATE==STATE_REFRESHING){
                    arrow.clearAnimation();
                }
                HintView.setText("下拉刷新");
                PULL_ENABLE=true;
                break;
            case STATE_READY:
                if(STATE==STATE_PULL){
                    arrow.clearAnimation();
                    arrow.startAnimation(mRotateUpAnim);
                    HintView.setText("松手刷新");
                }
                PULL_ENABLE=true;
                break;
            case STATE_REFRESHING:
                if(STATE==STATE_READY) {
                    HintView.setText("正在加载");
                    Time time = new Time();
                    time.setToNow();
                    setRefreshTime(time.format("%Y-%m-%d %T"));
                }
                PULL_ENABLE=false;
                break;
        }
        STATE=state;
    }

    public int getState(){return STATE;}


    public boolean getEnable(){return PULL_ENABLE;}
}
