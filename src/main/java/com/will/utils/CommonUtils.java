package com.will.utils;

/**
 * Created by will on 2016/3/5.
 */
public class CommonUtils {
    public static Boolean isSDCard(){
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

}
