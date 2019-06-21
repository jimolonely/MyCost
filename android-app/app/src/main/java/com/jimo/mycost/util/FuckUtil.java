package com.jimo.mycost.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jimo.mycost.R;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.util.Calendar;
import java.util.Random;

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
        new DatePickerDialog(context, (datePicker, year1, month1, dayOfMonth) -> {
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
        }, year, month, day).show();
    }

    /**
     * 弹出时间选择对话框
     *
     * @param context
     * @param callback
     */
    public static void showTimeSelectDialog(Context context, MyCallback.CommonCallback callback) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute1 = c.get(Calendar.MINUTE);
        new TimePickerDialog(context, (view1, hourOfDay, minute) -> {
            String h = hourOfDay + "";
            if (hourOfDay < 10) {
                h = "0" + hourOfDay;
            }
            String m = minute + "";
            if (minute < 10) {
                m = "0" + minute;
            }
            final String watchTime = h + ":" + m;
            callback.doSomething(watchTime);
        }, hour, minute1, true).show();
    }

    /**
     * 把图片加载封装了
     *
     * @param imgPath
     * @param callback
     */
    public static void loadImg(String imgPath, MyCallback.CommonCallback callback) {
        ImageOptions imageOptions = new ImageOptions.Builder().setFadeIn(true)
                .setCrop(true).setSize(100, 100) //设置大小
                .build();
        x.image().loadDrawable(imgPath, imageOptions, new Callback.CommonCallback<Drawable>() {

            @Override
            public void onSuccess(Drawable result) {
                Log.i("Fuck-loadImg", result.toString());
                callback.doSomething(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @NonNull
    public static TextView getTextView(Context context, String s, View.OnClickListener listener) {
        TextView tv = new TextView(context);
        tv.setText(s);
        tv.setTextSize(18);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(10, 5, 10, 5);
        tv.setOnClickListener(listener);
        tv.setTextColor(context.getResources().getColor(R.color.secondary_text));
        return tv;
    }

    /**
     * 随机生成颜色
     *
     * @author jimo
     * @date 18-10-21 下午5:41
     */
    public static int[] getRandomColors(int num) {
        if (num <= 0) {
            return new int[0];
        }
        int[] colors = new int[num];
        Random r = new Random();
        for (int i = 0; i < num; i++) {
            colors[i] = Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255));
        }
        return colors;
    }
}
