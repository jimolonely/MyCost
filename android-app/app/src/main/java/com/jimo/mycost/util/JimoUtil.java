package com.jimo.mycost.util;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by root on 17-7-25.
 */

public class JimoUtil {

    public static void mySnackbar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }


    public static int getYear(String date) {
        int first = date.indexOf('-');
        try {
            return Integer.parseInt(date.substring(0, first));
        } catch (Exception e) {
            return Calendar.getInstance().get(Calendar.YEAR);
        }
    }

    public static int getMonth(String date) {
        int first = date.indexOf('-') + 1;
        int last = date.lastIndexOf('-');
        try {
            return Integer.parseInt(date.substring(first, last));
        } catch (Exception e) {
            return Calendar.getInstance().get(Calendar.MONTH) + 1;
        }
    }

    public static String getDateTimeNow() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 做个时间偏移
     *
     * @param distanceDay
     * @return
     */
    public static String getDateBefore(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    /**
     * 相加的时间格式为 00:00:00 或则 00:00
     *
     * @param time
     * @param time2
     * @return
     */
    public static String addTwoTime(String time, String time2) {
        time = concat(time);
        time2 = concat(time2);
        String[] t1 = time.split(":");
        String[] t2 = time2.split(":");
        int carry = 0;
        String[] re = new String[t1.length];
        for (int i = t1.length - 1; i >= 0; i--) {
            int t = Integer.parseInt(t1[i]) + Integer.parseInt(t2[i]) + carry;
            carry = t / 60;
            t %= 60;
            re[i] = t / 10 == 0 ? "0" + t : t + "";
        }
        return TextUtils.join(":", re);
    }

    /**
     * 补齐为 00:00:00 这种格式
     *
     * @param time2
     * @return
     */
    @NonNull
    private static String concat(String time2) {
        if (time2.length() == 5) {
            time2 = "00:" + time2;
        }
        return time2;
    }

    /**
     * 取得一年中的第几周
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }
}
