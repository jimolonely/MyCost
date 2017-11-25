package com.jimo.mycost.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;

import java.util.List;

/**
 * Created by root on 17-7-22.
 * 适配器
 */

public class DayCostItemAdapter extends BaseAdapter {

    private List<DayCostItem> items;
    private LayoutInflater inflater;
    private Context context;

    public DayCostItemAdapter(List<DayCostItem> items, Context context) {
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
    public int getItemViewType(int position) {
        return items.get(position).getItemType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DayCostItem item = items.get(i);
        if (item.getItemType() == MyConst.ITEM_TYPE1) {
            ViewHolder1 holder = null;
            if (view == null) {
                view = inflater.inflate(R.layout.item_day_cost_item, null);
                holder = new ViewHolder1();
                holder.tv_money = (TextView) view.findViewById(R.id.tv_money);
                holder.tv_type = (TextView) view.findViewById(R.id.tv_type);
                view.setTag(holder);
            } else {
                holder = (ViewHolder1) view.getTag();
            }
            holder.tv_money.setText(item.getMoney());
            holder.tv_type.setText(item.getType());
        } else {
            ViewHolder2 holder2 = null;
            if (view == null) {
                view = inflater.inflate(R.layout.item_day_cost_title, null);
                holder2 = new ViewHolder2();
                holder2.tv_date = (TextView) view.findViewById(R.id.tv_date);
                view.setTag(holder2);
            } else {
                holder2 = (ViewHolder2) view.getTag();
            }
            holder2.tv_date.setText(item.getDate());
        }
        return view;
    }

    class ViewHolder1 {
        private TextView tv_type;
        private TextView tv_money;
    }

    class ViewHolder2 {
        private TextView tv_date;
    }
}
