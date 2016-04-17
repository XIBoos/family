package com.will.adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.will.bean.TimeLineItem;
import com.will.family.R;


import java.util.List;

/**
 * Created by will on 2016/3/7.
 */
public class TimeLineAdapet extends BaseAdapter {

    private List<TimeLineItem> items;
    Context context;
    LayoutInflater inflater;

    public TimeLineAdapet(Context cont,List<TimeLineItem> list) {
        super();
        items=list;
        context=cont;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }


    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        TimeLineItem item=items.get(position);
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=getViewByType(item);
            viewHolder.Title=(TextView)convertView.findViewById(R.id.timeline_title);
            viewHolder.Text=(TextView)convertView.findViewById(R.id.timeline_content);
            viewHolder.image=(ImageView)convertView.findViewById(R.id.timeline_image);
            viewHolder.data=(TextView)convertView.findViewById(R.id.timeline_data);
            viewHolder.hour=(TextView)convertView.findViewById(R.id.timeline_hour);
            viewHolder.image1=(ImageView)convertView.findViewById(R.id.timeline_image1);
            convertView.setTag(viewHolder);
        }else
            viewHolder=(ViewHolder)convertView.getTag();


        viewHolder.data.setText(item.getData());
        viewHolder.hour.setText(item.getHour());


        List<Bitmap> bitlist;
        switch (item.getTYPE()){
            case TimeLineItem.TYPE_TEXT:
                viewHolder.Title.setText(item.getTitle());
                viewHolder.Text.setText(item.getText());
                break;
            case TimeLineItem.TYPE_IMAGE:
                bitlist=item.getImages();
                if (bitlist.size()<=1) {
                    viewHolder.image.setImageBitmap(bitlist.get(0));
                    viewHolder.image1.setVisibility(View.GONE);
                }else {
                    viewHolder.image.setImageBitmap(bitlist.get(0));
                    viewHolder.image1.setImageBitmap(bitlist.get(1));
                    viewHolder.image1.setVisibility(View.VISIBLE);
                }
                break;
            case TimeLineItem.TYPE_BOTH:
                viewHolder.Title.setText(item.getTitle());
                viewHolder.Text.setText(item.getText());
                bitlist=item.getImages();
                if (bitlist.size()==0){
                    viewHolder.image.setVisibility(View.GONE);
                }else if (bitlist.size()==1) {
                    viewHolder.image.setImageBitmap(bitlist.get(0));;
                }
                break;
        }

        return convertView;
    }

    public View getViewByType(TimeLineItem i){
        if (i.getTYPE()==TimeLineItem.TYPE_BOTH){
            return inflater.inflate(R.layout.timeline_item_both,null);
        }else if (i.getTYPE()==TimeLineItem.TYPE_IMAGE){
            return inflater.inflate(R.layout.timeline_item_image,null);
        }else
            return inflater.inflate(R.layout.timeline_item_text,null);
    }



    @Override
    public int getItemViewType(int position) {
        return items.get(position).getTYPE();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    private class ViewHolder{
        protected TextView data;
        protected TextView hour;
        protected TextView Title;
        protected TextView Text;
        protected ImageView image;
        protected ImageView image1;
        protected ViewHolder(){
        }
    }
}
