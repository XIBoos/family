package com.will.User;

import cn.bmob.im.bean.BmobChatUser;

/**
 * Created by will on 2016/1/29.
 */
public class User extends BmobChatUser {

    private String RemarkName;
    public void SetRemarkName(String n){
        RemarkName=n;
    }

    public String getRemarkName(){
        if(RemarkName!=null){
            return RemarkName;
        }else {
            RemarkName="";
            return RemarkName;
        }

    }
}
