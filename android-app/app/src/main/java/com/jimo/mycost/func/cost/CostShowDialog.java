package com.jimo.mycost.func.cost;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jimo.mycost.R;
import com.jimo.mycost.data.model.CostInComeRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 显示一段时间内的开销数据列表
 */
public class CostShowDialog extends Dialog {

    private CostDayItemAdapter costDayItemAdapter;
    private int total;

    public CostShowDialog(@NonNull Context context, List<CostInComeRecord> costs) {
        super(context);
        total = costs.size();
        List<CostDayItem> dayCostItems = new ArrayList<>();
        CostShowFragment.fillTitles(costs, dayCostItems);
        costDayItemAdapter = new CostDayItemAdapter(dayCostItems, getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_cost_show, null);
        ListView lv_cost = view.findViewById(R.id.lv_cost);
        TextView tv_cost = view.findViewById(R.id.tv_cost_show);
        tv_cost.setText("共有" + total + "条数据");
        lv_cost.setAdapter(costDayItemAdapter);

        Objects.requireNonNull(this.getWindow()).setLayout(MATCH_PARENT, WRAP_CONTENT);
        this.setContentView(view);
    }
}
