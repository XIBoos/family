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
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.will.User.User;
import com.will.adaptor.ChatMsgAdapet;
import com.will.adaptor.EmotionHolder;
import com.will.adaptor.ExpressionGridAdapte;
import com.will.adaptor.ExpressionPageAdapet;
import com.will.bean.ExpressionHelper;
import com.will.bean.ExpressionItem;
import com.will.family.R;
import com.will.utils.CommonUtils;
import com.will.widget.ChatMsgList;
import com.will.widget.SpannableEidtText;
import com.will.widget.TopBar;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobRecordManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.OnRecordChangeListener;
import cn.bmob.im.inteface.UploadListener;

public class ChatActivity extends BassActivity {

    public String TAG="ChatActivity";

    private final int initPage=0;
    private int curreatPageNUM=0;

    private BmobChatUser chatObject;
    private String chatID="";

    public int REQUESTCODE_IMAGE_FROM_MEMORY=0;
    public int REQUESTCODE_IMAGE_FROM_CAMERA=1;
    public int REQUESTCODE_LOCATION=2;


    //控件
    private ChatMsgList chatmsglist;
    private ChatMsgAdapet msgAdapet;
    private LinearLayout layout_expression;
    private LinearLayout layout_more;
    private SpannableEidtText bottom_edit;
    private Button bottom_speak;
    private Button bottom_keyboard;
    private Button bottom_voice;
    private Button bottom_send;
    private TopBar topBar;

    private RelativeLayout chat_progress;
    private TextView chat_progress_tip;

    private BmobRecordManager recordManager;

    private NewBroadcastReceiver receiver;

    private ExpressionHelper expressionHelper;

    InputMethodManager inputMethodManager ;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle bundle =getIntent().getExtras();
        chatObject=(BmobChatUser)bundle.getSerializable("user");
        chatID = chatObject.getObjectId();
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        InitTopBar();
        InitMessageList();
        InitNewBroadcast();
        InitExpression();
        InitRecordManager();
        InitButtomBar();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    //底部初始化
    private void InitButtomBar(){
        layout_expression=(LinearLayout)findViewById(R.id.chat_layout_emo);
        layout_more=(LinearLayout)findViewById(R.id.chat_layout_more);
        bottom_edit =(SpannableEidtText)findViewById(R.id.chat_bottom_eidttext);
        bottom_speak=(Button)findViewById(R.id.chat_bottom_speak_button);
        bottom_keyboard=(Button)findViewById(R.id.chat_bottom_keyboard_button);
        bottom_voice=(Button)findViewById(R.id.chat_bottom_voice_button);
        bottom_send=(Button)findViewById(R.id.chat_bottom_send_button);
        //监听者
        bottom_speak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!CommonUtils.isSDCard()) {
                            showToast("发送语音消息需要SDCard支持");
                            return false;
                        }
                        bottom_speak.setPressed(true);
                        chat_progress.setVisibility(View.VISIBLE);
                        chat_progress_tip.setText("上滑取消");

