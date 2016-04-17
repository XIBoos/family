package com.will.adaptor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.will.bean.ExpressionItem;
import com.will.family.R;

import java.util.List;


/**
 * Created by will on 2016/2/22.
 */
public class ExpressionGridAdapte extends BaseAdapter{

    Context mcontext;

    private List<ExpressionItem> Items;

    public ExpressionGridAdapte(Context context,List list) {
        mcontext=context;
        Items=list;
    }

    @Override
    public int getCount() {
       return Items.size();
    }

    @Override
    public Object getItem(int position) {
        return Items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView itemview=null;
        EmotionHolder viewHolder=null;
        if(convertView==null){
            convertView=View.inflate(mcontext, R.layout.chat_layout_emo_item,null);
            viewHolder=new EmotionHolder();
            itemview=(ImageView)viewHolder.findView(convertView,R.id.emo_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(EmotionHolder)convertView.getTag();
            itemview=(ImageView)viewHolder.findView(convertView,R.id.emo_item);
        }
        itemview.setImageResource(Items.get(position).getID());
        viewHolder.setExpressionItem(Items.get(position));
        return convertView;
    }


}
