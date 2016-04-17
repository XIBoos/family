package com.will.ui;

import android.annotation.SuppressLint;
import android.content.Intent;


import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;


import com.will.family.MyApplication;
import com.will.family.R;
import com.will.utils.CollectionUtils;

import java.util.List;
import java.util.Map;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BassActivity {

    private static final int SUCCESS =100;
    // UI references.
    private AutoCompleteTextView mPhoneView;
    private EditText mPasswordView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }


    private void init(){
        mPhoneView = (AutoCompleteTextView) findViewById(R.id.phone);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button loginButton = (Button) findViewById(R.id.sign_in_button);
        Button mRegisterButton=(Button)findViewById(R.id.register_button);

        //TODO:setup password VIEW's Listener
        mPasswordView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (TextUtils.isEmpty(mPhoneView.getText().toString())) {
                    mPhoneView.setError(getString(R.string.error_Empty_phone));
                    return true;
                } else if (!isPhoneValid(mPhoneView.getText().toString())) {
                    mPhoneView.setError(getString(R.string.error_invalid_phone));
                    return true;
                }
                return false;
            }
        });

        //TODO:setup login Button's Listener
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = mPhoneView.getText().toString();
                String password = mPasswordView.getText().toString();

                if (isTrue(phone,password)) {
                    startProgress("正在登陆", false);
                    BmobChatUser user = new BmobChatUser();
                    user.setUsername(phone);
                    user.setPassword(password);
                    userManager.login(user, new SaveListener() {

                        @Override
                        public void onSuccess() {
                            Log.d(TAG,"LoginActivity_LoginSuccess");
                            loginHandler.sendEmptyMessage(SUCCESS);

                        }

                        @Override
                        public void onFailure(int errorcode, String arg0) {
                            // TODO Auto-generated method stub
                            endProgress();
                            showToast("登录失败");
                        }
                    });
                }
            }
        });

        //TODO:setup register Button's Listener
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler loginHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what) {
                case SUCCESS:
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    endProgress();
                    finish();
            }
        }
    };

    private boolean isTrue(String phone,String password){
        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError(getString(R.string.error_Empty_phone));
            return false;
        } else if (!isPhoneValid(phone)) {
            mPhoneView.setError(getString(R.string.error_invalid_phone));
            return false;
        }
        if (!isPasswordValid(password) || TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.setText("");
            return false;
        }
        return true;
    }

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.length()==11;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


}

