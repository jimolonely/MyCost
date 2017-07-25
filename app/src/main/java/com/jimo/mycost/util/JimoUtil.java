package com.jimo.mycost.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by root on 17-7-25.
 */

public class JimoUtil {

    public static void mySnackbar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }
}
