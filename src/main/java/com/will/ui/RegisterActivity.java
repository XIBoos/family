package com.will.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.will.family.R;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BassActivity {

    private EditText Register_Phone;
    private EditText Register_Pass1;
    private EditText Register_Pass2;
    private Button Register_Button;
    private String phone;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Register_Phone=(EditText)findViewById(R.id.register_Phone);
        Register_Pass1=(EditText)findViewById(R.id.register_Pass1);
        Register_Pass2=(EditText)findViewById(R.id.register_Pass2);
        Register_Button=(Button)findViewById(R.id.User_register);
        Register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone=Register_Phone.getText().toString();
                password=Register_Pass1.getText().toString();
                if(Check_Valid()){
                    BmobChatUser register_User=new BmobChatUser();
                    register_User.setDeviceType("android");
                    register_User.setUsername(phone);

                    register_User.setPassword(password);
                    register_User.signUp(RegisterActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }


        });
    }
    private Boolean Check_Valid(){

        if(TextUtils.isEmpty(phone)){
            Register_Phone.setError(getString(R.string.error_Empty_phone));
            return false;
        }else if(Register_Phone.getText().toString().length()!=11){Register_Phone.setError(getString(R.string.error_invalid_phone));
            return false;
        }

        if(TextUtils.isEmpty(password)){
            Register_Pass1.setError(getString(R.string.error_invalid_password));
            return false;
        }else if(password.equals(Register_Pass2.getText().toString())){
            return true;
        }else Register_Pass2.setError(getString(R.string.error_password_not_the_same));

        return false;
    }

}
