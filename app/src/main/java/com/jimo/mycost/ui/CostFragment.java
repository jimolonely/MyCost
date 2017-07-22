package com.jimo.mycost.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.jimo.mycost.MyApp;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.model.CostInComeRecord;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by root on 17-7-19.
 * 支出
 */
@ContentView(R.layout.cost_fragment)
public class CostFragment extends Fragment {

    @ViewInject(R.id.fbl_food)
    FlexboxLayout fl_food;

    @ViewInject(R.id.input_date)
    TextView input_date;

    @ViewInject(R.id.input_type)
    TextView input_type;

    @ViewInject(R.id.input_money)
    EditText input_money;

    @ViewInject(R.id.input_remark)
    EditText input_remark;//备注

    @ViewInject(R.id.btn_finish)
    Button btn_finish;

    private List<String> foodTitles = new ArrayList<>(Arrays.asList("早餐", "午餐", "晚餐", "其他"));

    //存储输入的值
    private String date;
    private String type;
    private float money;
    private String remark;

    //同步类型。默认是插入
    private int modifyType = MyConst.SYNC_TYPE_INSERT;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);

        initData();

        initViews();

//        return inflater.inflate(R.layout.cost_fragment,container,false);
        return view;

    }

    private void initViews() {
    }

    private void initData() {
        //food
        for (String s : foodTitles) {
            TextView tv = new TextView(getContext());
            tv.setText(s);
            tv.setTextSize(18);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(10, 5, 10, 5);
            tv.setOnClickListener(new FoodOnClickListener());
            fl_food.addView(tv);
        }

        //transport

    }

    /**
     * 提交
     *
     * @param view
     */
    @Event(R.id.btn_finish)
    private void finishClick(View view) {
        money = Float.parseFloat(String.valueOf(input_money.getText()));
        remark = String.valueOf(input_remark.getText());
        if (checkInput(view)) {

            localStore();

            cloudStore();
        }
    }


    /**
     * 同步云端
     */
    private void cloudStore() {

    }

    /**
     * 存本地数据库
     */
    private void localStore() {

        DbManager db = MyApp.dbManager;

        switch (modifyType) {
            case MyConst.SYNC_TYPE_INSERT:
                CostInComeRecord cost = new CostInComeRecord(MyConst.COST, money,
                        remark, date, type, MyConst.getUserName(getContext()), MyConst.SYNC_TYPE_INSERT);
                try {
                    db.save(cost);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    /**
     * 检查输入
     */
    private boolean checkInput(View view) {
        if (TextUtils.isEmpty(type)) {
            Snackbar.make(view, "选择用途", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(String.valueOf(money))) {
            Snackbar.make(view, "输入金额", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Event(R.id.input_date)
    private void dateClick(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                date = i + "-" + (i1 + 1) + "-" + "-" + i2;
                input_date.setText(date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private class FoodOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                int i = foodTitles.indexOf(tv.getText());
                switch (i) {
                    case 0:
                        Snackbar.make(view, tv.getText(), Snackbar.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Snackbar.make(view, tv.getText(), Snackbar.LENGTH_SHORT).show();
                        break;

                }
            }
        }
    }


    //在从主页面点击一条数据进来时确定是修改

}
