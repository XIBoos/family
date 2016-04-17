package com.will.bean;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.List;

/**
 * Created by will on 2016/3/7.
 */
public class TimeLineItem {

    final public static int TYPE_BOTH=0;
    final public static int TYPE_IMAGE=1;
    final public static int TYPE_TEXT=2;

    private String title;
    private String text;
    private List<Bitmap> images;
    private String data;
    private String hour;
    private int TYPE;


    public String getTitle(){
        return title;
    }

    public String getText(){
        return text;
    }

    public int getTYPE(){
        return TYPE;
    }

    public String getData(){
        return data;
    }

    public String getHour(){
        return hour;
    }

    public List<Bitmap> getImages(){
        return images;
    }

    public TimeLineItem(String d,String h,String t,String c){
        TYPE=TYPE_TEXT;
        title=t;
        text=c;
        data=d;
        hour=h;
    }

    public TimeLineItem(String d,String h,List<Bitmap> b){
        TYPE=TYPE_IMAGE;
        images=b;
        data=d;
        hour=h;
    }

    public TimeLineItem(String d,String h,List<Bitmap> b,String t,String c){
        TYPE=TYPE_BOTH;
        title=t;
        text=c;
        images=b;
        data=d;
        hour=h;
    }
}
