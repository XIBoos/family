package com.will.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.will.family.Constant;
import com.will.family.R;
import com.will.utils.CollectionUtils;

import java.util.List;

import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.FindListener;

public class InitActivity extends BassActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        BmobChat.DEBUG_MODE = true;
        BmobChat.getInstance(this).init(Constant.ApplicationKey);

    }

    @Override
    protected void onResume(){
        super.onResume();

        if(BmobUserManager.getInstance(getApplicationContext()).getCurrentUser()==null){

            startActivity(new Intent(InitActivity.this, LoginActivity.class));
        }else {

            startActivity(new Intent(InitActivity.this,MainActivity.class));
            Log.d(TAG,"InitActivity_用户已登录");
        }
        finish();
    }
}
