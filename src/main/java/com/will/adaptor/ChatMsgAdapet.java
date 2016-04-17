package com.will.adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.will.bean.RecordPlayListener;
import com.will.family.R;
import com.will.ui.PersonalInfor;
import com.will.bean.ImageLoadOptions;
import com.will.utils.TextUtil;
import com.will.utils.TimeUtil;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.inteface.DownloadListener;

/**
 * Created by will on 2016/2/12.
 */
public class ChatMsgAdapet extends AdapetBase<BmobMsg> {

    //8种item类型
    //文本
    private final int TYPE_TXT_RECEIVER=0;
    private final int TYPE_TXT_SEND=1;
    //图片
    private final int TYPE_IMAGE_RECEIVER=2;
    private final int TYPE_IMAGE_SEND=3;
    //位置
    private final int TYPE_LOCATION_RECEIVER=4;
    private final int TYPE_LOCATION_SEND=5;
    //语音
    private final int TYPE_VOICE_RECEIVER=6;
    private final int TYPE_VOICE_SEND=7;

    private String CurrentUserID="";

    DisplayImageOptions options;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    LayoutInflater inflater;
    public ChatMsgAdapet(Context context,List<BmobMsg> msgList) {
        super(context,msgList);
        CurrentUserID= BmobUserManager.getInstance(context).getCurrentUserObjectId();
        //初始化Options
        options=new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        inflater=LayoutInflater.from(context);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        final BmobMsg msg=list.get(position);
        if(convertView==null){
            convertView=creatViewByType(msg,position);
        }
        //文本类型
        ImageView iv_avatar = MsgListHolder.get(convertView, R.id.iv_avatar);
        final ImageView iv_fail_resend = MsgListHolder.get(convertView, R.id.iv_fail_resend);//失败重发
        final TextView tv_send_status = MsgListHolder.get(convertView, R.id.tv_send_status);//发送状态
        TextView tv_time = MsgListHolder.get(convertView, R.id.tv_time);
        TextView tv_message = MsgListHolder.get(convertView, R.id.tv_message);
        //图片
        ImageView iv_picture = MsgListHolder.get(convertView, R.id.iv_picture);
        final ProgressBar progress_load = MsgListHolder.get(convertView, R.id.progress_load);//进度条
        //位置
        TextView tv_location = MsgListHolder.get(convertView, R.id.tv_location);
        //语音
        final ImageView iv_voice = MsgListHolder.get(convertView, R.id.iv_voice);
        //语音长度
        final TextView tv_voice_length = MsgListHolder.get(convertView, R.id.tv_voice_length);

        tv_time.setText(TimeUtil.getChatTime(Long.parseLong(msg.getMsgTime())));

        String avatar =msg.getBelongAvatar();
        if(avatar!=null&&!avatar.equals("")){
            ImageLoader.getInstance().displayImage(avatar,iv_avatar, ImageLoadOptions.getOptions(),animateFirstListener);
        }else
            iv_avatar.setImageResource(R.drawable.default_avatar);

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext, PersonalInfor.class);
                intent.putExtra("username",msg.getBelongUsername());
                mcontext.startActivity(intent);
            }
        });
        if(isBelongToSelf(msg)){
            if(msg.getStatus()==BmobConfig.STATUS_SEND_SUCCESS){//发送成功
                progress_load.setVisibility(View.GONE);//待修改
                iv_fail_resend.setVisibility(View.GONE);//待修改
                if(msg.getMsgType()==BmobConfig.TYPE_VOICE){
                    tv_voice_length.setVisibility(View.VISIBLE);
                    tv_send_status.setVisibility(View.GONE);
                }else{
                    tv_send_status.setVisibility(View.VISIBLE);
                    tv_send_status.setText("已发送");
                }
            }else if(msg.getStatus()==BmobConfig.STATUS_SEND_FAIL) {//发送失败



            }else if(msg.getStatus()==BmobConfig.STATUS_SEND_RECEIVERED){//对方已经接收

            }
        }

        /*处理内容*/
        final String text =msg.getContent();
        switch(msg.getMsgType()){
            case BmobConfig.TYPE_TEXT:
                try {
                    SpannableString spannableString= TextUtil.toSpannableString(mcontext,text);
                    tv_message.setText(spannableString);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case BmobConfig.TYPE_IMAGE:

                break;
            case BmobConfig.TYPE_VOICE:
                if(text!=null&&!text.equals("")){
                    tv_voice_length.setVisibility(View.VISIBLE);
                    if(isBelongToSelf(msg)){
                        if((msg.getStatus()==BmobConfig.STATUS_SEND_RECEIVERED) ||
                                (msg.getStatus()==BmobConfig.STATUS_SEND_SUCCESS)){
                            tv_voice_length.setVisibility(View.VISIBLE);
                            String lenght=(text.split("&"))[2];
                            tv_voice_length.setText(lenght+"\''");
                        }else tv_voice_length.setVisibility(View.INVISIBLE);
                    }else {
                        final String lenght=(text.split("&"))[1];
                        tv_voice_length.setText(lenght+"\''");
                    }
                }
                iv_voice.setOnClickListener(new RecordPlayListener(mcontext, msg, new DownloadListener() {
                    @Override
                    public void onStart() {
                        progress_load.setVisibility(View.VISIBLE);
                        tv_voice_length.setVisibility(View.GONE);
                        iv_voice.setVisibility(View.INVISIBLE);//只有下载完成才显示播放的按钮
                    }

                    @Override
                    public void onSuccess() {
                        progress_load.setVisibility(View.GONE);
                        tv_voice_length.setVisibility(View.VISIBLE);
                        iv_voice.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(String s) {
                        progress_load.setVisibility(View.GONE);
                        tv_voice_length.setVisibility(View.GONE);
                        iv_voice.setVisibility(View.INVISIBLE);
                    }
                }));
                break;
            case BmobConfig.TYPE_LOCATION:
                break;
        }




        return convertView;
    }

    /*
    * 判断消息的所属*/
    public  boolean isBelongToSelf(BmobMsg temp){
        return CurrentUserID.equals(temp.getBelongId());
    }

    public View creatViewByType(BmobMsg msg,int position){
        int type=msg.getMsgType();
        switch (type){
            case BmobConfig.TYPE_IMAGE:
                return getItemViewType(position)==TYPE_IMAGE_SEND?
                        inflater.inflate(R.layout.chat_item_image_send,null):
                        inflater.inflate(R.layout.chat_item_image_receiver,null);
            case BmobConfig.TYPE_LOCATION:
                return getItemViewType(position)==TYPE_LOCATION_SEND?
                        inflater.inflate(R.layout.chat_item_location_send,null):
                        inflater.inflate(R.layout.chat_item_location_receiver,null);
            case BmobConfig.TYPE_VOICE:
                return getItemViewType(position)==TYPE_VOICE_SEND?
                        inflater.inflate(R.layout.chat_item_voice_send,null):
                        inflater.inflate(R.layout.chat_item_voice_receiver,null);
            default:
                return getItemViewType(position)==TYPE_TXT_SEND?
                        inflater.inflate(R.layout.chat_item_text_send,null):
                        inflater.inflate(R.layout.chat_item_text_receiver,null);
        }
    }
    /***********************************
    * 返回Item类型:  用于多种布局的ITEM返回正确类型的convertview*/
    public int getItemViewType(int position){
        BmobMsg msg=list.get(position);
        if(msg.getMsgType()==BmobConfig.TYPE_IMAGE){
            return isBelongToSelf(msg)?TYPE_IMAGE_SEND:TYPE_IMAGE_RECEIVER;

        }else if(msg.getMsgType()==BmobConfig.TYPE_LOCATION){
            return isBelongToSelf(msg)?TYPE_LOCATION_SEND:TYPE_LOCATION_RECEIVER;

        }else if(msg.getMsgType()==BmobConfig.TYPE_VOICE){
            return isBelongToSelf(msg)?TYPE_VOICE_SEND:TYPE_VOICE_RECEIVER;

        }else{
            return isBelongToSelf(msg)?TYPE_TXT_SEND:TYPE_TXT_RECEIVER;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 8;
    }

    /************************************/
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
