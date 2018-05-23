package com.jimo.mycost.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    private List<ItemDayCost> items;
    private LayoutInflater inflater;
    private Context context;

    public DayCostItemAdapter(List<ItemDayCost> items, Context context) {
        this.items = items;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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
        ItemDayCost item = items.get(i);
        if (item.getItemType() == MyConst.ITEM_TYPE1) {
            ViewHolder1 holder;
            if (view == null) {
                view = inflater.inflate(R.layout.item_day_cost_item, null);
                holder = new ViewHolder1();
                holder.tv_money = view.findViewById(R.id.tv_money);
                holder.tv_type = view.findViewById(R.id.tv_type);
                holder.tv_remark = view.findViewById(R.id.tv_remark);
                holder.rcv_temp_img = view.findViewById(R.id.rcv_temp_img);
                view.setTag(holder);
            } else {
                holder = (ViewHolder1) view.getTag();
            }
            holder.tv_money.setText(item.getMoney());
            holder.tv_type.setText(item.getType());
            holder.tv_remark.setText(item.getRemark());
            final RecyclerView rcv = holder.rcv_temp_img;
            rcv.setLayoutManager(new GridLayoutManager(context, 3));
            rcv.setAdapter(item.getAdapter());
            item.getAdapter().notifyDataSetChanged();
        } else {
            ViewHolder2 holder2;
            if (view == null) {
                view = inflater.inflate(R.layout.item_day_cost_title, null);
                holder2 = new ViewHolder2();
                holder2.tv_date = view.findViewById(R.id.tv_date);
                view.setTag(holder2);
            } else {
                holder2 = (ViewHolder2) view.getTag();
            }
            holder2.tv_date.setText(item.getDate());
        }
        return view;
    }

    private class ViewHolder1 {
        private TextView tv_type;
        private TextView tv_money;
        private TextView tv_remark;
        private RecyclerView rcv_temp_img;
    }

    private class ViewHolder2 {
        private TextView tv_date;
    }
}
