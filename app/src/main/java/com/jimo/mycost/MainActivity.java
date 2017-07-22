package com.jimo.mycost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.jimo.mycost.model.CostInComeRecord;
import com.jimo.mycost.ui.AddCostActivity;
import com.jimo.mycost.view.CostListView;
import com.jimo.mycost.view.DayCostItem;
import com.jimo.mycost.view.DayCostItemAdapter;
import com.jimo.mycost.view.DayCostTitle;
import com.jimo.mycost.view.DayCostTitleAdapter;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.ib_add)
    ImageButton ib_add;
    @ViewInject(R.id.clv_title)
    CostListView costListView;

    private List<DayCostTitle> dayCostTitles;
    private DayCostTitleAdapter dayCostTitleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        initRecordData();

        initViews();
    }

    /**
     * 更新ListView
     */
    private void initViews() {
        dayCostTitleAdapter = new DayCostTitleAdapter(dayCostTitles, this);
        costListView.setAdapter(dayCostTitleAdapter);

        //当点击item时需跳转到新页面展示
        costListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DayCostTitle dayCostTitle = dayCostTitles.get(i);

            }
        });
    }

    /**
     * 从数据库查出记录存在List里
     */
    private void initRecordData() {
        dayCostTitles = new ArrayList<>();

        //TODO 查询数据
        DbManager db = MyApp.dbManager;
        try {
            List<CostInComeRecord> costInComeRecords =
                    db.selector(CostInComeRecord.class).orderBy("id", true).limit(10).findAll();
            fillTitles(costInComeRecords);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析数据构造double ListView的数据
     * //TODO 待优化
     * @param costInComeRecords
     */
    private void fillTitles(List<CostInComeRecord> costInComeRecords) {
        Set<String> dates = new HashSet<>();
        for (CostInComeRecord c : costInComeRecords) {
            if (!dates.contains(c.getDate())) {
                dates.add(c.getDate());
                List<DayCostItem> items = new ArrayList<>();
                for (CostInComeRecord tc : costInComeRecords) {
                    if (tc.getDate().equals(c.getDate())) {
                        items.add(new DayCostItem(tc.getTypeName(), String.valueOf(tc.getMoney())));
                    }
                }
                dayCostTitles.add(new DayCostTitle(c.getDate(), items));
            }
        }
    }

    /**
     * 点击跳转页面
     */
    @Event(R.id.ib_add)
    private void ibAddClick(View v) {
        Intent intent = new Intent(this, AddCostActivity.class);
        startActivity(intent);
    }


}
