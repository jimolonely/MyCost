package com.jimo.mycost.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.jimo.mycost.MyApp;
import com.jimo.mycost.R;
import com.jimo.mycost.model.BodyData;
import com.jimo.mycost.model.RangeDate;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.jimo.mycost.MyConst.bodyData;

/**
 * Created by jimo on 18-2-24.
 * 展示身体数据
 */
@ContentView(R.layout.fragment_body_data_show)
public class BodyDataShowFragment extends Fragment {

    @ViewInject(R.id.chart)
    private LineChart chart;
    @ViewInject(R.id.sp_project)
    private Spinner sp_project;
    @ViewInject(R.id.sp_freq)
    private Spinner sp_freq;

    private ArrayAdapter<String> adapterProject;
    private ArrayAdapter<String> adapterFreq;

    private String project;//选择的项目
    private String freq;//选择的频率
    private int currCount;//当前选中的频率下的时间,比如如果是月.则记录月份
    private RangeDate rangeDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initViews();
        initData();
        return view;
    }

    private void initViews() {
        final String[] frequency = {"周", "月", "年"};
        adapterProject = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, bodyData);
        adapterFreq = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, frequency);
        sp_project.setAdapter(adapterProject);
        sp_freq.setAdapter(adapterFreq);
        sp_project.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //如果项目改变了需要把当前计数置为0
                currCount = bodyData[position].equals(project) ? currCount : 0;
                project = bodyData[position];
                reloadChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_freq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                freq = frequency[position];
                reloadChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 当选择变化时重新加载图表
     * 步骤分为:
     * 2. 去数据库查数据
     * 3. 绘图
     */
    private void reloadChart() {
        //2.
        loadDataFromDB();
        //3.
        drawChart();
    }

    private void drawChart() {
        //TODO
    }

    private void loadDataFromDB() {
        final DbManager dbManager = MyApp.dbManager;
        try {
            final List<BodyData> bodyData = dbManager.selector(BodyData.class).
                    where("body_part", "=", project).and("date", ">=", rangeDate.getBeginDate()).
                    and("date", "<=", rangeDate.getEndDate()).findAll();

        } catch (DbException e) {
            JimoUtil.mySnackbar(sp_freq, "加载身体数据出错");
            e.printStackTrace();
        }
    }

    /**
     * 需要根据周期确定currDate
     * 1. 如果currDate被更新过(上一个或下一个),则无需更新
     * 2. 否则确定当前currDate
     */
    private void updateChoice(int offset) {
        rangeDate = JimoUtil.getChoiceDateRange(freq, currCount, offset);
    }

    /**
     * 显示下一周期
     * 重置日期
     * 重新渲染
     *
     * @param view
     */
    @Event(R.id.ib_next)
    private void ibNextClick(View view) {
        updateChoice(1);
        reloadChart();
    }

    /**
     * 显示上一周期
     *
     * @param view
     */
    @Event(R.id.ib_last)
    private void ibLastClick(View view) {
        updateChoice(-1);
        reloadChart();
    }

    private void initData() {
        List<Entry> entries = new ArrayList<>();
        final String[] x = new String[30];
        for (int i = 0; i < 30; i++) {
            entries.add(new Entry(i, i));
            x[i] = i + "个";
        }
        final LineDataSet lineDataSet = new LineDataSet(entries, "3月");
//        lineDataSet.setColor();
        final LineData data = new LineData(lineDataSet);
        chart.setData(data);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return x[(int) value];
            }
        };
        final XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        chart.invalidate();
    }

}
