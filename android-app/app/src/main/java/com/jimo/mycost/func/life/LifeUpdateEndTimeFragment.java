package com.jimo.mycost.func.life;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jimo.mycost.MyApp;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.LifeRecord;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_life_update)
public class LifeUpdateEndTimeFragment extends Fragment {

    private List<LifeRecord> lifeRecords;

    @ViewInject(R.id.input_name_life)
    private EditText edt_name;
    @ViewInject(R.id.tv_life_select_name)
    private TextView tv_select;
    @ViewInject(R.id.input_time_life)
    private TextView tv_date;

    @ViewInject(R.id.lv_life_record)
    private ListView lv_record;
    private LifeRecordAdapter recordAdapter;

    private LifeRecord select;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initData();
        return view;
    }

    private void initData() {
        // 默认时间为当前时间
        tv_date.setText(JimoUtil.getDateTimeNow());

        lifeRecords = new ArrayList<>();
        recordAdapter = new LifeRecordAdapter(lifeRecords, getContext());
        lv_record.setAdapter(recordAdapter);
        lv_record.setOnItemClickListener((adapterView, view, i, l) -> {
            LifeRecord r = lifeRecords.get(i);
            tv_select.setText(r.getName());
            select = r;
        });
    }

    /**
     * 模糊搜索记录
     */
    @Event(R.id.btn_search_life)
    private void clickToSearch(View view) {
        if (!FuckUtil.checkInput(getContext(), edt_name)) {
            JimoUtil.mySnackbar(view, "输入名字");
            return;
        }
        String name = edt_name.getText().toString();

        DbManager db = MyApp.dbManager;

        try {
            List<LifeRecord> records = db.selector(LifeRecord.class)
                    .where("name", "like", "%" + name + "%")
                    .orderBy("id", true)
                    .findAll();
            if (records.size() == 0) {
                JimoUtil.mySnackbar(view, "无数据");
                return;
            }
            lifeRecords.clear();
            lifeRecords.addAll(records);
            recordAdapter.notifyDataSetChanged();
        } catch (DbException e) {
            JimoUtil.mySnackbar(view, e.getMessage());
        }
    }

    @Event(R.id.btn_ok_life)
    private void clickToUpdate(View view) {
        String date = String.valueOf(tv_date.getText());
        if (select != null && !TextUtils.isEmpty(date)) {
            select.setEndTime(date);

            DbManager db = MyApp.dbManager;
            try {
                db.update(select, "end_time");
                FuckUtil.clearInput((obj) -> {
                }, tv_select);
            } catch (DbException e) {
                JimoUtil.mySnackbar(view, e.getMessage());
            }
        } else {
            JimoUtil.mySnackbar(view, "请选择");
        }
    }

    @Event(R.id.input_time_life)
    private void chooseTime(View view) {
        FuckUtil.showDateSelectDialog(getContext(), (date) -> tv_date.setText(String.valueOf(date)));
    }
}
