package com.jimo.mycost.func.income;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.jimo.mycost.MyApp;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.CostInComeRecord;
import com.jimo.mycost.data.model.MonthCost;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.jimo.mycost.util.JimoUtil.getMonth;
import static com.jimo.mycost.util.JimoUtil.getYear;

/**
 * Created by root on 17-7-19.
 * 收入
 */
@ContentView(R.layout.fragment_income)
public class InComeAddFragment extends Fragment {

    @ViewInject(R.id.fbl_normal_income)
    FlexboxLayout fl_normal;

    @ViewInject(R.id.input_date_income)
    TextView tv_input_date;

    @ViewInject(R.id.input_type_income)
    TextView tv_input_type;

    @ViewInject(R.id.input_money_income)
    EditText tv_input_money;

    @ViewInject(R.id.input_remark_income)
    EditText edt_input_remark;

    private List<String> normalInComes = new ArrayList<>(Arrays.asList("工资", "兼职", "红包", "投资", "奖金", "补贴", "礼金", "其他"));

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
        registClickListener(normalInComes, "正常", fl_normal);
    }

    private void registClickListener(List<String> title, String type, FlexboxLayout flex) {
        for (String s : title) {
            TextView tvv = getTextView(s, (view) -> {
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    final String text = type + " " + String.valueOf(tv.getText());
                    tv_input_type.setText(text);
                }
            });
            flex.addView(tvv);
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
    @Event(R.id.btn_finish_income)
    private void finishClick(View view) {
        if (FuckUtil.checkInput(getContext(), tv_input_date, tv_input_money, tv_input_type)) {
            final float money = Float.parseFloat(String.valueOf(tv_input_money.getText()));
            final String remark = String.valueOf(edt_input_remark.getText());
            final String date = String.valueOf(tv_input_date.getText());
            DbManager db = MyApp.dbManager;

            switch (modifyType) {
                case MyConst.SYNC_TYPE_INSERT:
                    String userName = MyConst.getUserName(getContext());
                    CostInComeRecord cost = new CostInComeRecord(MyConst.IN_COME, money,
                            remark, date, String.valueOf(tv_input_type.getText()), userName, MyConst.SYNC_TYPE_INSERT);
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
                        FuckUtil.clearInput((obj) -> {
                        }, tv_input_type, tv_input_money, tv_input_date, edt_input_remark);
                        JimoUtil.mySnackbar(view, "保存成功");
                    } catch (DbException e) {
                        e.printStackTrace();
                        JimoUtil.mySnackbar(view, "error: " + e.getMessage());
                    }
            }
        }
    }

    @Event(R.id.input_date_income)
    private void dateClick(View view) {
        FuckUtil.showDateSelectDialog(getContext(), (date) -> tv_input_date.setText(String.valueOf(date)));
    }

}
