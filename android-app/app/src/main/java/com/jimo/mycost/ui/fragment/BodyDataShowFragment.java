package com.jimo.mycost.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.jimo.mycost.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimo on 18-2-24.
 * 展示身体数据
 */
@ContentView(R.layout.fragment_body_data_show)
public class BodyDataShowFragment extends Fragment {

    @ViewInject(R.id.chart)
    private LineChart chart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initData();
        return view;
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
