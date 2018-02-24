package com.jimo.mycost.ui;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jimo.mycost.R;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Calendar;

/**
 * Created by jimo on 18-2-24.
 * 记录身体数据
 */
@ContentView(R.layout.fragment_body_data)
public class BodyDataFragment extends Fragment {

    @ViewInject(R.id.sp_body)
    Spinner sp_body;
    @ViewInject(R.id.input_date)
    TextView tv_date;
    @ViewInject(R.id.input_body)
    EditText edt_data;
    @ViewInject(R.id.tv_unit)
    TextView tv_unit;

    private String[] bodyData = {"体重", "胸围", "腰围"};
    private String[] dataUnit = {"kg", "cm", "cm"};//数据的单位
    private ArrayAdapter<String> adapter;
    private String bodyPart;
    private String date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initData();
        return view;
    }

    private void initData() {
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, bodyData);
        sp_body.setAdapter(adapter);
        sp_body.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bodyPart = adapter.getItem(i);
                tv_unit.setText(dataUnit[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Event(R.id.input_date)
    private void dateClick(View view) {
        //TODO 重构
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
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
                tv_date.setText(date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    @Event(R.id.btn_finish)
    private void finishClick(View view) {
        if (checkInput(view)) {
            try {
                float data = Float.parseFloat(String.valueOf(edt_data.getText()));
                String unit = String.valueOf(tv_unit.getText());
            } catch (Exception e) {
                Snackbar.make(view, "error", Snackbar.LENGTH_SHORT).show();
                return;
            }
//            if (localStore(view)) {
//                clearInput();
//                JimoUtil.mySnackbar(view, "保存成功");
//            }
        }
    }

    private boolean checkInput(View view) {
        return true;
    }
}
