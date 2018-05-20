package com.jimo.mycost.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * 还没取好名字
 */
public class FuckUtil {

    //TODO 重构其他地方

    //    public static void clearInput(MyCallback.CommonCallback callback, EditText... edts) {
//        for (EditText edt : edts) {
//            edt.setText("");
//        }
//        callback.doSomething(null);
//    }
//
//    public static void clearInput(MyCallback.CommonCallback callback, TextView... tvs) {
//        for (TextView tv : tvs) {
//            tv.setText("");
//        }
//        callback.doSomething(null);
//    }
    public static void clearInput(MyCallback.CommonCallback callback, View... views) {
        for (View v : views) {
            ((TextView) v).setText("");//因为EditView是TextView的 子类,所以两个都兼容了
        }
        callback.doSomething(null);
    }


    public static boolean checkInput(Context context, View... views) {
        for (View v : views) {
            if (TextUtils.isEmpty(((TextView) v).getText())) {
                JimoUtil.myToast(context, "不能为空");
                return false;
            }
        }
        return true;
    }


//    public static boolean checkInput(Context context, TextView... tvs) {
//        for (TextView tv : tvs) {
//            if (TextUtils.isEmpty(tv.getText())) {
//                JimoUtil.myToast(context, "不能为空");
//                return false;
//            }
//        }
//        return true;
//    }

    /**
     * 弹出选择日期的对话框
     *
     * @param context
     * @param callback
     */
    public static void showDateSelectDialog(Context context, MyCallback.CommonCallback callback) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, year1, month1, dayOfMonth) -> {
            String date;
            //为了按日期排序，当9月9号时应该写成09-09,而不是9-9
            if (month1 < 9) {
                date = year1 + "-0" + (month1 + 1);
            } else {
                date = year1 + "-" + (month1 + 1);
            }
            if (dayOfMonth < 10) {
                date += "-0" + dayOfMonth;
            } else {
                date += "-" + dayOfMonth;
            }
            callback.doSomething(date);
        }, year, month, day);
        datePickerDialog.show();
    }
}
