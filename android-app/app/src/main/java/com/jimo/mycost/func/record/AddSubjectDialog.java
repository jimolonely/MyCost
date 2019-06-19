package com.jimo.mycost.func.record;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jimo.mycost.R;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;

/**
 * Created by jimo on 17-11-21.
 * 增加主题的自定义对话框
 */
public class AddSubjectDialog extends DialogFragment {

    public interface Callback {
        void onOk(String subjectName, String endDate);
    }

    private Callback callback;

    public void show(FragmentManager fragmentManager, Callback callback) {
        this.callback = callback;
        show(fragmentManager, "AddSubjectDialog");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_add_subject, null);
        final TextView tvDate = view.findViewById(R.id.tv_select_date);
        tvDate.setOnClickListener(view1 -> FuckUtil.showDateSelectDialog(getContext(), (date) -> tvDate.setText((CharSequence) date)));
        builder.setView(view).setPositiveButton("确定", (dialogInterface, i) -> {
            String endDate = tvDate.getText().toString();
            EditText name = view.findViewById(R.id.edt_subject_name);
            String subjectName = name.getText().toString();
            if ("".equals(subjectName)) {
                JimoUtil.mySnackbar(tvDate, "参数不正确");
                return;
            }
            callback.onOk(subjectName, endDate);
        });
        builder.setTitle("添加主题");
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }
}
