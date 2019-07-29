package com.jimo.mycost.func.cloud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jimo.mycost.R;
import com.jimo.mycost.data.dto.CloudFileEntry;

import java.util.List;

/**
 * 文件列表适配
 *
 * @author jimo
 * @date 19-7-29 下午7:56
 */
public class FileEntryAdapter extends BaseAdapter {

    private List<CloudFileEntry> entries;
    private Context context;
    private LayoutInflater inflater;

    public FileEntryAdapter(List<CloudFileEntry> entries, Context context) {
        this.entries = entries;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int i) {
        return entries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        CloudFileEntry entry = entries.get(i);

        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_cloud_file_entry, null);
            holder = new ViewHolder();
            holder.tv_name = view.findViewById(R.id.tv_file_name);
            holder.tv_size = view.findViewById(R.id.tv_file_size);
            holder.tv_modify = view.findViewById(R.id.tv_file_modify_time);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tv_name.setText(entry.getName());
        holder.tv_size.setText(entry.getSize() + "");
        holder.tv_modify.setText(entry.getModifyTime());

        return view;
    }

    private class ViewHolder {
        private TextView tv_name;
        private TextView tv_size;
        private TextView tv_modify;
    }
}
