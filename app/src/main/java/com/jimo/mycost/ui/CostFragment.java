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
 * 支出
 */
@ContentView(R.layout.cost_fragment)
public class CostFragment extends Fragment {

    @ViewInject(R.id.fbl_food)
    FlexboxLayout fl_food;

    @ViewInject(R.id.fbl_transport)
    FlexboxLayout fl_transport;

    @ViewInject(R.id.fbl_study)
    FlexboxLayout fl_study;

    @ViewInject(R.id.fbl_life)
    FlexboxLayout fl_life;

    @ViewInject(R.id.input_date)
    TextView input_date;

    @ViewInject(R.id.input_type)
    TextView input_type;

    @ViewInject(R.id.input_money)
    EditText input_money;

    @ViewInject(R.id.input_remark)
    EditText input_remark;//备注

    private List<String> foodTitles = new ArrayList<>(Arrays.asList("早餐", "午餐", "晚餐", "零食", "其他"));
    private List<String> transportTitles = new ArrayList<>(Arrays.asList(
            "地铁", "公交", "共享单车", "滴滴", "的士", "火车", "飞机", "其他"));
    private List<String> studyTitles = new ArrayList<>(Arrays.asList("买书", "文具", "付费课程", "其他"));
    private List<String> lifeTitles = new ArrayList<>(Arrays.asList("健康",
            "服饰", "居家", "娱乐", "人情", "旅游", "通讯", "其他"));
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
        //food
        for (String s : foodTitles) {
            TextView tv = getTextView(s, new FoodOnClickListener());
            fl_food.addView(tv);
        }

        //transport
        for (String s : transportTitles) {
            fl_transport.addView(getTextView(s, new TransportClickListener()));
        }

        //study
        for (String s : studyTitles) {
            fl_study.addView(getTextView(s, new StudyClickListener()));
        }

        //life
        for (String s : lifeTitles) {
            fl_life.addView(getTextView(s, new LifeClickListener()));
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
    @Event(R.id.btn_finish)
    private void finishClick(View view) {
        if (checkInput(view)) {
            try {
                //保留2位小数
                money = Math.round(Float.parseFloat(String.valueOf(input_money.getText())) * 100) / 100;
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
                CostInComeRecord cost = new CostInComeRecord(MyConst.COST, money,
                        remark, date, type, userName, MyConst.SYNC_TYPE_INSERT);
                //TODO 事务
                try {
                    db.save(cost);

                    int month = getMonth(date);
                    int year = getYear(date);
                    MonthCost monthCost = db.selector(MonthCost.class).
                            where("month", "=", month).and("year", "=", year).
                            and("user_name", "=", userName).and("in_out", "=", MyConst.COST).findFirst();
                    if (monthCost == null) {
                        monthCost = new MonthCost(year, month, money, MyConst.COST, MyConst.SYNC_TYPE_INSERT, userName);
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

    @Event(R.id.input_date)
    private void dateClick(View view) {
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
                type = "餐饮 " + String.valueOf(tv.getText());
                input_type.setText(type);
            }
        }
    }

    private class TransportClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                type = "交通 " + String.valueOf(tv.getText());
                input_type.setText(type);
            }
        }
    }

    private class StudyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                type = "学习 " + String.valueOf(tv.getText());
                input_type.setText(type);
            }
        }
    }

    private class LifeClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                type = "生活 " + String.valueOf(tv.getText());
                input_type.setText(type);
            }
        }
    }

    //在从主页面点击一条数据进来时确定是修改

}
