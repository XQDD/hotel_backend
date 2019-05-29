package com.zheng.hotel.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtils {
    //年月日期格式化
    public final static SimpleDateFormat DATE_YEAR_MOUTH_FORMAT = new SimpleDateFormat("yyyyMM");
    //普通日期格式化
    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    //日期时间
    public final static SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //精确到微秒日期时间格式化
    public final static SimpleDateFormat DATETIME_FORMAT_WITH_MS = new SimpleDateFormat("yyyy年MM月dd号H时m分s.sss秒");
    public final static SimpleDateFormat DATE_FORMAT_WITHOUT_DASH = new SimpleDateFormat("yyyyMMdd");
    public final static long SECONDS = 1000;
    public final static long MINUTE = 60 * SECONDS;
    public final static long HOUR = 60 * MINUTE;
    public final static long DAY = 24 * HOUR;
    public final static long WEEK = 7 * DAY;
    public final static long MONTH = 30 * DAY;
    public final static long YEAR = 365 * DAY;


    /**
     * 获取当前年份日历
     *
     * @return 日历
     */
    public static Calendar getYear() {
        return new Calendar.Builder().set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR)).build();
    }


    /**
     * 获取当前日期日历
     *
     * @return 日历
     */
    private static Calendar getCurrDate() {
        Calendar currDate = Calendar.getInstance();
        currDate.set(Calendar.HOUR_OF_DAY, 0);
        currDate.clear(Calendar.SECOND);
        currDate.clear(Calendar.MINUTE);
        currDate.clear(Calendar.MILLISECOND);
        return currDate;
    }

    /**
     * 获取当前月日历
     *
     * @return 日历
     */
    public static Calendar getMonth() {
        Calendar curr = Calendar.getInstance();
        return new Calendar.Builder().set(Calendar.MONTH, curr.get(Calendar.MONTH)).set(Calendar.YEAR, curr.get(Calendar.YEAR)).build();
    }


    /**
     * 获取明天
     *
     * @return 格式化后的时间
     */
    public static String getTomorrowString() {
        return DATE_FORMAT.format(getTomorrowCalendar().getTime());
    }

    /**
     * 获取明天时间戳
     *
     * @return 时间戳
     */
    public static long getTomorrowTimeMillis() {
        return getTomorrowCalendar().getTimeInMillis();
    }

    /**
     * 获取明天日历
     *
     * @return 日历
     */
    public static Calendar getTomorrowCalendar() {
        Calendar curr = getCurrDate();
        curr.add(Calendar.DATE, 1);
        return curr;
    }


    /**
     * 获取今天的时间戳
     *
     * @return 时间戳
     */
    public static long getTodayTimeMillis() {
        return getCurrDate().getTimeInMillis();
    }


}
