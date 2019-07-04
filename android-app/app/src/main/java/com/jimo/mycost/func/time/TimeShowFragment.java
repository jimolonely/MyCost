package com.jimo.mycost.func.time;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jimo.mycost.MyApp;
import com.jimo.mycost.R;
import com.jimo.mycost.data.dto.BarEntryColorList;
import com.jimo.mycost.data.dto.BarEntryWithColor;
import com.jimo.mycost.data.model.TimeCostRecord;
import com.jimo.mycost.func.cost.CostGraphFragment;
import com.jimo.mycost.func.cost.CostShowDialog;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;
import com.jimo.mycost.view.AutoLineBreakLayout;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *
 */
@ContentView(R.layout.fragment_time_show)
public class TimeShowFragment extends Fragment {

    @ViewInject(R.id.tv_common_start_date)
    private TextView tv_date_from;
    @ViewInject(R.id.tv_common_end_date)
    private TextView tv_date_to;
    @ViewInject(R.id.pie_time_type)
    private PieChart pie_type;
    @ViewInject(R.id.bar_time_type)
    private BarChart bar_type;
    @ViewInject(R.id.ll_check_type)
    private AutoLineBreakLayout ll_check_type;
    /**
     * 选中要显示的类型
     */
    private Set<String> checkedTypes;
    private List<TimeCostRecord> costs;
    private Map<String, Integer> typeMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = x.view().inject(this, inflater, container);
        initData();
        return view;
    }

    private void initData() {
        checkedTypes = new HashSet<>();
        typeMap = new HashMap<>(5);
        //初始日期为本月
        tv_date_from.setText(JimoUtil.getFirstDayOfMonth(JimoUtil.getCurrentMonth()));
        tv_date_to.setText(JimoUtil.getDateBefore(0));

        pie_type.setOnChartValueSelectedListener(new MyChartValueSelectedListener());
    }

    @Event(R.id.btn_common_ok)
    private void clickToSearch(View view) {
        String dateFrom = tv_date_from.getText().toString();
        String dateTo = tv_date_to.getText().toString();
        if (TextUtils.isEmpty(dateFrom) || TextUtils.isEmpty(dateTo)) {
            JimoUtil.mySnackbar(view, "please pick the date!");
            return;
        }
        costs = loadData(dateFrom, dateTo);
//        setTotalMoney(costs);
        setGraph(true);
    }

    private void setGraph(boolean first) {
        List<PieEntry> pieEntries = new ArrayList<>();
        Map<String, Integer> map = getGroupedData(first);
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            pieEntries.add(new PieEntry(e.getValue(), e.getKey(), e.getKey()));
        }
        drawPie(pieEntries, FuckUtil.getRandomColors(pieEntries.size()));
    }

    private Map<String, Integer> getGroupedData(boolean first) {
        if (first) {
            int totalLen = getTimeLen(
                    tv_date_from.getText() + " 00:00:00", tv_date_to.getText() + " 00:00:00");
            for (TimeCostRecord cost : costs) {
                int timeLen = getTimeLen(cost.getStart(), cost.getEnd());
                typeMap.put(cost.getBigType(), typeMap.getOrDefault(cost.getBigType(), 0)
                        + timeLen);
                totalLen -= timeLen;
            }
            typeMap.put("未知", totalLen);
            /*costs.add(new TimeCostRecord(tv_date_from.getText() + " 00:00:00",
                    tv_date_to.getText() + " 00:00:00", tv_date_to.getText().toString(),
                    "未知", "未知", ""));*/
            setTypeCheckboxes(typeMap.keySet());
            return typeMap;
        } else {
            Map<String, Integer> map = new HashMap<>(5);
            for (Map.Entry<String, Integer> e : typeMap.entrySet()) {
                if (checkedTypes.contains(e.getKey())) {
                    map.put(e.getKey(), e.getValue());
                }
            }
            return map;
        }
    }

    /**
     * 循环生成chengbox控件，用于选择是否显示cost type
     */
    private void setTypeCheckboxes(Set<String> types) {
        ll_check_type.removeAllViews();
        for (String type : types) {
            CheckBox c = new CheckBox(getActivity());
            c.setText(type);
            c.setChecked(true);
            checkedTypes.add(type);
            c.setOnCheckedChangeListener((compoundButton, checked) -> {
                if (checked) {
                    checkedTypes.add(compoundButton.getText().toString());
                } else {
                    checkedTypes.remove(compoundButton.getText().toString());
                }
                setGraph(false);
            });
            ll_check_type.addView(c);
        }
    }

    /**
     * 计算时间长度
     */
    private int getTimeLen(String startTime, String endTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        try {
            Date start = format.parse(startTime);
            Date end = format.parse(endTime);
            // 转成分钟
            return (int) ((end.getTime() - start.getTime()) / 1000 / 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void drawPie(List<PieEntry> pieEntries, int[] colors) {
        PieDataSet dataSet = new PieDataSet(pieEntries, "time type");
        dataSet.setColors(colors);
        PieData pieData = new PieData(dataSet);
        pie_type.setData(pieData);
        // 实心圆
        pie_type.setHoleRadius(0);
        Legend legend = pie_type.getLegend();
        legend.setXEntrySpace(2f);
        legend.setYEntrySpace(2f);

        Description description = new Description();
        description.setText("time pie");
        pie_type.setDescription(description);

        pie_type.invalidate();
    }

    private List<TimeCostRecord> loadData(String dateFrom, String dateTo) {
        DbManager db = MyApp.dbManager;
        try {
            List<TimeCostRecord> re = db.selector(TimeCostRecord.class).where("start", ">=", dateFrom)
                    .and("end", "<=", dateTo + " 23:59:59").findAll();
            /*int dist = JimoUtil.getDistOfDate(dateFrom, dateTo);
            for (int i = 0; i < dist; i++) {
                re.add(new TimeCostRecord(JimoUtil.getDateBefore(i + 1, dateTo) + " 23:00:00",
                        JimoUtil.getDateBefore(i, dateTo) + " 06:20:00",
                        JimoUtil.getDateBefore(i, dateTo), "休息", "睡觉", ""));
            }*/
            return re;
        } catch (DbException e) {
            e.printStackTrace();
            JimoUtil.myToast(getContext(), "load time error：" + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Event(R.id.tv_common_start_date)
    private void clickToSetFromDate(View view) {
        FuckUtil.showDateSelectDialog(getContext(), obj -> tv_date_from.setText((String) obj));
    }

    @Event(R.id.tv_common_end_date)
    private void clickToSetEndDate(View view) {
        FuckUtil.showDateSelectDialog(getContext(), obj -> tv_date_to.setText((String) obj));
    }

    private class MyChartValueSelectedListener implements OnChartValueSelectedListener {

        @Override
        public void onValueSelected(Entry e, Highlight h) {
            String type = (String) e.getData();
            if ("未知".equals(type)) {
                return;
            }
            // 加载柱状图展示小类别
            Map<String, Integer> map = new HashMap<>(8);
            for (TimeCostRecord cost : costs) {
                if (type.equals(cost.getBigType())) {
                    map.put(cost.getSmallType(), map.getOrDefault(cost.getSmallType(), 0)
                            + getTimeLen(cost.getStart(), cost.getEnd()));
                }
            }
            List<BarEntryWithColor> barEntryWithColors = new ArrayList<>();
            int i = 0;
            int[] randomColors = FuckUtil.getRandomColors(map.size());
            for (Map.Entry<String, Integer> c : map.entrySet()) {
                barEntryWithColors.add(new BarEntryWithColor(
                        new BarEntry(i, c.getValue(), c.getKey()), randomColors[i++], c.getKey()));
            }
            BarEntryColorList barEntryColorList = new BarEntryColorList(barEntryWithColors);
            drawBar(barEntryColorList);
        }

        @Override
        public void onNothingSelected() {
        }
    }

    private void drawBar(BarEntryColorList barEntryColorList) {
        List<BarEntry> barEntries = barEntryColorList.getBarEntries();
        int[] colors = barEntryColorList.getColors();
        List<String> xVals = barEntryColorList.getxVals();
        BarDataSet dataSet = new BarDataSet(barEntries, "time type");
        dataSet.setColors(colors);
        BarData data = new BarData(dataSet);
        bar_type.setData(data);
        bar_type.setFitBars(true);

        // x val
        XAxis xAxis = bar_type.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {

            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int i = (int) value;
                return i >= xVals.size() ? "未知" : xVals.get(i);
            }
        });
        Description description = new Description();
        description.setText("time bar");
        bar_type.setDescription(description);
        bar_type.invalidate();
    }
}
