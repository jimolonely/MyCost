package com.jimo.mycost.func.cost;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.jimo.mycost.MyApp;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.CostInComeRecord;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;
import com.jimo.mycost.view.AutoLineBreakLayout;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 图表分析开销
 *
 * @author jimo
 * @date 18-10-21 下午3:16
 */
@ContentView(R.layout.fragment_cost_graph)
public class CostGraphFragment extends Fragment {

    @ViewInject(R.id.tv_cost_total)
    private TextView tv_cost_total;
    @ViewInject(R.id.tv_common_start_date)
    private TextView tv_date_from;
    @ViewInject(R.id.tv_common_end_date)
    private TextView tv_date_to;
    @ViewInject(R.id.pie_cost_type)
    private PieChart pie_type;
    @ViewInject(R.id.bar_cost_type)
    private BarChart bar_type;
    @ViewInject(R.id.ll_check_type)
    private AutoLineBreakLayout ll_check_type;
    /**
     * 选中要显示的类型
     */
    private Set<String> checkedTypes;

    List<CostInComeRecord> costs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initViews();
        return view;
    }

    private void initViews() {
        checkedTypes = new HashSet<>();

        //初始日期为本月
        tv_date_from.setText(JimoUtil.getFirstDayOfMonth(JimoUtil.getCurrentMonth()));
        tv_date_to.setText(JimoUtil.getDateBefore(0));
    }

    /**
     * 根据起止日期开始查询数据
     *
     * @author jimo
     * @date 18-10-21 下午4:37
     */
    @Event(R.id.btn_common_ok)
    private void clickToSearch(View view) {
        String dateFrom = tv_date_from.getText().toString();
        String dateTo = tv_date_to.getText().toString();
        if (TextUtils.isEmpty(dateFrom) || TextUtils.isEmpty(dateTo)) {
            JimoUtil.mySnackbar(view, "please pick the date!");
            return;
        }
        costs = loadCostData(dateFrom, dateTo);
        setTotalMoney(costs);
        setGraph(true);
    }

    private void setGraph(boolean first) {
        drawPie(loadPieData(costs, first));
        drawBar(loadBarData(costs, first));
    }

    /**
     * 计算出总共的钱
     *
     * @author jimo
     * @date 18-11-3 下午10:22
     */
    private void setTotalMoney(List<CostInComeRecord> costs) {
        float total = 0;
        for (CostInComeRecord cost : costs) {
            total += cost.getMoney();
        }
        tv_cost_total.setText("总支出： " + total + "元");
    }

    private void drawBar(List<BarEntry> barEntries) {
        BarDataSet dataSet = new BarDataSet(barEntries, "cost type");
        dataSet.setColors(FuckUtil.getRandomColors(barEntries.size()));
        BarData data = new BarData(dataSet);
        bar_type.setData(data);
        bar_type.setFitBars(true);
        bar_type.invalidate();
    }

    private void drawPie(List<PieEntry> pieEntries) {
        PieDataSet dataSet = new PieDataSet(pieEntries, "cost type");
        dataSet.setColors(FuckUtil.getRandomColors(pieEntries.size()));
        PieData pieData = new PieData(dataSet);
        pie_type.setData(pieData);
        // 实心圆
        pie_type.setHoleRadius(0);
        Legend legend = pie_type.getLegend();
        legend.setXEntrySpace(2f);
        legend.setYEntrySpace(2f);

        Description description = new Description();
        description.setText("cost");
        pie_type.setDescription(description);

        pie_type.invalidate();
    }

    private List<BarEntry> loadBarData(List<CostInComeRecord> costs, boolean first) {
        List<BarEntry> barEntries = new ArrayList<>();
        Map<String, Float> map = getGroupedCostData(costs, first);
        int i = 0;
        for (Map.Entry<String, Float> c : map.entrySet()) {
            barEntries.add(new BarEntry(i++, c.getValue(), c.getKey()));
        }
        return barEntries;
    }

    /**
     * 循环生成chengbox控件，用于选择是否显示cost type
     */
    private void setTypeCheckboxes(Set<String> types) {
        ll_check_type.removeAllViews();
        for (String type : types) {
            CheckBox c = new CheckBox(getActivity());
            c.setText(type);
            c.setOnCheckedChangeListener((compoundButton, checked) -> {
                if (checked) {
                    checkedTypes.add(compoundButton.getText().toString());
                } else {
                    checkedTypes.remove(compoundButton.getText().toString());
                }
                setGraph(false);
            });
            c.setChecked(true);
            ll_check_type.addView(c);
        }
    }

    /**
     * 从数据库加载数据
     *
     * @author jimo
     * @date 18-10-21 下午4:58
     */
    private List<CostInComeRecord> loadCostData(String dateFrom, String dateTo) {
        DbManager db = MyApp.dbManager;

        try {
            return db.selector(CostInComeRecord.class).where("in_out", "=", MyConst.COST)
                    .and("c_date", ">=", dateFrom)
                    .and("c_date", "<=", dateTo)
                    .findAll();
        } catch (DbException e) {
            JimoUtil.mySnackbar(tv_date_from, "load cost data error: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * 构造pie chart的数据
     *
     * @author jimo
     * @date 18-10-21 下午6:13
     */
    private List<PieEntry> loadPieData(List<CostInComeRecord> costs, boolean first) {
        List<PieEntry> pieEntries = new ArrayList<>();
        Map<String, Float> map = getGroupedCostData(costs, first);
        for (Map.Entry<String, Float> c : map.entrySet()) {
            pieEntries.add(new PieEntry(c.getValue(), c.getKey()));
        }
        return pieEntries;
    }

    /**
     * 如果是第一次，就选中所有，否则根据选中的构造
     */
    @NonNull
    private Map<String, Float> getGroupedCostData(List<CostInComeRecord> costs, boolean first) {
        Map<String, Float> map = new HashMap<>();
        if (first) {
            for (CostInComeRecord cost : costs) {
                map.put(cost.getTypeName(), map.getOrDefault(cost.getTypeName(), 0.0f) + cost.getMoney());
            }
            setTypeCheckboxes(map.keySet());
        } else {
            for (CostInComeRecord cost : costs) {
                if (checkedTypes.contains(cost.getTypeName())) {
                    map.put(cost.getTypeName(), map.getOrDefault(cost.getTypeName(), 0.0f) + cost.getMoney());
                }
            }
        }
        return map;
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
