package com.jimo.mycost.func.time;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.jimo.mycost.MyApp;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.BigSmallType;
import com.jimo.mycost.data.model.TimeCostRecord;
import com.jimo.mycost.func.cost.AddBigSmallTypeDialog;
import com.jimo.mycost.util.CreateTypeList;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * time add
 */
@ContentView(R.layout.fragment_time_add)
public class TimeAddFragment extends Fragment {

    @ViewInject(R.id.ll_time_type)
    private LinearLayout ll_types;
    @ViewInject(R.id.tv_time_type)
    private TextView tv_time_type;
    @ViewInject(R.id.tv_time_begin)
    private TextView tv_time_begin;
    @ViewInject(R.id.lv_time)
    private ListView lv_time;

    private CreateTypeList createTypeList;
    private RunningTaskItemAdapter runningTaskItemAdapter;
    private List<TimeCostRecord> timeCostRecords;
    private String[] bigTypes = {"娱乐", "休息", "强迫工作", "高效工作", "拖延"};
    // 类别对应的item展示颜色
    private Map<String, Integer> colorMap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = x.view().inject(this, inflater, container);

        initViews();

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        createTypeList = new CreateTypeList(ll_types, getContext(), (
                (bigType, smallType) -> tv_time_type.setText(bigType + " " + smallType)),
                BigSmallType.TYPE_TIME
        );
        createTypeList.setTypes();

        colorMap = getColorMap();

        getTimeCostRecords();
        runningTaskItemAdapter = new RunningTaskItemAdapter(timeCostRecords, getContext(), colorMap);
        lv_time.setAdapter(runningTaskItemAdapter);
    }

    private Map<String, Integer> getColorMap() {
        Map<String, Integer> map = new HashMap<>(bigTypes.length);
        // 以后可能从数据库获取 TODO
        map.put(bigTypes[0], Color.rgb(146, 222, 233));
        map.put(bigTypes[1], Color.rgb(80, 238, 107));
        map.put(bigTypes[2], Color.rgb(254, 174, 102));
        map.put(bigTypes[3], Color.rgb(255, 255, 102));
        map.put(bigTypes[4], Color.rgb(245, 78, 37));
        return map;
    }

    private void getTimeCostRecords() {
        DbManager db = MyApp.dbManager;
        try {
            timeCostRecords = db.selector(TimeCostRecord.class).where("day", "=",
                    JimoUtil.getDateBefore(0)).findAll();
        } catch (DbException e) {
            JimoUtil.mySnackbar(tv_time_begin, "查询进行中任务失败：" + e.getMessage());
            e.printStackTrace();
            timeCostRecords = new ArrayList<>();
        }
    }

    private void refreshRunningTask() {
        getTimeCostRecords();
        runningTaskItemAdapter.setItems(timeCostRecords);
        runningTaskItemAdapter.notifyDataSetChanged();
    }

    /**
     * 开始一个项目
     */
    @Event(R.id.tv_time_begin)
    private void startTime(View view) {
        String type = tv_time_type.getText().toString();
        if (TextUtils.isEmpty(type)) {
            JimoUtil.mySnackbar(view, "请选择");
            return;
        }
        // begin
        String start = JimoUtil.getDateTimeNow();
        String day = JimoUtil.getDateBefore(0);
        // 默认把一件事的结束时间设为一天的结束
        String end = JimoUtil.getDateBefore(1) + " 00:00:00";
        String[] s = type.split(" ");
        String bigType = s[0];
        String smallType = s[1];
        // save to db
        saveToDb(start, day, end, bigType, smallType);
    }

    private void saveToDb(String start, String day, String end, String bigType, String smallType) {
        DbManager db = MyApp.dbManager;
        TimeCostRecord record = new TimeCostRecord(start, end, day, bigType, smallType);
        try {
            db.save(record);
            JimoUtil.mySnackbar(tv_time_begin, "保存成功");
            refreshRunningTask();
        } catch (DbException e) {
            JimoUtil.mySnackbar(tv_time_begin, "保存失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Event(R.id.btn_time_add_type)
    private void addType(View view) {
        // 弹框添加类别
        AddBigSmallTypeDialog dialog = new AddBigSmallTypeDialog();
        dialog.show(Objects.requireNonNull(getActivity()).getFragmentManager(),
                bigTypes, (b, s) -> createTypeList.saveSmallType(b, s));
    }
}
