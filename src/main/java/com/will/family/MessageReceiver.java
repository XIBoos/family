package com.will.family;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.inteface.EventListener;
import cn.bmob.im.util.BmobLog;

/**
 * Created by will on 2016/2/11.
 */
public class MessageReceiver extends BroadcastReceiver {

    public static ArrayList<EventListener> ListenerList = new ArrayList<EventListener>();

    private int NewMsgNum=0;
    BmobUserManager userManager;
    BmobChatUser chatUser;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("myself", "receiver");
        //abortBroadcast();
    }

    private void AnalyzeMessage(){

    }

    public void registerListener(EventListener listener){
        ListenerList.add(listener);
    }


    public void setNewMsgNum(int newMsgNum) {
        NewMsgNum = newMsgNum;
    }

    public int getNewMsgNum() {

        return NewMsgNum;
    }
}
