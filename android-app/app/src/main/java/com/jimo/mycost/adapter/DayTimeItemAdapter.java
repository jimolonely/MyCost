package com.jimo.mycost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jimo.mycost.R;

import java.util.List;

/**
 * Created by root on 17-11-24.
 * 适配器
 */

public class DayTimeItemAdapter extends BaseAdapter {

    private List<ItemDayTime> items;
    private LayoutInflater inflater;

    public DayTimeItemAdapter(List<ItemDayTime> items, Context context) {
        this.items = items;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ItemDayTime item = items.get(i);
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_day_time_item, null);
            holder = new ViewHolder();
            holder.tv_subject = (TextView) view.findViewById(R.id.tv_subject);
            holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_time.setText(item.getTime());
        holder.tv_subject.setText(item.getSubject());
        return view;
    }

    private class ViewHolder {
        private TextView tv_subject;
        private TextView tv_time;
    }
}
