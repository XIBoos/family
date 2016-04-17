package com.will.ui;


import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.will.User.User;
import com.will.adaptor.ContactAdapet;
import com.will.bean.TimeLineItem;
import com.will.family.MyApplication;
import com.will.family.R;
import com.will.layout.main_ScrollView;
import com.will.layout.main_contactView;
import com.will.widget.CircleImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MainActivity extends BassActivity {

    MyApplication myApplication;
    NewBroadcastReceiver receiver;
    Context context;
    private List<BmobChatUser> contact;
    private ContactAdapet adapet;
    private List<TimeLineItem> list;

    //widget
    private ListView listView_contact;
    private Button but_Menu;
    private boolean menuTip=false;

    private main_contactView contactView;

    private main_ScrollView scroll_mainView;


    public int setAvatarFromMerory=0;
    public int CROPIMAGE=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myApplication=MyApplication.getInstance();
        context=this;
        InitView();
    }


    private void InitView(){

        scroll_mainView=(main_ScrollView)findViewById(R.id.scroll_mainView);
        but_Menu =(Button)findViewById(R.id.main_but_menu);
        but_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuTip) {
                    scroll_mainView.smoothScrollTo(main_ScrollView.settingWidth, 0);
                    menuTip = false;
                } else {
                    scroll_mainView.smoothScrollTo(0, 0);
                    menuTip = true;
                }
            }
        });
        Log.d("family","44444");
        InitSettingView();
        InitContactView();
    }

    private void InitSettingView(){
        Button button1=(Button)findViewById(R.id.btn_setting);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button button2=(Button)findViewById(R.id.btn_unloging);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("family", "logout");
                userManager.logout();
                context.startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    //初始化联系人视图
    private void InitContactView(){
        CircleImageView top_avatar=(CircleImageView)findViewById(R.id.main_contact_top_avat);
        top_avatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,setAvatarFromMerory);
                return true;
            }
        });
        contactView=(main_contactView)findViewById(R.id.main_contactView);
        if(MyApplication.getInstance().getmyContaction()!=null){
            Log.d(TAG,"MainActivity_InitContactView");
            contact= MyApplication.getInstance().getmyContaction();
        }
        contactView.InitCurrentUser(userManager.getCurrentUser());
        List<BmobChatUser> temp=contact;
        if (temp==null)
            temp = new ArrayList<>();
        contactView.UpdataContactView(MainActivity.this, temp);
        queryContact();
    }


    //查询联网联系人
    private void queryContact(){
        Log.d(TAG,"MainActivity_queryContact");
        userManager.queryCurrentContactList(new FindListener<BmobChatUser>() {
            @Override
            public void onSuccess(List<BmobChatUser> list) {
                showToast("联系人列表更新成功");
                contact = list;
                list.remove(0);
                contactView.UpdataContactView(MainActivity.this, list);
            }

            @Override
            public void onError(int i, String s) {
                showToast("联系人列表更新失败:" + s);
            }
        });
    }

    float start;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                start=event.getRawX();

                return true;
            case MotionEvent.ACTION_MOVE:

                return true;
            default:
                if (event.getRawX()-start>=5){
                    contactView.display(contactView.HIDE);
                }else if(event.getRawX()-start<=-5){
                    contactView.display(contactView.SHOW);
                }
                return true;
        }
    }
    private Uri extreURI;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==setAvatarFromMerory){
                if(data==null)
                    return;
                Uri uri=data.getData();
                File extrefile=new File("/storage/emulated/0/tencent/MicroMsg/WeiXin/temp.jpg");
                if(!extrefile.exists()) {
                    try {
                        extrefile.createNewFile();
                    } catch (Exception e) {
                        Log.e("family", e.toString());
                    }
                }
                extreURI=Uri.fromFile(extrefile);
                Intent intent=new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(uri, "image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, extreURI);
                intent.putExtra("return-data", false);
                intent.putExtra("noFaceDetection", true);
                startActivityForResult(intent, CROPIMAGE);
            }
            if(requestCode==CROPIMAGE){

                Log.d("family",extreURI.toString());
                final File temp=new File(extreURI.getPath());
                final BmobFile file=new BmobFile(temp);
                file.upload(this, new UploadFileListener() {
                    @Override
                    public void onSuccess() {
                        String url = file.getFileUrl(MainActivity.this);
                        temp.delete();
                        Log.d("family", url);
                        updataAvatar(userManager.getCurrentUser(),url);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.d("family", "上传失败" + s);
                    }
                });
            }
        }
    }

    private void updataAvatar(BmobChatUser user, final String url){
        user.setAvatar(url);
        user.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.d("family","头像跟新成功");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.d("family", "头像跟新失败");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiver=new NewBroadcastReceiver();
        IntentFilter intentFilter=new IntentFilter(BmobConfig.BROADCAST_NEW_MESSAGE);
        intentFilter.setPriority(3);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public class NewBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 记得把广播给终结掉
            Toast.makeText(MainActivity.this,"mainactivity",Toast.LENGTH_SHORT).show();
            abortBroadcast();
        }
    }

    public class MYBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context,Intent intent){


        }
    }

    public class TWOBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context,Intent intent){
            //abortBroadcast();
        }

    }

    private class TagBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //推送的标签对象
            BmobInvitation message=(BmobInvitation)intent
                    .getSerializableExtra("invite");
        }
    }

}
