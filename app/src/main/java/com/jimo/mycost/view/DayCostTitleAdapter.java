package com.jimo.mycost.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jimo.mycost.R;

import java.util.List;

/**
 * Created by root on 17-7-22.
 * 适配器
 */

public class DayCostTitleAdapter extends BaseAdapter {

    private List<DayCostTitle> titles;
    private LayoutInflater inflater;
    private Context context;

    public DayCostTitleAdapter(List<DayCostTitle> titles, Context context) {
        this.titles = titles;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int i) {
        return titles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.day_cost_title, null);
            holder = new ViewHolder();
            holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
            holder.list_item = (CostListView) view.findViewById(R.id.cost_list_view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        List<DayCostItem> items = this.titles.get(i).getItems();
        DayCostItemAdapter dayCostItemAdapter = new DayCostItemAdapter(items, context);
        holder.list_item.setAdapter(dayCostItemAdapter);
        holder.tv_date.setText(this.titles.get(i).getDate());
        return view;
    }

    class ViewHolder {
        private TextView tv_date;
        private CostListView list_item;
    }
}
