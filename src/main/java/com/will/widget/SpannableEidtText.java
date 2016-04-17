package com.will.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by will on 2016/2/26.
 */
public class SpannableEidtText extends EditText {
    public SpannableEidtText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if(!TextUtils.isEmpty(text)) {
            super.setText(replace(text.toString()), type);
        }else
            super.setText(text,type);
    }


    private CharSequence replace(String text){
        try {
            int start=0;
            Pattern pattern=Pattern.compile("\\\\em[0-9]{2}", Pattern.CASE_INSENSITIVE);
            Matcher matcher=pattern.matcher(text);
            SpannableString spannableString=new SpannableString(text);
            while (matcher.find()){
                String emotext=matcher.group();
                String emotion=emotext.substring(1);
                BitmapFactory.Options options=new BitmapFactory.Options();
                Bitmap bitmap=BitmapFactory.decodeResource(getContext().getResources(),
                        getContext().getResources().getIdentifier(emotion, "drawable", getContext().getPackageName()),
                        options);
                ImageSpan span=new ImageSpan(getContext(),bitmap);
                int startIndex=text.indexOf(emotext,start);
                int endIndex=startIndex+emotext.length();
                if(startIndex>=0);
                    spannableString.setSpan(span,startIndex,endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                start=endIndex-1;
            }
            return spannableString;
        }catch (Exception e){
            return text;
        }

    }
}
