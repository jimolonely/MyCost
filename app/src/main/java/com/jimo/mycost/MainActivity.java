package com.jimo.mycost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jimo.mycost.model.CostInComeRecord;
import com.jimo.mycost.model.MonthCost;
import com.jimo.mycost.ui.AddCostActivity;
import com.jimo.mycost.util.JimoUtil;
import com.jimo.mycost.view.DayCostItem;
import com.jimo.mycost.view.DayCostItemAdapter;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.lv_cost)
    ListView listViewCost;
    @ViewInject(R.id.tv_month_total)
    TextView tv_total;
    @ViewInject(R.id.tv_month_cost)
    TextView tv_cost;
    @ViewInject(R.id.tv_month_income)
    TextView tv_income;

    private List<DayCostItem> dayCostItems;
    private DayCostItemAdapter dayCostItemAdapter;

    private float inCome = 0;
    private float cost = 0;

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
        dayCostItemAdapter = new DayCostItemAdapter(dayCostItems, this);
        listViewCost.setAdapter(dayCostItemAdapter);

        //当点击item时需跳转到新页面展示
        listViewCost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DayCostItem dayCostTitle = dayCostItems.get(i);

            }
        });
    }


    private void initRecordData() {
        dayCostItems = new ArrayList<>();
        queryData();
    }

    /**
     * 从数据库查出记录存在List里
     */
    private void queryData() {
        dayCostItems.clear();
        DbManager db = MyApp.dbManager;
        //查存每条记录
        try {
            List<CostInComeRecord> costInComeRecords =
                    db.selector(CostInComeRecord.class).orderBy("c_date", true).limit(10).findAll();
            fillTitles(costInComeRecords);
        } catch (DbException e) {
            e.printStackTrace();
        }

        //查询月记录
        int month = getMonth();
        int year = getYear();
        try {
            MonthCost monthCost = db.selector(MonthCost.class).
                    where("month", "=", month).and("year", "=", year).and("in_out", "=", MyConst.COST).
                    and("user_name", "=", MyConst.getUserName(this)).findFirst();
            MonthCost monthInCome = db.selector(MonthCost.class).
                    where("month", "=", month).and("year", "=", year).and("in_out", "=", MyConst.IN_COME).
                    and("user_name", "=", MyConst.getUserName(this)).findFirst();
            Float c = 0f;
            Float i = 0f;
            if (monthCost != null) {
                c = monthCost.getMoney();
            }
            if (monthInCome != null) {
                i = monthInCome.getMoney();
            }
            tv_cost.setText("-" + c);
            tv_income.setText("+" + i);
            tv_total.setText(month + "月: " + (i - c) + "元");
        } catch (DbException e) {
            JimoUtil.mySnackbar(tv_income, "月记录查询出错");
            e.printStackTrace();
        }
    }

    /**
     * 解析数据构造double ListView的数据
     * //TODO 待优化
     *
     * @param costInComeRecords
     */
    private void fillTitles(List<CostInComeRecord> costInComeRecords) {
        if (costInComeRecords == null) {
            return;
        }
        Set<String> dates = new HashSet<>();
        for (CostInComeRecord c : costInComeRecords) {
            if (!dates.contains(c.getDate())) {
                dates.add(c.getDate());
                dayCostItems.add(new DayCostItem(c.getDate(), MyConst.ITEM_TYPE2,
                        c.getTypeName(), String.valueOf(c.getMoney())));
                for (CostInComeRecord tc : costInComeRecords) {
                    String d = tc.getDate();
                    if (d != null && d.equals(c.getDate())) {
                        dayCostItems.add(new DayCostItem(tc.getDate(), MyConst.ITEM_TYPE1,
                                tc.getTypeName(), String.valueOf(tc.getMoney())));
                    }
                }
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


    @Override
    protected void onResume() {
        super.onResume();
        //刷新列表
        queryData();
        dayCostItemAdapter.notifyDataSetChanged();
    }

    public int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
}
