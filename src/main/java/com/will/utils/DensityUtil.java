package com.will.utils;

import android.content.Context;

/**
 * Created by will on 2016/3/13.
 */
public class DensityUtil {

    public static int dpTOpx(Context context,final int dpValue){
        final float density= context.getResources().getDisplayMetrics().density;
        return (int)(density*dpValue+0.5f);
    }

    public static int pxTOdp(Context context,final int pxValue){
        final float density=context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/density+0.5f);
    }

}
