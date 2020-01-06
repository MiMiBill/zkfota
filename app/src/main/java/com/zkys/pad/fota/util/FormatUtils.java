package com.zkys.pad.fota.util;

import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormatUtils {

    /**
     * 日期格式化
     */
    public static class FormatDateUtil {

        public static final String formatDate = "yyyy-MM-dd HH:mm:ss";
        public static final String formatDate2 = "yyyy-MM-dd";


        /**
         * 13时间戳转换成 指定格式字符串
         *
         * @return
         */
        public static final String parseDateLong(String formatDate,String timeMillis)  {
            Calendar expireData = Calendar.getInstance();
            expireData.setTimeInMillis(Long.parseLong(timeMillis));
            return formatDate(formatDate, expireData.getTime());
        }



        /**
         * 十位时间戳转换成 @Date
         *
         * @return
         */
        public static final Date parseDate(String date)  {
            Calendar expireData = Calendar.getInstance();
            expireData.setTimeInMillis(Long.parseLong(date) * 1000);
            return expireData.getTime();
        }

        /**
         * 判断时间戳是否在有效期之内
         * @param date
         * @return
         */
        public static final boolean isValid(String date) {
            Calendar expireData = Calendar.getInstance();
            expireData.setTimeInMillis(Long.parseLong(date) * 1000);
            return expireData.getTimeInMillis() >= new Date().getTime();
        }

        public static final String formatDateHHmm() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(calendar.getTime());
        }

        /**
         * 将时分切割
         * @param time
         * @return
         */
        public static final int[] formatDateHHmm(String time) {
            Logger.d("time:%s", time);
            String[] hm = time.split(":");
            int[] ints = new int[]{Integer.parseInt(hm[0]), Integer.parseInt(hm[1])};
            return ints;
        }

        /**
         * 当前时间转换成指定格式
         * @param format
         * @return
         */
        public static  String formatDate(String format) {
            return formatDate(format, new Date());
        }

        /**
         * 指定时间格式化指定字符串
         * @param format
         * @param time
         * @return
         */
        public static String formatDate(String format, Date time) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(time);
        }

        public static final String formatDateWeek() {
            String[] arr = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};

            Calendar calendar = Calendar.getInstance();
            String week = arr[calendar.get(calendar.DAY_OF_WEEK) - 1];

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 ");

            return sdf.format(calendar.getTime()) + week;
        }



    }


}
