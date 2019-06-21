package com.jimo.mycost.func.time;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jimo.mycost.MyApp;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.TimeCostRecord;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;
import java.util.Map;

/**
 * 展示正在进行的任务的列表item适配器
 */
public class RunningTaskItemAdapter extends BaseAdapter {

    private List<TimeCostRecord> items;
    private LayoutInflater inflater;
    private Context context;
    private Map<String, Integer> colorMap;

    public RunningTaskItemAdapter(List<TimeCostRecord> items,
                                  Context context, Map<String, Integer> colorMap) {
        this.items = items;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.colorMap = colorMap;
    }

    public void setItems(List<TimeCostRecord> items) {
        this.items = items;
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
            holder.rl_item = view.findViewById(R.id.ll_item_running_task);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_start.setText(item.getStart());
        holder.tv_end.setText(item.getEnd());
        holder.tv_content.setText(item.getBigType() + " " + item.getSmallType());

        // 设置背景颜色
        String now = JimoUtil.getDateBefore(1) + " 00:00:00";
        if (now.equals(item.getEnd())) {
            // 运行中
//            holder.rl_item.setBackgroundColor(Color.rgb(201, 233, 41));
        } else {
            holder.rl_item.setBackgroundColor(colorMap.getOrDefault(item.getBigType(), Color.WHITE));
        }

        // 点击开始结束可以修改时间
        View finalView = view;
        holder.tv_start.setOnClickListener(v -> modifyTime(finalView, item, true));
        holder.tv_end.setOnClickListener(v -> modifyTime(finalView, item, false));
        // 长按删除
        holder.tv_content.setOnLongClickListener(v -> deleteItem(v, item));
        return view;
    }

    private boolean deleteItem(View v, TimeCostRecord item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("warning")
                .setMessage("确认删除吗？不可恢复！")
                .setPositiveButton("ok", (dialog, which) -> {
                    DbManager db = MyApp.dbManager;
                    try {
                        db.delete(item);
                        items.remove(item);
                        RunningTaskItemAdapter.this.notifyDataSetChanged();
                        JimoUtil.mySnackbar(v, "删除成功");
                    } catch (DbException e) {
                        JimoUtil.mySnackbar(v, "删除失败：" + e.getMessage());
                        e.printStackTrace();
                    }
                });
        builder.show();
        return true;
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
        private RelativeLayout rl_item;
    }
}
