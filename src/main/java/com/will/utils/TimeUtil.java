package com.will.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by will on 2016/2/17.
 */
public class TimeUtil {
    public final static String FORMAT_YEAR="yyyy";
    public final static String FORMAT_MONTH_DAY="MM月dd日";

    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_TIME = "HH:mm";
    public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日  hh:mm";

    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    //public final static String FORMAT_DATE1_TIME = "yyyy/MM/dd HH:mm";
    public final static String FORMAT_DATE_TIME_SECOND = "yyyy/MM/dd HH:mm:ss";

    private static final int YEAR = 365 * 24 * 60 * 60;// 年
    private static final int MONTH = 30 * 24 * 60 * 60;// 月
    private static final int DAY = 24 * 60 * 60;// 天
    private static final int HOUR = 60 * 60;// 小时
    private static final int MINUTE = 60;// 分钟


    /*
    *获取当前时间
    *@param Format 格式化格式
    * */
    public static String getCurrentTime(String Format){
        SimpleDateFormat dateformat =new SimpleDateFormat();
        if(Format==null||Format.trim().equals("")){
            dateformat.applyPattern(FORMAT_DATE_TIME);
        }else {
            dateformat.applyPattern(Format);
        }
        return dateformat.format(new Date());
    }
    /*
    * 获取讯息记录时间
    * @param 时间戳
    * */
    public static String getChatTime(long timestamp){
        long cleartime=timestamp*1000;
        String result="";
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat dateformat =new SimpleDateFormat("dd");
        int temp = Integer.parseInt(dateformat.format(now))
                - Integer.parseInt(dateformat.format(new Date(cleartime)));
        switch (temp){
            case 0:
                result="今天 "+getHourAndMin(cleartime);
                return result;
            case 1:
                result="昨天 "+getHourAndMin(cleartime);
                return result;
            case 2:
                result="前天 "+getHourAndMin(cleartime);
                return result;
            default:

                result=getTime(cleartime);
                return result;
        }
    }

    /*
    * 返回 HH:mm 格式的时间
    * */
    public static String getHourAndMin(long timestamp){
        SimpleDateFormat dateformat =new SimpleDateFormat(FORMAT_TIME);
        return dateformat.format(new Date(timestamp));
    }

    /*
    * 返回 yyyy-MM-dd hh:mm 格式的时间
    * */
    public static String getTime(long timestamp){
        SimpleDateFormat dateformat =new SimpleDateFormat(FORMAT_DATE_TIME);
        return dateformat.format(new Date(timestamp));
    }


}