                        try{
                            recordManager.startRecording(chatObject.getObjectId());
                        }catch (Exception e){
                            Log.e("will",e.getMessage(),e);
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if(event.getY()<0){
                            chat_progress_tip.setText("松手取消发送");
                        }else
                            chat_progress_tip.setText("上滑取消");

                        return true;
                    case MotionEvent.ACTION_UP:
                        bottom_speak.setPressed(false);
                        chat_progress.setVisibility(View.INVISIBLE);
                        if(event.getY()<0){
                            try{
                                recordManager.cancelRecording();
                                showToast("放弃录音");
                            }catch (Exception e){
                                Log.e("will",e.getMessage(),e);
                            }
                        }else {
                            try{
                                int recordTime=recordManager.stopRecording();
                                if(recordTime<=1){
                                    showToast("录音时间过短,取消发送");
                                }else{
                                    sendRecordVoice(recordTime,recordManager.getRecordFilePath(chatID));
                                }
                            }catch (Exception e){
                                Log.e("will",e.getMessage(),e);
                            }

                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

        bottom_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content=bottom_edit.getText().toString();
                if(content.equals("")) {
                    Toast.makeText(ChatActivity.this, "请输入发送内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(chatManager!=null) {
                    BmobMsg message = BmobMsg.createTextSendMsg(ChatActivity.this, chatObject.getObjectId(), content);
                    chatManager.sendTextMessage(chatObject, message);
                    refreashMsg(message);
                    bottom_edit.setText("");
                }else
                    Toast.makeText(ChatActivity.this,"kong",Toast.LENGTH_SHORT).show();

            }
        });
        bottom_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    bottom_send.setVisibility(View.VISIBLE);
                    bottom_voice.setVisibility(View.GONE);
                } else {
                    bottom_send.setVisibility(View.GONE);
                    bottom_voice.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //上部初始化
    private void InitTopBar(){
        topBar=(TopBar)findViewById(R.id.chat_layout_topbar);
        if(!TextUtils.isEmpty(chatObject.getNick()))
            topBar.setTitleText(chatObject.getNick());
        else
            topBar.setTitleText(chatObject.getUsername());
        topBar.getLeftButton().setBackgroundResource(R.drawable.but_universal_back);
        topBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topBar.setRightText("资料");
        topBar.setRightOnClickLisener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChatActivity.this,UserInforActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("user",chatObject);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    //聊天记录列表的初始化
    private void InitMessageList(){
        chatmsglist=(ChatMsgList)findViewById(R.id.List);
        msgAdapet=new ChatMsgAdapet(this,QueryMessages(initPage));
        chatmsglist.setAdapter(msgAdapet);
        chatmsglist.setSelection(msgAdapet.getCount()-1);
        chatmsglist.setChatMsgListListener(new ChatMsgList.ChatMsgListListener() {
            @Override
            public void OnLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        curreatPageNUM++;
                        int total = BmobDB.create(ChatActivity.this)
                                .queryChatTotalCount(chatObject.getObjectId());
                        int NUM = msgAdapet.getCount();
                        if (total == NUM) {
                            Toast.makeText(ChatActivity.this, "记录已经加载完毕", Toast.LENGTH_SHORT).show();
                        } else {
                            msgAdapet.addList(QueryMessages(curreatPageNUM));
                            chatmsglist.setSelection(msgAdapet.getCount() - NUM - 1);
                        }
                        //或可以加一个更新时间的操作
                    }
                }, 1000);
            }
        });

    }

    /*初始化语音相关*/
    private void InitRecordManager(){
        //progress
        chat_progress=(RelativeLayout)findViewById(R.id.chat_progress);
        chat_progress.setVisibility(View.INVISIBLE);
        chat_progress_tip=(TextView)findViewById(R.id.progress_tip);



        recordManager=BmobRecordManager.getInstance(this);
        recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {
            @Override
            public void onVolumnChanged(int i) {
                //音量监听接口
            }

            @Override
            public void onTimeChanged(int i, String s) {
                if (i >= BmobRecordManager.MAX_RECORD_TIME) {
                    bottom_speak.setClickable(false);
                    bottom_speak.setPressed(false);
                    sendRecordVoice(i, s);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bottom_speak.setClickable(true);
                        }
                    }, 1000);
                }
            }
        });


    }

    private void sendRecordVoice(int time,String path){
        chatManager.sendVoiceMessage(chatObject, path, time, new UploadListener() {
            @Override
            public void onStart(BmobMsg bmobMsg) {
                msgAdapet.add(bmobMsg);
            }

            @Override
            public void onSuccess() {
                Log.d("will", "录音发送成功");
            }

            @Override
            public void onFailure(int i, String s) {
                msgAdapet.notifyDataSetChanged();//上传失败
            }
        });
    }
    /***********************************/
    // 表情布局的初始化
    private void InitExpression(){
        ViewPager EmoPager=(ViewPager)findViewById(R.id.chat_viewpage_emo);
        expressionHelper=new ExpressionHelper();
        List<View> views=new ArrayList<View>();
        views.add(GetGridView(0));
        views.add(GetGridView(1));
        EmoPager.setAdapter(new ExpressionPageAdapet(views));
    }

    private View GetGridView(int page){
        View view =View.inflate(this,R.layout.chat_layout_emo_gridview,null);
        GridView gridView=(GridView)view.findViewById(R.id.chat_emo_gridView);
        if(page==0){
            List<ExpressionItem> items= expressionHelper.ExpressionList.subList(0,21);
            gridView.setAdapter(new ExpressionGridAdapte(this,items));
        }else if(page==1){
            List<ExpressionItem> items= expressionHelper.ExpressionList.subList(21,
                    expressionHelper.ExpressionList.size());
            gridView.setAdapter(new ExpressionGridAdapte(this,items));
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmotionHolder object = (EmotionHolder) view.getTag();//convertview
                ExpressionItem item = object.getExpressionItem();
                String key = item.getKey();
                if (bottom_edit != null && (!TextUtils.isEmpty(key))) {
                    int start = bottom_edit.getSelectionStart();
                    CharSequence text = bottom_edit.getText().insert(start, key);
                    bottom_edit.setText(text);
                    CharSequence info = bottom_edit.getText();
                    if (info instanceof Spannable) {
                        Spannable spanText = (Spannable) info;
                        Selection.setSelection(spanText,
                                start + key.length());
                    }
                }
            }
        });
        return view;
    }
    /*********************************************/

    /*初始化新消息广播*/
    private void InitNewBroadcast(){
        receiver=new NewBroadcastReceiver();
        IntentFilter action=new IntentFilter(BmobConfig.BROADCAST_NEW_MESSAGE);
        action.setPriority(5);
        registerReceiver(receiver,action);
    }

    public void onClick(View v){
        if(v.getId()==R.id.chat_bottom_emo_button){//表情按钮
            inputMethodManager.hideSoftInputFromWindow(bottom_edit.getWindowToken(),0);
            if(layout_expression.getVisibility()==View.VISIBLE){
                layout_expression.setVisibility(View.GONE);
            }
            else{
                if(layout_more.getVisibility()==View.VISIBLE)
                    layout_more.setVisibility(View.GONE);
                layout_expression.setVisibility(View.VISIBLE);
            }
        }
        if(v.getId()==R.id.chat_bottom_keyboard_button){//键盘按键
            if(bottom_keyboard.getVisibility()==View.VISIBLE){
                bottom_keyboard.setVisibility(View.GONE);
                bottom_voice.setVisibility(View.VISIBLE);
                bottom_edit.setVisibility(View.VISIBLE);
                bottom_speak.setVisibility(View.GONE);
            }else {
                bottom_keyboard.setVisibility(View.VISIBLE);
                bottom_voice.setVisibility(View.GONE);
                bottom_edit.setVisibility(View.GONE);
                bottom_speak.setVisibility(View.VISIBLE);
            }
        }
        if(v.getId()==R.id.chat_bottom_more_button){//更多按键
            if(layout_more.getVisibility()==View.GONE){
                if(layout_expression.getVisibility()==View.VISIBLE)
                    layout_expression.setVisibility(View.GONE);
                layout_more.setVisibility(View.VISIBLE);
            }else
                layout_more.setVisibility(View.GONE);
        }
        if(v.getId()==R.id.chat_bottom_voice_button){//录音按键
            if(bottom_voice.getVisibility()==View.VISIBLE){
                bottom_keyboard.setVisibility(View.VISIBLE);
                bottom_voice.setVisibility(View.GONE);
                bottom_edit.setVisibility(View.GONE);
                bottom_speak.setVisibility(View.VISIBLE);
            }else {
                bottom_keyboard.setVisibility(View.GONE);
                bottom_voice.setVisibility(View.VISIBLE);
                bottom_edit.setVisibility(View.VISIBLE);
                bottom_speak.setVisibility(View.GONE);
            }
        }
        if(v.getId()==R.id.chat_more_camera_button){
            SelectImageFromCamere();
        }
        if(v.getId()==R.id.chat_more_picture_button){
            SelectImageFromMemory();
        }
    }

    private void refreashMsg(BmobMsg msg){
        msgAdapet.add(msg);
        chatmsglist.setSelection(msgAdapet.getCount()-1);

    }


    /*连接数据库如果数据量比较大的话,建议在线程中执行*/
    //待修改成线程模式
    private List<BmobMsg> QueryMessages(int PageNum){

        List<BmobMsg> list=BmobDB.create(this).queryMessages(chatObject.getObjectId(), PageNum);
        return list;
    }

    /*新消息广播*/
    public class NewBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String from=intent.getStringExtra("fromId");
            String msgId=intent.getStringExtra("msgId");
            String msgTime=intent.getStringExtra("msgTime");
            if((!TextUtils.isEmpty(from))&&(!TextUtils.isEmpty(msgId))&&(!TextUtils.isEmpty(msgTime))){
                if(!from.equals(chatObject.getObjectId()))
                    return;
                BmobMsg msg= BmobChatManager.getInstance(ChatActivity.this).getMessage(msgId,msgTime);
                msgAdapet.add(msg);
                chatmsglist.setSelection(msgAdapet.getCount() - 1);
                BmobDB.create(ChatActivity.this).resetUnread(chatObject.getObjectId());
            }
            Toast.makeText(ChatActivity.this,"chatactivity",Toast.LENGTH_SHORT).show();
            abortBroadcast();
        }
    }

    public void SelectImageFromMemory(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,REQUESTCODE_IMAGE_FROM_MEMORY);
    }

    public void SelectImageFromCamere(){
        Intent intent=new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUESTCODE_IMAGE_FROM_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==REQUESTCODE_IMAGE_FROM_MEMORY){
                Uri uri=data.getData();
                ContentResolver resolver=this.getContentResolver();
                try {
                    Bitmap memoryphoto = BitmapFactory.decodeStream(resolver.openInputStream(uri));
                }catch (Exception e){
                    Log.e("exception",e.getMessage(),e);
                }
                Log.d("will","照片获取好了");
            }
            if (requestCode==REQUESTCODE_IMAGE_FROM_CAMERA){
                Bitmap cameraphoto=(Bitmap)data.getExtras().get("data");

            }
        }


    }
}
