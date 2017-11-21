package com.jimo.mycost.util;

import android.support.design.widget.Snackbar;
import android.view.View;

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
}
