package com.jimo.mycost.func.reflect;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jimo.mycost.MyApp;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.BigSmallType;
import com.jimo.mycost.data.model.ReflectionRecord;
import com.jimo.mycost.func.cost.AddBigSmallTypeDialog;
import com.jimo.mycost.util.CreateTypeList;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * time add
 */
@ContentView(R.layout.fragment_reflect_add)
public class ReflectAddFragment extends Fragment {

    @ViewInject(R.id.tv_reflect_type)
    private TextView tv_reflect_type;
    @ViewInject(R.id.tv_reflect_day)
    private TextView tv_reflect_day;
    @ViewInject(R.id.edt_reflect_content)
    private EditText edt_reflect_content;
    @ViewInject(R.id.edt_reflect_address)
    private EditText edt_reflect_address;
    @ViewInject(R.id.edt_reflect_people)
    private EditText edt_reflect_people;
    @ViewInject(R.id.ll_reflect_type)
    private LinearLayout ll_types;

    private String[] bigTypes = {"收获", "后悔", "感触", "情感"};
    // 类别对应的item展示颜色
    private Map<String, Integer> colorMap;
    private CreateTypeList createTypeList;

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
                (bigType, smallType) -> tv_reflect_type.setText(bigType + " " + smallType)),
                BigSmallType.TYPE_REFLECT
        );
        createTypeList.setTypes();

        colorMap = getColorMap();
    }

    private Map<String, Integer> getColorMap() {
        Map<String, Integer> map = new HashMap<>(bigTypes.length);
        // 以后可能从数据库获取 TODO
        map.put(bigTypes[0], Color.rgb(146, 222, 233));
        map.put(bigTypes[1], Color.rgb(80, 238, 107));
        map.put(bigTypes[2], Color.rgb(254, 174, 102));
        map.put(bigTypes[3], Color.rgb(255, 255, 102));
        return map;
    }

    @Event(R.id.btn_reflect_add)
    private void submit(View view) {
        String type = tv_reflect_type.getText().toString();
        String content = edt_reflect_content.getText().toString();
        String address = edt_reflect_address.getText().toString();
        String people = edt_reflect_people.getText().toString();
        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(content)) {
            JimoUtil.mySnackbar(view, "请选择");
            return;
        }
        String day = tv_reflect_day.getText().toString();
        if (TextUtils.isEmpty(day)) {
            day = JimoUtil.getDateBefore(0);
        }
        String[] s = type.split(" ");
        String bigType = s[0];
        String smallType = s[1];
        // save to db
        saveToDb(people, day, address, bigType, smallType, content);
    }

    private void saveToDb(String people, String day, String address,
                          String bigType, String smallType, String content) {
        DbManager db = MyApp.dbManager;
        ReflectionRecord record =
                new ReflectionRecord(bigType, smallType, day, content, people, address);
        try {
            db.save(record);
            JimoUtil.mySnackbar(tv_reflect_type, "保存成功");
            FuckUtil.clearInput(d -> {
                    }, tv_reflect_type, tv_reflect_day, edt_reflect_address,
                    edt_reflect_content, edt_reflect_people);
        } catch (DbException e) {
            JimoUtil.mySnackbar(tv_reflect_type, "保存失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Event(R.id.btn_reflect_add_type)
    private void addType(View view) {
        // 弹框添加类别
        AddBigSmallTypeDialog dialog = new AddBigSmallTypeDialog();
        dialog.show(Objects.requireNonNull(getActivity()).getFragmentManager(),
                bigTypes, (b, s) -> createTypeList.saveSmallType(b, s));
    }

    @Event(R.id.tv_reflect_day)
    private void selectDay(View view) {
        FuckUtil.showDateSelectDialog(getContext(), d -> tv_reflect_day.setText(d.toString()));
    }
}
