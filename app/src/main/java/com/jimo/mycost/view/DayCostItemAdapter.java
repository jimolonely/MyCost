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

public class DayCostAdapter extends BaseAdapter {

    private List<DayCostItem> items;
    private LayoutInflater inflater;
    private Context context;

    public DayCostAdapter(List<DayCostItem> items, Context context) {
        this.items = items;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.day_cost_item, null);
            holder = new ViewHolder();
            holder.tv_money = (TextView) view.findViewById(R.id.tv_money);
            holder.tv_type = (TextView) view.findViewById(R.id.tv_type);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_money.setText(items.get(i).getMoney());
        holder.tv_type.setText(items.get(i).getType());
        return view;
    }

    class ViewHolder {
        private TextView tv_type;
        private TextView tv_money;
    }
}
