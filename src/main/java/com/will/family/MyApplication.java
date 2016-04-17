package com.will.family;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.will.utils.CollectionUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;

/**
 * Created by will on 2016/1/21.
 */
public class MyApplication extends Application {

    public static MyApplication myApplication;


    //谨记application被系统杀掉的问题
    private List<BmobChatUser> myContacts;

    @Override
    public void onCreate(){
        super.onCreate();
        myApplication=this;
        init();
    }


    private void init(){
        InitImageLoader(getApplicationContext());
        if(BmobUserManager.getInstance(getApplicationContext()).getCurrentUser()!=null){
            Log.d("family","application_用户已登录");
            myContacts = BmobDB.create(getApplicationContext()).getContactList();
            Log.d("family","myContactsSize"+myContacts.size());
            myContacts.remove(0);
        }
    }

    public static MyApplication getInstance(){
        return myApplication;
    }

    public void setmyContaction(List<BmobChatUser> contaction){
        myContacts =contaction;
    }

    public List<BmobChatUser> getmyContaction(){
        if(myContacts!=null)
            return myContacts;
        else
            return null;
    }

    public void InitImageLoader(Context context){
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,"family/Cache");
        ImageLoaderConfiguration configuration=new ImageLoaderConfiguration
                .Builder(context)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY-2)
                .memoryCache(new WeakMemoryCache())
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCache(new UnlimitedDiskCache(cacheDir))
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(configuration);
    }

}
