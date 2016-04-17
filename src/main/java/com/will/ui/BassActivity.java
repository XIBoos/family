package com.will.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.will.family.MyApplication;
import com.will.utils.CollectionUtils;
import com.will.widget.TopBar;

import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by will on 2016/1/21.
 */
public class BassActivity extends AppCompatActivity {


    String TAG="family";

    ProgressDialog myProgress;
    MyApplication myApplication;
    BmobUserManager userManager;
    BmobChatManager chatManager;

    protected TopBar topBar;

    protected int mScreenWidth;
    protected int mScreenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        myApplication=MyApplication.getInstance();
        userManager=BmobUserManager.getInstance(this);
        chatManager=BmobChatManager.getInstance(this);
        myProgress=new ProgressDialog(this);
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenHeight=metrics.heightPixels;
        mScreenWidth=metrics.widthPixels;
    }

    public void showToast(String details){

        Toast.makeText(getApplicationContext(), details, Toast.LENGTH_SHORT).show();
    }

    public void startProgress(String message,Boolean TouthEnable){

        myProgress.setMessage(message);
        myProgress.setCanceledOnTouchOutside(TouthEnable);
        myProgress.show();
    }
    public void endProgress(){
        myProgress.dismiss();

    }

}
