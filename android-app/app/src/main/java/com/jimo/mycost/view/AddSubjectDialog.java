package com.jimo.mycost.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.jimo.mycost.R;
import com.jimo.mycost.util.JimoUtil;

import java.util.Calendar;

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
        final TextView tvDate = (TextView) view.findViewById(R.id.tv_select_date);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        //TODO 重构
                        String date;
                        //为了按日期排序，当9月9号时应该写成09-09,而不是9-9
                        if (i1 < 9) {
                            date = i + "-0" + (i1 + 1);
                        } else {
                            date = i + "-" + (i1 + 1);
                        }
                        if (i2 < 10) {
                            date += "-0" + i2;
                        } else {
                            date += "-" + i2;
                        }
                        tvDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        builder.setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String endDate = tvDate.getText().toString();
                EditText name = (EditText) view.findViewById(R.id.edt_subject_name);
                String subjectName = name.getText().toString();
                if ("".equals(subjectName)) {
                    JimoUtil.mySnackbar(tvDate, "参数不正确");
                    return;
                }
                callback.onOk(subjectName, endDate);
            }
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
