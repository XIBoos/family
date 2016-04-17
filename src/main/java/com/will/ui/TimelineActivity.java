package com.will.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ActionMenuView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.will.adaptor.TimeLineAdapet;
import com.will.bean.TimeLineItem;
import com.will.family.MyApplication;
import com.will.family.R;
import com.will.utils.DensityUtil;

import java.nio.InvalidMarkException;
import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {

    List<TimeLineItem> list;

    //旋转动画
    Animation animation_start_1;
    Animation animation_start_2;
    Animation animation_end_1;
    Animation animation_end_2;

    //按钮状态
    Boolean BUTTON_STATE=false;

    private ImageButton add,both,image,text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        InitAnimation();
        ListView listView=(ListView)findViewById(R.id.timeline_list);
        both=(ImageButton)findViewById(R.id.timeline_button_both);
        both.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("will","both");
            }
        });
        image=(ImageButton)findViewById(R.id.timeline_button_image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("will","image");
            }
        });
        text=(ImageButton)findViewById(R.id.timeline_button_text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("will","text");
            }
        });
        add=(ImageButton)findViewById(R.id.timeline_button_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BUTTON_STATE){
                    both.setVisibility(View.VISIBLE);
                    image.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                    text.startAnimation(animation_start_1);
                    image.startAnimation(animation_start_2);
                    BUTTON_STATE=true;
                }else {
                    text.startAnimation(animation_end_1);
                    image.startAnimation(animation_end_2);
                    BUTTON_STATE=false;
                }

            }
        });

        list=new ArrayList<TimeLineItem>();
        for(int i=0;i<4;i++) {
            TimeLineItem item = new TimeLineItem("2016.10.1","12:00","标题"+i,"内容"+i);
            list.add(item);
        }
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.chat_emo_normal);
        List<Bitmap> lll=new ArrayList<Bitmap>();
        lll.add(bitmap);
        for(int i=0;i<4;i++) {
            TimeLineItem item = new TimeLineItem("2016.10.1","12:00",lll);
            list.add(item);
        }
        List<Bitmap> llls=new ArrayList<Bitmap>();
        llls.add(bitmap);
        Bitmap bitmaps= BitmapFactory.decodeResource(getResources(), R.drawable.chat_emo_normal);
        llls.add(bitmaps);
        for(int i=0;i<12;i++) {
            TimeLineItem item = new TimeLineItem("2016.10.1","12:00",llls);
            list.add(item);
        }
        int k=llls.size();
        Log.d("will", k + "ii");
        TimeLineAdapet adapet=new TimeLineAdapet(this,list);
        listView.setAdapter(adapet);



    }

    public void InitAnimation(){
        animation_start_1=new RotateAnimation(0.0f,90.0f,
                Animation.RELATIVE_TO_SELF,1.90f,
                Animation.RELATIVE_TO_SELF,0.50f);
        animation_start_1.setDuration(300);
        animation_start_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                RelativeLayout.LayoutParams params1=(RelativeLayout.LayoutParams)text.getLayoutParams();
                params1.setMargins(0, 0,
                        DensityUtil.dpTOpx(TimelineActivity.this, 10),
                        DensityUtil.dpTOpx(TimelineActivity.this,80));
                text.setLayoutParams(params1);
                text.clearAnimation();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        animation_start_2=new RotateAnimation(0.0f,45.0f,
                Animation.RELATIVE_TO_SELF,1.90f,
                Animation.RELATIVE_TO_SELF,0.50f);
        animation_start_2.setDuration(300);
        animation_start_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RelativeLayout.LayoutParams params2=(RelativeLayout.LayoutParams)image.getLayoutParams();
                params2.setMargins(0,0,
                        DensityUtil.dpTOpx(TimelineActivity.this,59),
                        DensityUtil.dpTOpx(TimelineActivity.this,59));
                image.setLayoutParams(params2);
                image.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        animation_end_1=new RotateAnimation(0.0f,-90.0f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,1.9f);
        animation_end_1.setDuration(300);
        animation_end_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RelativeLayout.LayoutParams params3=(RelativeLayout.LayoutParams)text.getLayoutParams();
                params3.setMargins(0, 0,
                        DensityUtil.dpTOpx(TimelineActivity.this,80),
                        DensityUtil.dpTOpx(TimelineActivity.this,10));
                text.setLayoutParams(params3);
                text.clearAnimation();
                text.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation_end_2=new RotateAnimation(0.0f,-45.0f,
                Animation.RELATIVE_TO_SELF,1.48f,
                Animation.RELATIVE_TO_SELF,1.48f);
        animation_end_2.setDuration(300);
        animation_end_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RelativeLayout.LayoutParams params4=(RelativeLayout.LayoutParams)image.getLayoutParams();
                params4.setMargins(0, 0,
                        DensityUtil.dpTOpx(TimelineActivity.this, 80),
                        DensityUtil.dpTOpx(TimelineActivity.this,10));
                image.setLayoutParams(params4);
                image.clearAnimation();
                image.setVisibility(View.GONE);
                both.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }




}
