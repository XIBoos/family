package com.will.adaptor;

import android.view.View;
import android.widget.ImageView;

import com.will.bean.ExpressionItem;

/**
 * Created by will on 2016/2/26.
 */
public class EmotionHolder {
    ImageView itemView;
    ExpressionItem expressionItem;
    public View findView(View view,int id){
        if(itemView==null) {
            itemView = (ImageView) view.findViewById(id);
            return itemView;
        }
        return itemView;
    }

    public ExpressionItem getExpressionItem(){
        return expressionItem;
    }

    public void setExpressionItem(ExpressionItem i){
        expressionItem=i;
    }
}
