package com.will.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.bmob.im.bean.BmobChatUser;

/**
 * Created by will on 2016/1/21.
 */
public class CollectionUtils {
    public static Map<String,BmobChatUser> List2Map(List<BmobChatUser> users){
        Map<String,BmobChatUser> friends=new HashMap<String,BmobChatUser>();
        for (BmobChatUser i:users) {

            friends.put(i.getUsername(),i);

        }
        return friends;
    }

    public static List<BmobChatUser> Map2List(Map<String,BmobChatUser> map){

        List<BmobChatUser> list=new ArrayList<BmobChatUser>();
        Iterator<Map.Entry<String,BmobChatUser>> iterator=map.entrySet().iterator();
        if(iterator.hasNext()){
            Map.Entry<String,BmobChatUser> entry=iterator.next();
            list.add(entry.getValue());
        }
        return list;
    }
}
