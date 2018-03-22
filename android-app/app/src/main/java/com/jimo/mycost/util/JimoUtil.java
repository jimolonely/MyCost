package com.jimo.mycost.util;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import com.jimo.mycost.model.RangeDate;

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

    public static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
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
     * @param offset
     * @return
     */
    private static int getWeekOfYear(int currCount, int offset) {
        if (currCount + offset <= 0) {
            return 52;
        } else if (currCount + offset > 12) {
            return 1;
        }
        if (currCount > 0) {
            return currCount + offset;
        } else {
            final Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.setTime(new Date());
            return calendar.get(Calendar.WEEK_OF_YEAR) + offset;
        }
    }

    private static int getMonthOfYear(int currCount, int offset) {
        if (currCount + offset <= 0) {
            return 12;
        } else if (currCount + offset > 12) {
            return 1;
        }
        if (currCount > 0) {
            return currCount + offset;
        } else {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            return calendar.get(Calendar.MONTH) + offset;
        }
    }

    public static RangeDate getChoiceDateRange(String freq, int currCount, int offset) {
        RangeDate rangeDate;
        int count;
        switch (freq) {
            case "月":
                count = getMonthOfYear(currCount, offset);
                rangeDate = new RangeDate(getFirstDayOfMonth(count), getLastDayOfMonth(count));
                break;
            case "年":
                count = getOffsetYear(offset);
                rangeDate = new RangeDate(count + "-01-01", count + "-12-31");
                break;
            case "周":
            default:
                count = getWeekOfYear(currCount, offset);
                rangeDate = new RangeDate(getDayOfWeek(getOffsetYear(0), count, 0),
                        getDayOfWeek(getOffsetYear(0), count, 1));
        }
        return rangeDate;
    }

    private static int getOffsetYear(int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.YEAR) + offset;
    }

    /**
     * 取得周的偏移日期字符串
     *
     * @param year
     * @param week
     * @param offset 仅仅为了复用代码
     * @return
     */
    private static String getDayOfWeek(int year, int week, int offset) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR, week + offset);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return formatDate(c.getTime());
    }

    public static String getFirstDayOfMonth(int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return formatDate(c.getTime());
    }

    public static String getLastDayOfMonth(int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, 0);
        return formatDate(c.getTime());
    }
}
