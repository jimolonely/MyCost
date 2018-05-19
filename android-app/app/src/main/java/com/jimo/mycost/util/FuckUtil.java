package com.jimo.mycost.util;

import android.widget.EditText;

/**
 * 还没取好名字
 */
public class FuckUtil {

    //TODO 重构其他地方

    /**
     * @param callback 用来清理其他输入
     * @param edts
     */
    public static void clearInput(MyCallback.CommonCallback callback, EditText... edts) {
        for (EditText edt : edts) {
            edt.setText("");
        }
        callback.doSomething();
    }
}
