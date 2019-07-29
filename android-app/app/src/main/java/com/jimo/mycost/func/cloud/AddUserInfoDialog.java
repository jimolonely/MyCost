package com.jimo.mycost.func.cloud;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jimo.mycost.R;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;

/**
 * 添加云用户密码信息弹框
 *
 * @author jimo
 * @date 19-7-29 下午7:24
 */
public class AddUserInfoDialog extends DialogFragment {

    public interface Callback {
        void onOk(String username, String password);
    }

    private Context context;

    private Callback callback;

    public void show(FragmentManager fragmentManager, Callback callback, Context context) {
        this.callback = callback;
        this.context = context;
        show(fragmentManager, "AddUserInfoDialog");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_cloud_add_user_info, null);
        builder.setView(view).setPositiveButton("确定", (dialogInterface, i) -> {

            EditText name = view.findViewById(R.id.edt_username);
            EditText pass = view.findViewById(R.id.edt_password);
            String username = name.getText().toString();
            String password = pass.getText().toString();
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                JimoUtil.mySnackbar(name, "参数不正确");
                return;
            }
            // 验证连通性
            callback.onOk(username, password);
        });
        builder.setTitle("完善云用户信息");
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }
}
