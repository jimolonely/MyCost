package com.jimo.mycost.func.time;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.jimo.mycost.MyApp;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.TimeCostRecord;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    private List<TimeCostRecord> costs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = x.view().inject(this, inflater, container);
        initData();
        return view;
    }

    private void initData() {
        //初始日期为本月
        tv_date_from.setText(JimoUtil.getFirstDayOfMonth(JimoUtil.getCurrentMonth()));
        tv_date_to.setText(JimoUtil.getDateBefore(0));
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
        Map<String, Integer> map = getGroupedData();
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            pieEntries.add(new PieEntry(e.getValue(), e.getKey(), e.getKey()));
        }
        drawPie(pieEntries, FuckUtil.getRandomColors(pieEntries.size()));
    }

    private Map<String, Integer> getGroupedData() {
        Map<String, Integer> map = new HashMap<>(5);
        for (TimeCostRecord cost : costs) {
            map.put(cost.getBigType(), map.getOrDefault(
                    cost.getBigType(), 0) + getTimeLen(cost));
        }
        return map;
    }

    /**
     * 计算时间长度
     */
    private int getTimeLen(TimeCostRecord cost) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        try {
            Date start = format.parse(cost.getStart());
            Date end = format.parse(cost.getEnd());
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
            return db.selector(TimeCostRecord.class).where("start", ">=", dateFrom)
                    .and("end", "<=", dateTo).findAll();
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
}
