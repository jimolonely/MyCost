package com.jimo.mycost.func.life;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jimo.mycost.R;
import com.jimo.mycost.data.model.LifeRecord;

import java.util.List;

/**
 * @author jimo
 * @date 20-3-23 下午10:43
 */
public class LifeRecordAdapter extends BaseAdapter {

    private List<LifeRecord> list;
    private Context context;

    public LifeRecordAdapter(List<LifeRecord> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LifeRecord record = list.get(i);
        ViewHolder vh;
        View v;
        if (view == null) {
            v = LayoutInflater.from(context).inflate(R.layout.item_life_record, null);

            vh = new ViewHolder();
            vh.tv_name = v.findViewById(R.id.tv_life_name);
            vh.tv_type = v.findViewById(R.id.tv_life_type);
            vh.tv_start = v.findViewById(R.id.tv_life_start_time);
            vh.tv_end = v.findViewById(R.id.tv_life_end_time);

            v.setTag(vh);
        } else {
            v = view;
            vh = (ViewHolder) v.getTag();
        }
        vh.tv_name.setText(record.getName());
        vh.tv_type.setText(record.getTheme());
        vh.tv_start.setText(record.getStartTime());
        vh.tv_end.setText(record.getEndTime());

        return v;
    }

    class ViewHolder {
        TextView tv_name;
        TextView tv_type;
        TextView tv_start;
        TextView tv_end;
    }
}
