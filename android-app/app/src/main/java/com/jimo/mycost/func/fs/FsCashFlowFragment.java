package com.jimo.mycost.func.fs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jimo.mycost.MyApp;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.CostInComeRecord;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 现金流量表
 *
 * @author jimo
 * @date 20-2-24 上午8:27
 */
@ContentView(R.layout.fragment_cash_flow)
public class FsCashFlowFragment extends Fragment {

    @ViewInject(R.id.tv_common_start_date)
    private TextView tv_date_from;
    @ViewInject(R.id.tv_common_end_date)
    private TextView tv_date_to;
    @ViewInject(R.id.tl_cash_flow)
    private TableLayout tl_cash;


    private Map<String, MoneyPercent> lifeCost;
    private Map<String, MoneyPercent> investCost;
    private Map<String, MoneyPercent> workIncome;
    private Map<String, MoneyPercent> investIncome;
    private Map<String, MoneyPercent> investSoldIncome;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);

        initData();

        return view;
    }

    private void initData() {
        //初始日期为本月
        tv_date_from.setText(JimoUtil.getFirstDayOfMonth(JimoUtil.getCurrentMonth()));
        tv_date_to.setText(JimoUtil.getDateBefore(0));

        lifeCost = new HashMap<>(16);
        investCost = new HashMap<>(16);
        workIncome = new HashMap<>(16);
        investIncome = new HashMap<>(16);
        investSoldIncome = new HashMap<>(16);

        //设置所有的item都可伸缩扩展
        tl_cash.setStretchAllColumns(true);
    }

    /**
     * 提交
     */
    @Event(R.id.btn_common_ok)
    private void query(View view) {
        clearDataView();
        CharSequence startTime = tv_date_from.getText();
        CharSequence endTime = tv_date_to.getText();
        // 从库里查出列表

        DbManager db = MyApp.dbManager;

        double totalCost = 0d;
        double totalIncome = 0d;
        try {
            List<CostInComeRecord> records = db.selector(CostInComeRecord.class).where("c_date", ">=", startTime)
                    .and("c_date", "<=", endTime).findAll();
            for (CostInComeRecord record : records) {
                if (record.getInOut() == MyConst.COST) {
                    // 支出
                    handleCost(record);
                    totalCost += record.getMoney();
                } else {
                    handleIncome(record);
                    totalIncome += record.getMoney();
                }
            }
            // update percent
            updatePercent(lifeCost, totalCost);
            updatePercent(investCost, totalCost);
            updatePercent(workIncome, totalIncome);
            updatePercent(investIncome, totalIncome);
            updatePercent(investSoldIncome, totalIncome);
        } catch (DbException e) {
            JimoUtil.mySnackbar(view, e.getMessage());
        }

        // 构造视图
        addTableDivider(3);

        double lifeSum = addTableView(lifeCost, "日常生活支出", totalCost, R.color.danger);
        addTableView(investCost, "投资活动支出", totalCost, R.color.danger);
        addTableView(workIncome, "劳动收入", totalIncome, R.color.yellow);
        double investSum = addTableView(investIncome, "投资收入", totalIncome, R.color.green);
        double investSoldSum = addTableView(investSoldIncome, "投资卖出收入", totalIncome, R.color.green);

        addTableDivider(9);
        // 总收入
        addConcludeRow("总收入", 1, R.color.green, totalIncome);
        // 总支出
        addConcludeRow("总支出", 1, R.color.danger, totalCost);
        // 总投资收入
        double investTotal = investSoldSum + investSum;
        addConcludeRow("总投资收入", investTotal / totalIncome, R.color.green, investTotal);
        // 净流量, 如果占收入的比例越高，那说明收入越高
        addConcludeRow("净收入现金流量", (totalIncome - totalCost) / totalIncome,
                R.color.green, totalIncome - totalCost);
        // 财务自由流量净额,占总收入的比重越高，说明越财务自由
        double freeMoney = investSum - lifeSum;
        addConcludeRow("财务自由流量净额", (freeMoney) / totalIncome,
                freeMoney > 0 ? R.color.green : R.color.danger, freeMoney);
        addTableDivider(9);
    }

    private void clearDataView() {
        tl_cash.removeAllViews();
        lifeCost.clear();
        investCost.clear();
        workIncome.clear();
        investIncome.clear();
        investSoldIncome.clear();
    }

    private void updatePercent(Map<String, MoneyPercent> cost, double totalCost) {
        for (Map.Entry<String, MoneyPercent> e : cost.entrySet()) {
            e.getValue().setPercent(JimoUtil.keepPrecision(e.getValue().getMoney() / totalCost * 100) + "%");
        }
    }

    private double addTableView(Map<String, MoneyPercent> map, String title, double total, int color) {
        double sum = 0d;
        for (Map.Entry<String, MoneyPercent> e : map.entrySet()) {
            sum += e.getValue().getMoney();
            TableRow row = new TableRow(this.getContext());
            TextView tv_key = new TextView(this.getContext());
            tv_key.setText(e.getKey());
            TextView tv_money = new TextView(this.getContext());
            tv_money.setText(String.format("%s", JimoUtil.keepPrecision(e.getValue().getMoney())));
            TextView tv_percent = new TextView(this.getContext());
            tv_percent.setText(e.getValue().getPercent());
            row.addView(tv_key);
            row.addView(tv_money);
            row.addView(tv_percent);

            tl_cash.addView(row);
        }
        // 总结栏
        addConcludeRow(title, sum / total, color, sum);

        addTableDivider(3);

        return sum;
    }

    private void addConcludeRow(String title, double percent, int color, double num) {
        TextView tv_key = new TextView(this.getContext());
        tv_key.setText(title);
        tv_key.setTextColor(ContextCompat.getColor(Objects.requireNonNull(this.getContext()), color));
        tv_key.setTextSize(20);
        TextView tv_sum = new TextView(this.getContext());
        tv_sum.setText(JimoUtil.keepPrecision(num));
        TextView tv_percent = new TextView(this.getContext());
        tv_percent.setText(String.format("%s%%", JimoUtil.keepPrecision(percent * 100)));
        TableRow row = new TableRow(this.getContext());
        row.addView(tv_key);
        row.addView(tv_sum);
        row.addView(tv_percent);
        tl_cash.addView(row);
    }

    private void addTableDivider(int height) {
        // 增加一根分割线
        TextView tv_divider = new TextView(this.getContext());
        tv_divider.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, height
        ));
        tv_divider.setBackgroundColor(ContextCompat.getColor(
                Objects.requireNonNull(this.getContext()), R.color.divider));
        tl_cash.addView(tv_divider);
    }

    private void handleIncome(CostInComeRecord record) {
        String type = record.getTypeName();
        if (type.startsWith("劳动收入")) {
            putToWorkIncome(record, type.substring(5));
        } else if (type.startsWith("投资经常收入")) {
            putToInvestIncome(record, type.substring(5));
        } else if (type.startsWith("投资卖出收入")) {
            putToInvestSoldIncome(record, type.substring(7));
        } else {
            putToWorkIncome(record, "未知其他");
        }
    }

    // 遍历列表取相应的数据，组合成map
    private void handleCost(CostInComeRecord record) {
        String type = record.getTypeName();
        if (type.startsWith("餐饮")) {
            putToLifeCost(record, "餐饮");
        } else if (type.startsWith("交通")) {
            putToLifeCost(record, "交通");
        } else if (type.equals("生活 住房")) {
            putToLifeCost(record, "住房");
        } else if (type.equals("生活 服饰")) {
            putToLifeCost(record, "服饰");
        } else if (type.startsWith("教育")) {
            putToLifeCost(record, "教育费");
        } else if (type.equals("生活 娱乐")) {
            putToLifeCost(record, "休闲娱乐");
        } else if (type.equals("生活 旅游")) {
            putToLifeCost(record, "旅游");
        } else if (type.equals("生活 保险")) {
            putToLifeCost(record, "保险费");
        } else if (type.equals("生活 医疗")) {
            putToLifeCost(record, "医疗费");
        } else if (type.equals("投资 REITs")) {
            putToInvestCost(record, "买入REITs");
        } else if (type.equals("投资 逆回购")) {
            putToInvestCost(record, "买入逆回购");
        } else if (type.equals("投资 债券")) {
            putToInvestCost(record, "买入债券");
        } else if (type.equals("投资 货币基金")) {
            putToInvestCost(record, "买入货币基金");
        } else if (type.equals("投资 股票")) {
            putToInvestCost(record, "买入股票");
        } else if (type.equals("投资 基金")) {
            putToInvestCost(record, "买入基金");
        } else {
            putToLifeCost(record, "其他生活支出");
        }
    }

    private void putToWorkIncome(CostInComeRecord record, String key) {
        workIncome.put(key, setMoney(workIncome, key, record.getMoney()));
    }

    private void putToInvestIncome(CostInComeRecord record, String key) {
        investIncome.put(key, setMoney(investCost, key, record.getMoney()));
    }

    private void putToInvestSoldIncome(CostInComeRecord record, String key) {
        investSoldIncome.put(key, setMoney(investSoldIncome, key, record.getMoney()));
    }

    private void putToLifeCost(CostInComeRecord record, String key) {
        lifeCost.put(key, setMoney(lifeCost, key, record.getMoney()));
    }

    private void putToInvestCost(CostInComeRecord record, String key) {
        investCost.put(key, setMoney(investCost, key, record.getMoney()));
    }

    private MoneyPercent setMoney(Map<String, MoneyPercent> map, String type, Float money) {
        MoneyPercent mp = map.getOrDefault(type, new MoneyPercent(0d, "0%"));
        mp.setMoney(mp.getMoney() + money);
        return mp;
    }

    @Event(R.id.tv_common_start_date)
    private void clickToSetFromDate(View view) {
        FuckUtil.showDateSelectDialog(getContext(), obj -> tv_date_from.setText((String) obj));
    }

    @Event(R.id.tv_common_end_date)
    private void clickToSetEndDate(View view) {
        FuckUtil.showDateSelectDialog(getContext(), obj -> tv_date_to.setText((String) obj));
    }
}
