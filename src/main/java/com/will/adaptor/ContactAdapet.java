package com.will.adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.will.family.R;
import com.will.ui.ChatActivity;

import java.util.List;


import cn.bmob.im.bean.BmobChatUser;

/**
 * Created by will on 2016/3/22.
 */
public class ContactAdapet extends BaseAdapter {

    private List<BmobChatUser> users;
    Context context;
    private LayoutInflater inflater;

    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    public ContactAdapet(Context cont,List<BmobChatUser> list) {
        super();
        context=cont;
        users=list;
        inflater=LayoutInflater.from(cont);
        InitDisplayImageOptions();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        final BmobChatUser friend;

        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.main_contact_item,null);
            viewHolder.avatar=(ImageView)convertView.findViewById(R.id.contaction_avatar);
            viewHolder.Nick=(TextView)convertView.findViewById(R.id.contaction_nick);
            convertView.setTag(viewHolder);
        }else
            viewHolder=(ViewHolder)convertView.getTag();

        friend=users.get(position);

        if(friend.getNick()==null){
            viewHolder.Nick.setText(friend.getUsername());
        }else{
            viewHolder.Nick.setText(friend.getNick());
        }

        /*********异步加载图像*************/
        String avator=friend.getAvatar();
        imageLoader.displayImage(avator,viewHolder.avatar,imageOptions,new simpleImageListener());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", friend);
                Intent toChat = new Intent(context, ChatActivity.class);
                toChat.putExtras(bundle);
                context.startActivity(toChat);
            }
        });
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    private class ViewHolder{
        protected ImageView avatar;

        protected TextView Nick;
    }


    void InitDisplayImageOptions(){
        imageLoader=ImageLoader.getInstance();
        imageOptions=new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.test_friend_avat)
                .showImageOnLoading(R.drawable.test_master_avat)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.NONE)
                .build();
    }


    private class simpleImageListener extends SimpleImageLoadingListener{
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            super.onLoadingComplete(imageUri, view, loadedImage);

        }
    }

}
