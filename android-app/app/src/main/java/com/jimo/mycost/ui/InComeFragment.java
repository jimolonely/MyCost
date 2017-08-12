package com.jimo.mycost.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.jimo.mycost.MyApp;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.model.CostInComeRecord;
import com.jimo.mycost.model.MonthCost;
import com.jimo.mycost.util.JimoUtil;

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

import static com.jimo.mycost.util.JimoUtil.getMonth;
import static com.jimo.mycost.util.JimoUtil.getYear;

/**
 * Created by root on 17-7-19.
 * 收入
 */
@ContentView(R.layout.income_fragment)
public class InComeFragment extends Fragment {

    @ViewInject(R.id.fbl_in_normal)
    FlexboxLayout fl_normal;

    @ViewInject(R.id.input_date1)
    TextView input_date;

    @ViewInject(R.id.input_type1)
    TextView input_type;

    @ViewInject(R.id.input_money1)
    EditText input_money;

    @ViewInject(R.id.input_remark1)
    EditText input_remark;

    private List<String> normalInComes = new ArrayList<>(Arrays.asList("工资", "兼职", "红包", "投资", "奖金", "补贴", "礼金", "其他"));

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

        return view;
    }

    private void initData() {
        //normal
        for (String s : normalInComes) {
            TextView tv = getTextView(s, new NormalOnClickListener());
            fl_normal.addView(tv);
        }
    }

    @NonNull
    private TextView getTextView(String s, View.OnClickListener listener) {
        TextView tv = new TextView(getContext());
        tv.setText(s);
        tv.setTextSize(18);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(10, 5, 10, 5);
        tv.setOnClickListener(listener);
        tv.setTextColor(getResources().getColor(R.color.secondary_text));
        return tv;
    }

    /**
     * 提交
     *
     * @param view
     */
    @Event(R.id.btn_finish1)
    private void finishClick(View view) {
        if (checkInput(view)) {
            try {
                money = Float.parseFloat(String.valueOf(input_money.getText()));
                remark = String.valueOf(input_remark.getText());
            } catch (Exception e) {
                Snackbar.make(view, "error", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (localStore(view)) {
                clearInput();
                JimoUtil.mySnackbar(view, "保存成功");
            }
        }
    }

    //清空输入框
    private void clearInput() {
        input_date.setText("");
        input_type.setText("");
        input_money.setText("");
        input_remark.setText("");
    }

    /**
     * 存本地数据库
     */
    private boolean localStore(View view) {

        DbManager db = MyApp.dbManager;

        switch (modifyType) {
            case MyConst.SYNC_TYPE_INSERT:
                String userName = MyConst.getUserName(getContext());
                CostInComeRecord cost = new CostInComeRecord(MyConst.IN_COME, money,
                        remark, date, type, userName, MyConst.SYNC_TYPE_INSERT);
                //TODO 事务
                try {
                    db.save(cost);

                    int month = getMonth(date);
                    int year = getYear(date);
                    MonthCost monthCost = db.selector(MonthCost.class).
                            where("month", "=", month).and("year", "=", year).
                            and("user_name", "=", userName).and("in_out", "=", MyConst.IN_COME).findFirst();
                    if (monthCost == null) {
                        monthCost = new MonthCost(year, month, money, MyConst.IN_COME, MyConst.SYNC_TYPE_INSERT, userName);
                        db.save(monthCost);
                    } else {
                        monthCost.setSyncType(MyConst.SYNC_TYPE_UPDATE);
                        monthCost.setMoney(monthCost.getMoney() + money);
                        db.update(monthCost, "money", "sync_type");
                    }
                    return true;
                } catch (DbException e) {
                    e.printStackTrace();
                    JimoUtil.mySnackbar(view, "error local store");
                    return false;
                }
        }

        return false;
    }


    /**
     * 检查输入
     */
    private boolean checkInput(View view) {
        if (TextUtils.isEmpty(date)) {
            Snackbar.make(view, "选择日期", Snackbar.LENGTH_SHORT).show();
            return false;
        }
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

    @Event(R.id.input_date1)
    private void dateClick(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
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
                input_date.setText(date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private class NormalOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                type = "正常 " + String.valueOf(tv.getText());
                input_type.setText(type);
            }
        }
    }
}
