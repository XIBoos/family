package com.will.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by will on 2016/2/17.
 */
public class TextUtil {

    public static SpannableString toSpannableString(Context context,String text){
        if(!TextUtils.isEmpty(text)){
            SpannableString spannableString=new SpannableString(text);
            final int start=0;
            Pattern pattern=Pattern.compile("\\\\ue[a-z0-9]{3}",Pattern.CASE_INSENSITIVE);
            Matcher matcher=pattern.matcher(text);
            if(matcher.find()){
                String faceString=matcher.group();
                String key=faceString.substring(1);
                Bitmap face=BitmapFactory.decodeResource(context.getResources(), context.getResources()
                        .getIdentifier(key, "drawable", context.getPackageName()), new BitmapFactory.Options());
                int indexstart=text.indexOf(faceString, start);
                int indexend=indexstart+faceString.length();
                ImageSpan span= new ImageSpan(context,face);
                if(indexstart>=0){
                    spannableString.setSpan(span,indexstart,indexend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            return spannableString;
        }else return new SpannableString("");
    }

}
