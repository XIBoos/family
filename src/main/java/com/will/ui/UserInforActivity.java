package com.will.ui;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.will.User.User;
import com.will.bean.ImageLoadOptions;
import com.will.family.R;
import com.will.widget.TopBar;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.listener.PushListener;

public class UserInforActivity extends BassActivity {


    private ImageView avator;
    private TextView username;
    private TextView nick;
    private TextView remarkname;
    private TopBar topBar;
    private Button addFriend;

    private User object;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infor);
        Bundle bundle=getIntent().getExtras();
        object=(User)bundle.getSerializable("user");
        InitView();
    }

    private void InitTopBar(){
        topBar=(TopBar)findViewById(R.id.userinfo_topbar);
        topBar.setTitleText("个人信息");
        topBar.setLeftText("返回");
        topBar.setRightText("");
        topBar.setRightOnClickLisener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        topBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void InitView(){
        InitTopBar();
        avator=(ImageView)findViewById(R.id.userinfo_image_avat);
        username=(TextView)findViewById(R.id.userinfo_text_username);
        nick=(TextView)findViewById(R.id.userinfo_text_nick);
        remarkname=(TextView)findViewById(R.id.userinfo_text_remarkname);
        String avatar=object.getAvatar();
        if(avatar!=null&&!avatar.equals("")){
            ImageLoader.getInstance().displayImage(avatar,avator, ImageLoadOptions.getOptions(),animateFirstListener);
        }
        if(object.getUsername()!=null&&!TextUtils.isEmpty(object.getUsername()))
            username.setText(object.getUsername());
        if(object.getNick()!=null&&!TextUtils.isEmpty(object.getNick()))
            nick.setText(object.getNick());
        if(object.getRemarkName()!=null&&!TextUtils.isEmpty(object.getRemarkName()))
            remarkname.setText(object.getRemarkName());

        addFriend=(Button)findViewById(R.id.userinfo_butt_addfriend);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progress = new ProgressDialog(UserInforActivity.this);
                progress.setMessage("正在添加...");
                progress.setCanceledOnTouchOutside(false);
                progress.show();
                chatManager.sendTagMessage(BmobConfig.TAG_ADD_CONTACT, object.getObjectId(),
                        new PushListener() {
                    @Override
                    public void onSuccess() {
                        showToast("发送请求成功,等待回复");
                        Log.d(TAG, "发送请求成功,等待回复");
                        progress.dismiss();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.d(TAG,"发送请求失败:"+s);
                        progress.dismiss();
                    }
                });
            }
        });
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
