package com.jimo.mycost.func.time;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jimo.mycost.MyApp;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.TimeCostRecord;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * 展示正在进行的任务的列表item适配器
 */
public class RunningTaskItemAdapter extends BaseAdapter {

    private List<TimeCostRecord> items;
    private LayoutInflater inflater;
    private Context context;

    public RunningTaskItemAdapter(List<TimeCostRecord> items, Context context) {
        this.items = items;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TimeCostRecord item = items.get(position);

        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_running_task, null);
            holder = new ViewHolder();
            holder.tv_content = view.findViewById(R.id.tv_content);
            holder.tv_end = view.findViewById(R.id.tv_end);
            holder.tv_start = view.findViewById(R.id.tv_start);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_start.setText(item.getStart());
        holder.tv_end.setText(item.getEnd());
        holder.tv_content.setText(item.getBigType() + " " + item.getSmallType());

        // 设置背景颜色 TODO

        // 点击开始结束可以修改时间
        View finalView = view;
        holder.tv_start.setOnClickListener(v -> modifyTime(finalView, item, true));
        holder.tv_end.setOnClickListener(v -> modifyTime(finalView, item, false));
        return view;
    }

    private void modifyTime(View view, TimeCostRecord item, boolean start) {
        FuckUtil.showTimeSelectDialog(context, time -> {
            DbManager db = MyApp.dbManager;
            try {
                String day = JimoUtil.getDateBefore(0);
                if (start) {
                    item.setStart(day + " " + time + ":00");
                    db.update(item, "start");
                } else {
                    item.setEnd(day + " " + time + ":00");
                    db.update(item, "end");
                }
                this.notifyDataSetChanged();
            } catch (DbException e) {
                JimoUtil.mySnackbar(view, "保存失败：" + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private class ViewHolder {
        private TextView tv_start;
        private TextView tv_end;
        private TextView tv_content;
    }
}
